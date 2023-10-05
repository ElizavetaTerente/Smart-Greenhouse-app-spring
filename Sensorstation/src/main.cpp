#include <Arduino.h>
#include <ArduinoBLE.h>
#include <Wire.h>
#include <SPI.h>
#include "Adafruit_BME680.h"

#define RGB_LED_RED D0
#define RGB_LED_BLUE D1
#define RGB_LED_GREEN D2
#define S1 D3
#define S2 D4
#define DIP_1 D5
#define DIP_2 D6
#define DIP_3 D7
#define DIP_4 D8
#define DIP_5 D9
#define DIP_6 D10
#define DIP_7 D11
#define DIP_8 D12
#define PIEZO_BUZZER A3
#define PHOTO_TRANSISTOR A7
#define HYGROMETER A6
#define SDA A4
#define SCL A5

#define DEBOUNCE_INTERVAL 50    // debounce intervall for Buttons in ms
#define UPDATE_INTERVAL 5000    // how often the BLE Characteristics get updated in ms
#define SAMPLES 20              // how many samples between update Intervalls will be taken

void rgbLedSetValue(int redValue, int greenValue, int blueValue);
int getStationId();

void blePeripheralConnectedHandler(BLEDevice central);
void blePeripheralDisconnectedHandler(BLEDevice central);
void manufacturerNameHandler(BLEDevice central, BLECharacteristic characteristics);
void objectIdHandler(BLEDevice central, BLECharacteristic characteristics);
void limitHandler(BLEDevice central, BLECharacteristic characteristics);

BLEService deviceInfoService("180A");
BLEStringCharacteristic manufacturerNameChar("2A29", BLERead, 64);
BLEIntCharacteristic objectIDChar("2AC3", BLERead);

BLEService sensorDataService("181A");
BLEIntCharacteristic irradianceChar("2A77", BLERead);
BLEIntCharacteristic temperatureChar("2A6E", BLERead);
BLEIntCharacteristic airHumidityChar("2A6F", BLERead);
BLEIntCharacteristic soilHumidityChar("FB170677-D837-4A49-9688-0736D93EAED5", BLERead);
BLEIntCharacteristic airPressureChar("2A6D", BLERead);
BLEIntCharacteristic gasResistanceChar("2AFB", BLERead);

BLEService sensorLimitService("93B5EAD0-0252-460D-9E23-414627CE26E8");
BLEByteCharacteristic irradianceLimitChar("93B5EAD1-0252-460D-9E23-414627CE26E8", BLERead | BLEWrite | BLENotify);
BLEByteCharacteristic temperatureLimitChar("93B5EAD2-0252-460D-9E23-414627CE26E8", BLERead | BLEWrite | BLENotify);
BLEByteCharacteristic airHumidityLimitChar("93B5EAD3-0252-460D-9E23-414627CE26E8", BLERead | BLEWrite | BLENotify);
BLEByteCharacteristic soilHumidityLimitChar("93B5EAD4-0252-460D-9E23-414627CE26E8", BLERead | BLEWrite | BLENotify);
BLEByteCharacteristic airPressureLimitChar("93B5EAD5-0252-460D-9E23-414627CE26E8", BLERead | BLEWrite | BLENotify);
BLEByteCharacteristic gasResistanceLimitChar("93B5EAD6-0252-460D-9E23-414627CE26E8", BLERead | BLEWrite | BLENotify);
BLEBooleanCharacteristic gardenerResetChar("93B5EAD7-0252-460D-9E23-414627CE26E8", BLERead | BLENotify);

Adafruit_BME680 bme;

enum Ble_state {idle, polling, connected};
Ble_state bleState = idle;
int limits[6] = {0};

void setup() {
  pinMode(RGB_LED_RED, OUTPUT);
  pinMode(RGB_LED_BLUE, OUTPUT);
  pinMode(RGB_LED_GREEN, OUTPUT);
  pinMode(S1, INPUT_PULLUP);
  pinMode(S2, INPUT_PULLUP);
  pinMode(DIP_1, INPUT_PULLUP);
  pinMode(DIP_2, INPUT_PULLUP);
  pinMode(DIP_3, INPUT_PULLUP);
  pinMode(DIP_4, INPUT_PULLUP);
  pinMode(DIP_5, INPUT_PULLUP);
  pinMode(DIP_6, INPUT_PULLUP);
  pinMode(DIP_7, INPUT_PULLUP);
  pinMode(DIP_8, INPUT_PULLUP);
  pinMode(PIEZO_BUZZER, OUTPUT);
  pinMode(PHOTO_TRANSISTOR, INPUT);
  pinMode(HYGROMETER, INPUT);


  Serial.begin(9600);
  //while(!Serial);
  Serial.println("Serial started");

  if (!bme.begin()) {
    Serial.println("Could not find a valid BME680 sensor, check wiring!");
    while (1);
  }

  // Set up oversampling and filter initialization
  bme.setTemperatureOversampling(BME680_OS_8X);
  bme.setHumidityOversampling(BME680_OS_2X);
  bme.setPressureOversampling(BME680_OS_4X);
  bme.setIIRFilterSize(BME680_FILTER_SIZE_3);
  bme.setGasHeater(320, 150); // 320*C for 150 ms

  if(!BLE.begin()) {
    Serial.println("BLE didn't start");
    while(1);
  }
  Serial.println("BLE started");

  String name = "Sensorstation G5T2 " + String(getStationId());
  char nameChar[64];
  name.toCharArray(nameChar, 64);

  BLE.setLocalName(nameChar);
  BLE.setDeviceName(nameChar);

  byte identifier = getStationId();
  byte manufacturerData[2] = {identifier, 0x00};
  
  BLE.setManufacturerData(manufacturerData, sizeof(manufacturerData));

  BLE.setEventHandler(BLEConnected, blePeripheralConnectedHandler);
  BLE.setEventHandler(BLEDisconnected, blePeripheralDisconnectedHandler);

  manufacturerNameChar.writeValue("WS BLE Device G5T2");
  manufacturerNameChar.setEventHandler(BLERead, manufacturerNameHandler);
  objectIDChar.writeValue(getStationId());
  objectIDChar.setEventHandler(BLERead, objectIdHandler);

  deviceInfoService.addCharacteristic(manufacturerNameChar);
  deviceInfoService.addCharacteristic(objectIDChar);

  BLEDescriptor irradianceCharDescriptor("290C", "light level");
  irradianceChar.addDescriptor(irradianceCharDescriptor);
  BLEDescriptor temperatureCharDescriptor("290C", "temperature");
  temperatureChar.addDescriptor(temperatureCharDescriptor);
  BLEDescriptor airHumidityCharDescriptor("290C", "air humidity");
  airHumidityChar.addDescriptor(airHumidityCharDescriptor);
  BLEDescriptor soilHumidityCharDescriptor("290C", "soil humidity");
  soilHumidityChar.addDescriptor(soilHumidityCharDescriptor);
  BLEDescriptor airPressureCharDescriptor("290C", "air pressure");
  airPressureChar.addDescriptor(airPressureCharDescriptor);
  BLEDescriptor gasResistanceCharDesriptor("290C", "gas resistance");
  gasResistanceChar.addDescriptor(gasResistanceCharDesriptor);

  sensorDataService.addCharacteristic(irradianceChar);
  sensorDataService.addCharacteristic(temperatureChar);
  sensorDataService.addCharacteristic(airHumidityChar);
  sensorDataService.addCharacteristic(soilHumidityChar);
  sensorDataService.addCharacteristic(airPressureChar);
  sensorDataService.addCharacteristic(gasResistanceChar);

  BLEDescriptor irradianceLimitCharDescriptor("290C", "light level limit");
  irradianceLimitChar.addDescriptor(irradianceLimitCharDescriptor);
  irradianceLimitChar.setEventHandler(BLEWritten, limitHandler);
  BLEDescriptor temperatureLimitCharDescriptor("290C", "temperature limit");
  temperatureLimitChar.addDescriptor(temperatureLimitCharDescriptor);
  temperatureLimitChar.setEventHandler(BLEWritten, limitHandler);
  BLEDescriptor airHumidityLimitCharDescriptor("290C", "air humidity limit");
  airHumidityLimitChar.addDescriptor(airHumidityLimitCharDescriptor);
  airHumidityChar.setEventHandler(BLEWritten, limitHandler);
  BLEDescriptor soilHumidityLimitCharDescriptor("290C", "soil humidity limit");
  soilHumidityLimitChar.addDescriptor(soilHumidityLimitCharDescriptor);
  soilHumidityLimitChar.setEventHandler(BLEWritten, limitHandler);
  BLEDescriptor airPressureLimitCharDescriptor("290C", "air pressure limit");
  airPressureLimitChar.addDescriptor(airPressureLimitCharDescriptor);
  airPressureLimitChar.setEventHandler(BLEWritten, limitHandler);
  BLEDescriptor gasResistanceLimitCharDesriptor("290C", "gas resistance limit");
  gasResistanceLimitChar.addDescriptor(gasResistanceLimitCharDesriptor);
  gasResistanceLimitChar.setEventHandler(BLEWritten, limitHandler);
  BLEDescriptor gardenerResetCharDescriptor("290C", "gardener rest");
  gardenerResetChar.addDescriptor(gardenerResetCharDescriptor);

  sensorLimitService.addCharacteristic(irradianceLimitChar);
  sensorLimitService.addCharacteristic(temperatureLimitChar);
  sensorLimitService.addCharacteristic(airHumidityLimitChar);
  sensorLimitService.addCharacteristic(soilHumidityLimitChar);
  sensorLimitService.addCharacteristic(airPressureLimitChar);
  sensorLimitService.addCharacteristic(gasResistanceLimitChar);
  sensorLimitService.addCharacteristic(gardenerResetChar);

  BLE.addService(deviceInfoService);
  BLE.addService(sensorDataService);
  BLE.addService(sensorLimitService);

  BLE.advertise();
}

void loop() {
  boolean bleButtonPressed = false;
  boolean gardenerResetButtonPressed = false;

  static unsigned long debounceTimer = 0;
  if(millis() - debounceTimer >= DEBOUNCE_INTERVAL) {
    debounceTimer = millis();

    static int s1StateOld = HIGH;
    int s1State = digitalRead(S1);
    if(s1State != s1StateOld) {
      s1StateOld = s1State;
      bleButtonPressed = false;
      if(s1State == LOW) {
        bleButtonPressed = true;
      } 
    }

    static int s2StateOld = HIGH;
    int s2State = digitalRead(S2);
    if(s2State != s2StateOld) {
      s2StateOld = s2State;
      gardenerResetButtonPressed = false;
      if(s2State == LOW) {
        gardenerResetButtonPressed = true;
      }
    }
  }

  static unsigned long timeOutTimer;

  switch(bleState) {
    //indicated by blue static white LED
    case idle:
      rgbLedSetValue(0, 0, 100);
      if(bleButtonPressed) {
        bleState = polling;
        timeOutTimer = millis();
      }
      break;
    //indicated by blinking blue LED
    case polling:
      //5 minute timeout
      if(millis() - timeOutTimer >= 5*60*1000 || bleButtonPressed) {
        bleState = idle;
        break;
      }

      BLE.poll();

      static unsigned long ledBlinkTimer = millis();
      static boolean ledState = false;
      if(millis() - ledBlinkTimer >= 500) {
        ledBlinkTimer = millis();
        if(ledState) {
          rgbLedSetValue(0,0,0);
        } else {
          rgbLedSetValue(0, 0, 100);
        }
        ledState = !ledState;
      }
      break;
    
    case connected:
      BLE.poll();

      if(bleButtonPressed) {
        BLE.disconnect();
        bleState = idle;
        break;
      }

      if(gardenerResetButtonPressed) {
        gardenerResetChar.writeValue(true);
        for(int i=0; i<=5; i++) {
          limits[i] = 0;
        }
      }

      static int sample = 0;
      static int soilHumidity = 0;
      static int light = 0;
      static unsigned long readTimer = 0;

      if(millis() - readTimer >= UPDATE_INTERVAL/SAMPLES) {
        readTimer = millis();
        if(++sample == 1) {
          bme.beginReading();
        }
        float sampleWeight = 1/sample;

        soilHumidity = (1-sampleWeight)*soilHumidity + sampleWeight*analogRead(HYGROMETER);
        light = (1-sampleWeight)*light + sampleWeight*analogRead(PHOTO_TRANSISTOR);

        if(sample == SAMPLES) {
          sample = 0;

          bme.endReading();
          Serial.print("Temperature: ");
          Serial.println(bme.temperature);
          temperatureChar.writeValue(bme.temperature);
          Serial.print("Pressure: ");
          Serial.println(bme.pressure);
          airPressureChar.writeValue(bme.pressure);
          Serial.print("Humidity: ");
          Serial.println(bme.humidity);
          airHumidityChar.writeValue(bme.humidity);
          Serial.print("Gas: ");
          Serial.println(bme.gas_resistance);
          gasResistanceChar.writeValue(bme.gas_resistance);
          

          int soilHumidity = analogRead(HYGROMETER);
          Serial.print("earth Humidity: ");
          Serial.println(soilHumidity);
          soilHumidityChar.writeValue(soilHumidity);

          int light = analogRead(PHOTO_TRANSISTOR);
          Serial.print("light level: ");
          Serial.println(light);
          irradianceChar.writeValue(light);

          Serial.println("----------------------------------------");
        }
      }


      static int limitIndicatorIterator = 0;
      static unsigned long limitIndicatorTimer = millis();
      static int blink_state = 0;
      if((blink_state == 1 && millis()-limitIndicatorTimer >= 250) || (blink_state == 2 && millis()-limitIndicatorTimer >= 4000)) {
        limitIndicatorTimer = millis();
        blink_state = 0;
        rgbLedSetValue(limits[limitIndicatorIterator], 255 - limits[limitIndicatorIterator], 0);
      } else if(blink_state == 0 && millis()-limitIndicatorTimer >= 1000) {
        limitIndicatorTimer = millis();
        blink_state = 1;
        rgbLedSetValue(0,0,0);
        limitIndicatorIterator++;
        if(limitIndicatorIterator > 5) {
          limitIndicatorIterator = 0;
          blink_state = 2;
        }
      }
      break;
  }

}

void rgbLedSetValue(int redValue, int greenValue, int blueValue) {
  analogWrite(RGB_LED_RED, redValue);
  analogWrite(RGB_LED_GREEN, greenValue/2); // green led is way too strong
  analogWrite(RGB_LED_BLUE, blueValue);
}

int getStationId() {
  return 1*!digitalRead(DIP_1) +
    2*!digitalRead(DIP_2) +
    4*!digitalRead(DIP_3) +
    8*!digitalRead(DIP_4) +
    16*!digitalRead(DIP_5) +
    32*!digitalRead(DIP_6) +
    64*!digitalRead(DIP_7) +
    128*!digitalRead(DIP_8);
}

void blePeripheralConnectedHandler(BLEDevice central) {
  Serial.println("central connected");
  Serial.println(central.address());
  bleState = connected;
}

void blePeripheralDisconnectedHandler(BLEDevice central) {
  Serial.println("central disconnected");
  bleState = idle;
}

void manufacturerNameHandler(BLEDevice central, BLECharacteristic characteristics) {
  Serial.println("manufacturereNameHandler");
}

void objectIdHandler(BLEDevice central, BLECharacteristic characteristics){
  int objectId = getStationId();
  Serial.print("objectIdHandler");
  Serial.println(objectId);
  objectIDChar.writeValue(objectId);
}

void limitHandler(BLEDevice central, BLECharacteristic characteristics)
{
  Serial.println("limit indicator has been set");
  limits[0] = irradianceLimitChar.value();
  limits[1] = temperatureLimitChar.value();
  limits[2] = airHumidityLimitChar.value();
  limits[3] = soilHumidityLimitChar.value();
  limits[4] = airPressureLimitChar.value();
  limits[5] = gasResistanceLimitChar.value();
}
