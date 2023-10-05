package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Accesspoint implements Persistable<Long>, Serializable, Comparable<Accesspoint> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accesspointId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "accesspoint")
    @JsonManagedReference
    private Set<SensorStation> sensorStations = new HashSet<>();

    private String locationName;

    private boolean enabled;

    private boolean accessible;

    private boolean accepted;

    private boolean searchingSensorStation;

    private Time transmissionInterval;

    @ManyToOne
    @JsonBackReference
    private Userx createUser;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @ManyToOne
    @JsonBackReference
    private Userx updateUser;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.accesspointId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Accesspoint)) {
            return false;
        }
        final Accesspoint other = (Accesspoint) obj;
        return Objects.equals(this.accesspointId, other.accesspointId);
    }

    @Override
    public String toString() {
        return "ID: " + accesspointId + ", Location Name: " + locationName;
    }

    @Override
    public Long getId() {
        return getAccesspointId();
    }

    public void setId(Long id) {
        setAccesspointId(id);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

	@Override
	public int compareTo(Accesspoint o) {
		return this.accesspointId.compareTo(o.getAccesspointId());
	}

    public Set<SensorStation> getSensorStations() {
        return sensorStations;
    }

    public void setSensorStations(Set<SensorStation> sensorStations) {
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

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
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

    public Userx getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Userx createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Userx getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Userx updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }


}
