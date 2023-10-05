package at.qe.skeleton.model.frontEndDto;

import at.qe.skeleton.model.SensorData;

import java.util.*;

public class SensorDataOfAllSensorsInTimeRangeDto {
    List<SensorData> soilHumidity = new ArrayList<>();
    List<SensorData> temperature = new ArrayList<>();
    List<SensorData> airHumidity = new ArrayList<>();
    List<SensorData> airPressure = new ArrayList<>();
    List<SensorData> airQuality = new ArrayList<>();
    List<SensorData> light = new ArrayList<>();

    public SensorDataOfAllSensorsInTimeRangeDto() {
    }

    public SensorDataOfAllSensorsInTimeRangeDto(List<SensorData> soilHumidity, List<SensorData> temperature, List<SensorData> airHumidity, List<SensorData> airPressure, List<SensorData> airQuality, List<SensorData> light) {
        this.soilHumidity = soilHumidity;
        this.temperature = temperature;
        this.airHumidity = airHumidity;
        this.airPressure = airPressure;
        this.airQuality = airQuality;
        this.light = light;
    }

    public List<SensorData> getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(List<SensorData> soilHumidity) {
        this.soilHumidity = soilHumidity;
    }

    public List<SensorData> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<SensorData> temperature) {
        this.temperature = temperature;
    }

    public List<SensorData> getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(List<SensorData> airHumidity) {
        this.airHumidity = airHumidity;
    }

    public List<SensorData> getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(List<SensorData> airPressure) {
        this.airPressure = airPressure;
    }

    public List<SensorData> getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(List<SensorData> airQuality) {
        this.airQuality = airQuality;
    }

    public List<SensorData> getLight() {
        return light;
    }

    public void setLight(List<SensorData> light) {
        this.light = light;
    }
}
