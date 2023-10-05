package at.qe.skeleton.tests;

import static org.junit.jupiter.api.Assertions.*;

import at.qe.skeleton.model.AuditEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@WebAppConfiguration
class AuditEventTest {
    private AuditEvent auditEvent1;
    private AuditEvent auditEvent2;

    @BeforeEach
    void setUp() {
        auditEvent1 = new AuditEvent();
        auditEvent1.setLogId(1L);
        auditEvent1.setUsername("user1");
        auditEvent1.setTimestamp(LocalDateTime.now());
        auditEvent1.setEventType("type1");
        auditEvent1.setCorrespondingObject("object1");

        auditEvent2 = new AuditEvent();
        auditEvent2.setLogId(2L);
        auditEvent2.setUsername("user2");
        auditEvent2.setTimestamp(LocalDateTime.now());
        auditEvent2.setEventType("type2");
        auditEvent2.setCorrespondingObject("object2");
    }

    @Test
    void testEquals() {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setLogId(1L);
        assertEquals(auditEvent, auditEvent1);
        assertNotEquals(auditEvent, auditEvent2);
    }

    @Test
    void testHashCode() {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setLogId(1L);
        assertEquals(auditEvent.hashCode(), auditEvent1.hashCode());
        assertNotEquals(auditEvent.hashCode(), auditEvent2.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "at.qe.skeleton.model.AuditEvent[ id=1 ]";
        assertEquals(expectedString, auditEvent1.toString());
    }

    @Test
    void testCompareTo() {
        assertTrue(auditEvent1.compareTo(auditEvent2) < 0);
        assertTrue(auditEvent2.compareTo(auditEvent1) > 0);
        assertEquals(0, auditEvent1.compareTo(auditEvent1));
    }

    @Test
    void testIsNew() {
        AuditEvent auditEvent = new AuditEvent();
        assertTrue(auditEvent.isNew());
        auditEvent.setTimestamp(LocalDateTime.now());
        assertFalse(auditEvent.isNew());
    }
}
