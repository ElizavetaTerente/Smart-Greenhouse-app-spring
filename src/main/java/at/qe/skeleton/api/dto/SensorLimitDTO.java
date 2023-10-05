package at.qe.skeleton.api.dto;

import at.qe.skeleton.model.SensorType;

import java.time.Duration;

public class SensorLimitDTO {

    private Long sensorLimitId;

    private SensorType sensorType;

    private Duration overrunInterval;

    private double thresholdMin;

    private double thresholdMax;

    public Long getSensorLimitId() {
        return sensorLimitId;
    }

    public void setSensorLimitId(Long sensorLimitsId) {
        this.sensorLimitId = sensorLimitsId;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public Duration getOverrunInterval() {
        return overrunInterval;
    }

    public void setOverrunInterval(Duration overrunInterval) {
        this.overrunInterval = overrunInterval;
    }

    public double getThresholdMin() {
        return thresholdMin;
    }

    public void setThresholdMin(double thresholdMin) {
        this.thresholdMin = thresholdMin;
    }

    public double getThresholdMax() {
        return thresholdMax;
    }

    public void setThresholdMax(double thresholdMax) {
        this.thresholdMax = thresholdMax;
    }
}
