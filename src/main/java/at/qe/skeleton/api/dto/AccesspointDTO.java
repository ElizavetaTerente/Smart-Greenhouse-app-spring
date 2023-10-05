package at.qe.skeleton.api.dto;

import java.sql.Time;
import java.util.List;

public class AccesspointDTO {

    private Long accesspointId;
    private String locationName;
    private boolean enabled;

    private boolean accepted;
    private boolean searchingSensorStation;
    private Time transmissionInterval;

    private List<SensorStationDTO> sensorStations;

    public AccesspointDTO(){}

    public AccesspointDTO(Long accesspointId, String locationName,boolean accepted, boolean enabled, boolean searchingSensorStation, Time transmissionInterval, List<SensorStationDTO> sensorStations) {
        this.accesspointId = accesspointId;
        this.locationName = locationName;
        this.enabled = enabled;
        this.searchingSensorStation = searchingSensorStation;
        this.transmissionInterval = transmissionInterval;
        this.sensorStations = sensorStations;
        this.accepted = accepted;
    }

    public List<SensorStationDTO> getSensorStations() {
        return sensorStations;
    }

    public void setSensorStations(List<SensorStationDTO> sensorStations) {
        this.sensorStations = sensorStations;
    }

    public Long getAccesspointId() {
        return accesspointId;
    }

    public void setAccesspointId(Long accesspointId) {
        this.accesspointId = accesspointId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public boolean isSearchingSensorStation() {
        return searchingSensorStation;
    }

    public void setSearchingSensorStation(boolean searchingSensorStation) {
        this.searchingSensorStation = searchingSensorStation;
    }

    public Time getTransmissionInterval() {
        return transmissionInterval;
    }

    public void setTransmissionInterval(Time transmissionInterval) {
        this.transmissionInterval = transmissionInterval;
    }
}
