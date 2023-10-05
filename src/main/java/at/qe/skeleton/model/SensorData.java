package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class SensorData implements Persistable<Long>, Serializable, Comparable<SensorData> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sensorDataId;

    private SensorType sensorType;

    private LocalDateTime dataTime;

    private double dataValue;

    @ManyToOne
    @JoinColumn(name = "sensorstation_id")
    @JsonBackReference
    private SensorStation sensorStation;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.sensorDataId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SensorData)) {
            return false;
        }
        final SensorData other = (SensorData) obj;
        return Objects.equals(this.sensorDataId, other.sensorDataId);
    }

    /*
    @Override
    public String toString() {
        return "at.qe.skeleton.model.SensorData[ id=" + sensorDataId + " ]";
        }
     */

    @Override
    public String toString() {
        return "SensorData{" +
                "dataTime=" + dataTime +
                '}';
    }

    @Override
    public Long getId() {
        return getSensorDataId();
    }

    public void setId(Long id) {
        setSensorDataId(id);
    }

    @Override
    public boolean isNew() {
        return (dataTime == null);
    }

    @Override
    public int compareTo(SensorData o) {
        return this.sensorDataId.compareTo(o.getSensorDataId());
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

    public void setDataTime(LocalDateTime time) {
        this.dataTime = time;
    }

    public double getDataValue() {
        return dataValue;
    }

    public void setDataValue(double value) {
        this.dataValue = value;
    }

    public SensorStation getSensorStation() {
        return sensorStation;
    }

    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
    }


}
