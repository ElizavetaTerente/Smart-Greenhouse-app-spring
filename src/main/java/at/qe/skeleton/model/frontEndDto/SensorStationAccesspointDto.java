package at.qe.skeleton.model.frontEndDto;

import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.model.SensorStation;

public class SensorStationAccesspointDto {
    private SensorStation house;
    private Accesspoint accesspoint;
    private boolean checked;

    public SensorStationAccesspointDto(SensorStation house, Accesspoint accesspoint) {
        this.house = house;
        this.accesspoint = accesspoint;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public SensorStation getHouse() {
        return house;
    }

    public void setHouse(SensorStation house) {
        this.house = house;
    }

    public Accesspoint getAccesspoint() {
        return accesspoint;
    }

    public void setAccesspoint(Accesspoint accesspoint) {
        this.accesspoint = accesspoint;
    }
}
