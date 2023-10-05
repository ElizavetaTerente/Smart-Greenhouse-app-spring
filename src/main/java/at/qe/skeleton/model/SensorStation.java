package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class SensorStation implements Persistable<Long>, Serializable, Comparable<SensorStation> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sensorStationId;

    private String universallyUniqueIdentifier;

    private Long sensorStationDipId;

    private String sensorStationName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accesspoint_id")
    @JsonBackReference
    private Accesspoint accesspoint;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="sensorStation")
    @JsonManagedReference
    private Set<StationImage> images = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="sensorStation")
    @JsonManagedReference
    private Set<SensorLimit> sensorLimits = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="sensorStation")
    @JsonManagedReference
    private Set<SensorData> sensorData = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "observed_stations",
            joinColumns = @JoinColumn(name = "sensor_station_id"),
            inverseJoinColumns = @JoinColumn(name = "userx_username"))
    @JsonManagedReference
    private Set<Userx> observingUsers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "managed_stations",
            joinColumns = @JoinColumn(name = "sensor_station_id"),
            inverseJoinColumns = @JoinColumn(name = "userx_username"))
    @JsonManagedReference
    private Set<Userx> managingUsers;

    private boolean enabled;

    private boolean accepted;

    private boolean accessible;

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
        hash = 59 * hash + Objects.hashCode(this.sensorStationId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SensorStation)) {
            return false;
        }
        final SensorStation other = (SensorStation) obj;
        return Objects.equals(this.sensorStationId, other.sensorStationId);
    }


    @Override
    public String toString() {
        return universallyUniqueIdentifier;
    }

    @Override
    public Long getId() {
        return getSensorStationId();
    }

    public void setId(Long id) {
        setSensorStationId(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

	@Override
	public int compareTo(SensorStation o) {
		return this.sensorStationId.compareTo(o.getSensorStationId());
	}

    public String getUniversallyUniqueIdentifier() {
        return universallyUniqueIdentifier;
    }

    public void setUniversallyUniqueIdentifier(String universallyUniqueIdentifier) {
        this.universallyUniqueIdentifier = universallyUniqueIdentifier;
    }

    public Long getSensorStationDipId() {
        return sensorStationDipId;
    }

    public void setSensorStationDipId(Long sensorStationDipId) {
        this.sensorStationDipId = sensorStationDipId;
    }

    public Long getSensorStationId() {
        return sensorStationId;
    }

    public void setSensorStationId(Long sensorStationId) {
        this.sensorStationId = sensorStationId;
    }

    public String getSensorStationName() {
        return sensorStationName;
    }

    public void setSensorStationName(String name) {
        this.sensorStationName = name;
    }

    public Accesspoint getAccesspoint() {
        return accesspoint;
    }

    public void setAccesspoint(Accesspoint accesspoint) {
        this.accesspoint = accesspoint;
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

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
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

    public Set<StationImage> getImages() {
        return images;
    }

    public void setImages(Set<StationImage> images) {
        this.images = images;
    }


    public Set<SensorLimit> getSensorLimits() {
        return sensorLimits;
    }

    public void setSensorLimits(Set<SensorLimit> sensorLimits) {
        this.sensorLimits = sensorLimits;
    }

    public Set<SensorData> getSensorData() {
        return sensorData;
    }

    public void setSensorData(Set<SensorData> sensorData) {
        this.sensorData = sensorData;
    }

    public Set<Userx> getObservingUsers() {
        return observingUsers;
    }

    public void setObservingUsers(Set<Userx> observingUsers) {
        this.observingUsers = observingUsers;
    }

    public Set<Userx> getManagingUsers() {
        return managingUsers;
    }

    public void setManagingUsers(Set<Userx> managingUsers) {
        this.managingUsers = managingUsers;
    }
}
