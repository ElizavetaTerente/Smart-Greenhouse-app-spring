# Arduino Sensor Station Documentation

## Introduction
This documentation describes the color-blink code and BLE-GATT Services used in the Project.

## RGB Blink Code 
The LED blinks once for a second for each sensor in a given order, after every sensor indicator blinked once there will be a 4 second pause before repeating
### Sensor Order
The sensor indication is arranged in the following order:
1. Light
2. Temperature
3. Air Humidity
4. Soil Humidity
5. Air Pressure
6. Air Quality

### Color Indication
The LED blinks with different colors to indicate the status of each sensor value within the threshold.

| Color     | Status          | Description                                |
|-----------|-----------------|--------------------------------------------|
| Green     | Good            | Sensor value is within the threshold        |
| Yellow    | Moderate        | Sensor value is slightly outside threshold  |
| Red       | Critical        | Sensor value is significantly outside threshold  |

## GATT-Services

### Device Information Service (UUID: 180A)

| Description  | UUID | Permissions | Data Type     |
|---|------|-------------|---------------|
| Manufacturer Name | 2A29 | Read        | UTF-8 String  |
| Object ID | 2AC3 | Read        | Signed 32-bit int |

### Sensor Data Service (UUID: 181A)

| Description  | UUID                              | Permissions | Data Type          |
|---|-----------------------------------|-------------|--------------------|
| illuminance | 2A77                              | Read        | Signed 32-bit int  |
| temperature | 2A6E                              | Read        | Signed 32-bit int  |
| air humidity | 2A6F                              | Read        | Signed 32-bit int  |
| soil humidity | FB170677-D837-4A49-9688-0736D93EAED5 | Read        | Signed 32-bit int  |
| air pressure | 2A6D                              | Read        | Signed 32-bit int  |
| air quality | 2AFB                              | Read        | Signed 32-bit int  |

### Sensor Limit Service (UUID: 93B5EAD0-0252-460D-9E23-414627CE26E8)

| Description | UUID                              | Permissions       | Data Type            |
|---|-----------------------------------|-------------------|----------------------|
| illuminance limit | 93B5EAD1-0252-460D-9E23-414627CE26E8 | Read, Write, Notify | Unsigned 8-bit int  |
| temperature limit | 93B5EAD2-0252-460D-9E23-414627CE26E8 | Read, Write, Notify | Unsigned 8-bit int  |
|  air humidity limit | 93B5EAD3-0252-460D-9E23-414627CE26E8 | Read, Write, Notify | Unsigned 8-bit int  |
| soil humidity limit  | 93B5EAD4-0252-460D-9E23-414627CE26E8 | Read, Write, Notify | Unsigned 8-bit int  |
|  air pressure limit | 93B5EAD5-0252-460D-9E23-414627CE26E8 | Read, Write, Notify | Unsigned 8-bit int  |
|  air quality limit | 93B5EAD6-0252-460D-9E23-414627CE26E8 | Read, Write, Notify | Unsigned 8-bit int  |
| gardener reset pushed | 93B5EAD7-0252-460D-9E23-414627CE26E8 | Read, Notify      | Boolean              |
