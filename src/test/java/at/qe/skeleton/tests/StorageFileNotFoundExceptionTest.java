package at.qe.skeleton.tests;

import static org.junit.jupiter.api.Assertions.*;

import at.qe.skeleton.api.storage.StorageFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@WebAppConfiguration
public class StorageFileNotFoundExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        String message = "Test message";
        StorageFileNotFoundException exception = new StorageFileNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");
        StorageFileNotFoundException exception = new StorageFileNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCausePropagatedProperly() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");
        StorageFileNotFoundException exception = new StorageFileNotFoundException(message, cause);

        assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }
}

