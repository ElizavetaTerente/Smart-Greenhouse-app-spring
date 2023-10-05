package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AuditEvent;

import java.util.List;

public interface AuditEventRepository extends AbstractRepository<AuditEvent, Long> {

    List<AuditEvent> findByEventType(String eventType);

}
