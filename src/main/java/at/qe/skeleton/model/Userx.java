package at.qe.skeleton.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

@Entity
public class Userx implements Persistable<String>, Serializable, Comparable<Userx> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @ManyToMany(mappedBy = "observingUsers", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<SensorStation> observedSensorStations;

    @ManyToMany(mappedBy = "managingUsers", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<SensorStation> managedSensorStations;

    @ManyToOne(/*optional = false*/)
    @JsonBackReference
    private Userx createUser;
//    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @ManyToOne(optional = true)
    @JsonBackReference
    private Userx updateUser;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    boolean enabled;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Userx)) {
            return false;
        }
        final Userx other = (Userx) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int compareTo(Userx o) {
        return this.username.compareTo(o.getUsername());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
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

    public Set<SensorStation> getObservedSensorStations() {
        return observedSensorStations;
    }

    public void setObservedSensorStations(Set<SensorStation> observedSensorStations) {
        this.observedSensorStations = observedSensorStations;
    }

    public Set<SensorStation> getManagedSensorStations() {
        return managedSensorStations;
    }

    public void setManagedSensorStations(Set<SensorStation> managedSensorStations) {
        this.managedSensorStations = managedSensorStations;
    }
}