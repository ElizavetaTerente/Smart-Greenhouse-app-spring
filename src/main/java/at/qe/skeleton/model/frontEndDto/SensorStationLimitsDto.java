package at.qe.skeleton.model.frontEndDto;

public class SensorStationLimitsDto {

    Limit soilHumidity;
    Limit temperature;
    Limit airHumidity;
    Limit airPressure;
    Limit airQuality;
    Limit light;

    public Limit getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(Limit soilHumidity) {
        this.soilHumidity = soilHumidity;
    }

    public Limit getTemperature() {
        return temperature;
    }

    public void setTemperature(Limit temperature) {
        this.temperature = temperature;
    }

    public Limit getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(Limit airHumidity) {
        this.airHumidity = airHumidity;
    }

    public Limit getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(Limit airPressure) {
        this.airPressure = airPressure;
    }

    public Limit getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(Limit airQuality) {
        this.airQuality = airQuality;
    }

    public Limit getLight() {
        return light;
    }

    public void setLight(Limit light) {
        this.light = light;
    }
}
