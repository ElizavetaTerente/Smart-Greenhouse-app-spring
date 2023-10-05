package at.qe.skeleton.api.dto;

import at.qe.skeleton.model.SensorType;
import java.time.LocalDateTime;

public class SensorDataDTO {

    private Long sensorDataId;
    private SensorType sensorType;
    private LocalDateTime dataTime;
    private double dataValue;
    private Long sensorStationId;

    public SensorDataDTO() {}

    public SensorDataDTO(Long sensorDataId, SensorType sensorType, LocalDateTime dataTime,
                         double dataValue, Long sensorStationId) {
        this.sensorDataId = sensorDataId;
        this.sensorType = sensorType;
        this.dataTime = dataTime;
        this.dataValue = dataValue;
        this.sensorStationId = sensorStationId;
    }

    public Long getSensorDataId() {
        return sensorDataId;
    }

    public void setSensorDataId(Long sensorDataId) {
        this.sensorDataId = sensorDataId;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public LocalDateTime getDataTime() {
        return dataTime;
    }

    public void setDataTime(LocalDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public double getDataValue() {
        return dataValue;
    }

    public void setDataValue(double dataValue) {
        this.dataValue = dataValue;
    }

    public Long getSensorStationId() {
        return sensorStationId;
    }

    public void setSensorStationId(Long sensorStationId) {
        this.sensorStationId = sensorStationId;
    }
}
