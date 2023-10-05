package at.qe.skeleton.model.frontEndDto;

import at.qe.skeleton.model.SensorData;

public class CurrentStationValuesDto {
    SensorData soilHumidity;
    SensorData temperature;
    SensorData airHumidity;
    SensorData airPressure;
    SensorData airQuality;
    SensorData light;
    int transmissionInterval;
    boolean Accessible;

    public SensorData getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(SensorData soilHumidity) {
        this.soilHumidity = soilHumidity;
    }

    public SensorData getTemperature() {
        return temperature;
    }

    public void setTemperature(SensorData temperature) {
        this.temperature = temperature;
    }

    public SensorData getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(SensorData airHumidity) {
        this.airHumidity = airHumidity;
    }

    public SensorData getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(SensorData airPressure) {
        this.airPressure = airPressure;
    }

    public SensorData getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(SensorData airQuality) {
        this.airQuality = airQuality;
    }

    public SensorData getLight() {
        return light;
    }

    public void setLight(SensorData light) {
        this.light = light;
    }

    public int getTransmissionInterval() {
        return transmissionInterval;
    }

    public void setTransmissionInterval(int transmissionInterval) {
        this.transmissionInterval = transmissionInterval;
    }

    public boolean isAccessible() {
        return Accessible;
    }

    public void setAccessible(boolean accessible) {
        Accessible = accessible;
    }
}
