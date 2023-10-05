package at.qe.skeleton.services;

import at.qe.skeleton.model.AuditEvent;
import at.qe.skeleton.repositories.AuditEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Scope("application")
public class AuditLogService {

    @Autowired
    private AuditEventRepository auditEventRepository;

    public AuditEvent logEvent(String username, String eventType, String correspondingObject) {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setTimestamp(LocalDateTime.now());
        auditEvent.setUsername(username);
        auditEvent.setEventType(eventType);
        auditEvent.setCorrespondingObject(correspondingObject);
        return auditEventRepository.save(auditEvent);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AuditEvent> findAll() {
        return auditEventRepository.findAll();
    }

    public List<AuditEvent> findByEventType(String eventType) {
        return auditEventRepository.findByEventType(eventType);
    }
}
