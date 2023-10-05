package at.qe.skeleton.model.frontEndDto;

import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.model.SensorStation;

import java.util.Collection;
import java.util.Set;

public class AccesspointSensorStationsDto {
    private Accesspoint accesspoint;
    private CustomTime time;
    private Collection<SensorStation> sensorStations;

    public AccesspointSensorStationsDto(Accesspoint accesspoint, Collection<SensorStation> sensorStations) {
        this.accesspoint = accesspoint;
        this.sensorStations = sensorStations;
    }

    public CustomTime getTime() {
        return time;
    }

    public void setTime(CustomTime time) {
        this.time = time;
    }

    public void setSensorStations(Collection<SensorStation> sensorStations) {
        this.sensorStations = sensorStations;
    }

    public Accesspoint getAccesspoint() {
        return accesspoint;
    }

    public void setAccesspoint(Accesspoint accesspoint) {
        this.accesspoint = accesspoint;
    }

    public Collection<SensorStation> getSensorStations() {
        return sensorStations;
    }

    public void setSensorStations(Set<SensorStation> sensorStations) {
        this.sensorStations = sensorStations;
    }

}
