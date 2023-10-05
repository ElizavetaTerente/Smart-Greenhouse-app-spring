package at.qe.skeleton.tests;

import static org.junit.jupiter.api.Assertions.*;

import at.qe.skeleton.api.storage.StorageException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@WebAppConfiguration
public class StorageExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        String message = "Test message";
        StorageException exception = new StorageException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");
        StorageException exception = new StorageException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCausePropagatedProperly() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");
        StorageException exception = new StorageException(message, cause);
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }
}

