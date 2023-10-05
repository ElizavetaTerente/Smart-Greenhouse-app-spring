package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class StationImage implements Persistable<Long>, Serializable, Comparable<StationImage> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private LocalDateTime uploadDate;

    private String picturePath;

    private Boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "sensorstation_id")
    @JsonBackReference
    private SensorStation sensorStation;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.imageId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StationImage)) {
            return false;
        }
        final StationImage other = (StationImage) obj;
        if (!Objects.equals(this.imageId, other.imageId)) {
            return false;
        }
        return true;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.StationImage[ id=" + imageId + " ]";
    }

    @Override
    public Long getId() {
        return getImageId();
    }

    public void setId(Long id) {
        setImageId(id);
    }

    @Override
    public boolean isNew() {
        return (null == uploadDate);
    }

	@Override
	public int compareTo(StationImage o) {
		return this.imageId.compareTo(o.getId());
	}

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public SensorStation getSensorStation() {
        return sensorStation;
    }

    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
    }
}
