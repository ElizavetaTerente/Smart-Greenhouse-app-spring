package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

@Entity
public class SensorLimit implements Persistable<Long>, Serializable, Comparable<SensorLimit> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sensorLimitId;

    private SensorType sensorType;

    private Duration overrunInterval;

    private double thresholdMin;

    private double thresholdMax;


    @ManyToOne
    @JoinColumn(name = "sensorstation_id")
    @JsonBackReference
    private SensorStation sensorStation;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.sensorLimitId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SensorLimit)) {
            return false;
        }
        final SensorLimit other = (SensorLimit) obj;
        if (!Objects.equals(this.sensorLimitId, other.sensorLimitId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.SensorLimit[ id=" + sensorLimitId + " ]";
    }

    @Override
    public Long getId() {
        return getSensorLimitId();
    }

    public void setId(Long id) {
        setSensorLimitId(id);
    }

    @Override
    public boolean isNew() {
        return (null == sensorType);
    }

	@Override
	public int compareTo(SensorLimit o) {
		return this.sensorLimitId.compareTo(o.getId());
	}

    public Long getSensorLimitId() {
        return sensorLimitId;
    }

    public void setSensorLimitId(Long sensorLimitId) {
        this.sensorLimitId = sensorLimitId;
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

    public SensorStation getSensorStation() {
        return sensorStation;
    }

    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
    }
}
