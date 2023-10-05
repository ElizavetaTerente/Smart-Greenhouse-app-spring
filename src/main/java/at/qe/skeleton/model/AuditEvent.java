package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AuditEvent implements Persistable<Long>, Serializable, Comparable<AuditEvent> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long logId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String correspondingObject;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.logId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AuditEvent)) {
            return false;
        }
        final AuditEvent other = (AuditEvent) obj;
        if (!Objects.equals(this.logId, other.logId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.AuditEvent[ id=" + logId + " ]";
    }

    @Override
    public Long getId() {
        return getLogId();
    }

    @Override
    public boolean isNew() {
        return (null == timestamp);
    }

	@Override
	public int compareTo(AuditEvent o) {
		return logId.compareTo(o.getLogId());
	}

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCorrespondingObject() {
        return correspondingObject;
    }

    public void setCorrespondingObject(String correspondingObject) {
        this.correspondingObject = correspondingObject;
    }
}
