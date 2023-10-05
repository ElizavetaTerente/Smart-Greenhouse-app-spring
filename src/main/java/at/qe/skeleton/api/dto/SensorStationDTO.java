package at.qe.skeleton.api.dto;


import java.util.HashSet;
import java.util.Set;

public class SensorStationDTO {
    private Long sensorStationId;

    private Long sensorStationDipId;

    private Set<SensorLimitDTO> sensorLimits = new HashSet<>();
    private String sensorStationName;

    private boolean enabled;

    private boolean accepted;
    private boolean accessible;

    public SensorStationDTO() {}

    public SensorStationDTO(Long sensorStationId, Long sensorStationDipId, Set<SensorLimitDTO> sensorLimits, String sensorStationName, boolean accessible, boolean enabled, boolean accepted) {
        this.sensorStationId = sensorStationId;
        this.sensorStationDipId = sensorStationDipId;
        this.sensorLimits = sensorLimits;
        this.sensorStationName = sensorStationName;
        this.accessible = accessible;
        this.enabled = enabled;
        this.accepted = accepted;
    }

    public Long getSensorStationId() {
        return sensorStationId;
    }

    public void setSensorStationId(Long sensorStationId) {
        this.sensorStationId = sensorStationId;
    }

    public Long getSensorStationDipId() {
        return sensorStationDipId;
    }

    public void setSensorStationDipId(Long sensorStationDipId) {
        this.sensorStationDipId = sensorStationDipId;
    }

    public Set<SensorLimitDTO> getSensorLimits() {
        return sensorLimits;
    }

    public void setSensorLimits(Set<SensorLimitDTO> sensorLimits) {
        this.sensorLimits = sensorLimits;
    }

    public String getSensorStationName() {
        return sensorStationName;
    }

    public void setSensorStationName(String sensorStationName) {
        this.sensorStationName = sensorStationName;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
