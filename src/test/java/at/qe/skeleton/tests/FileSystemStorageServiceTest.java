package at.qe.skeleton.tests;

import at.qe.skeleton.api.storage.FileSystemStorageService;
import at.qe.skeleton.api.storage.StorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@WebAppConfiguration
class FileSystemStorageServiceTest {

    private FileSystemStorageService service;

    @Mock
    private StorageProperties properties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getLocation()).thenReturn("test");
        service = new FileSystemStorageService(properties);

        // Create the test directory if it does not exist
        try {
            Files.createDirectories(Paths.get(properties.getLocation()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create test directory", e);
        }
    }


    @Test
    void store() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", "text/plain", "Hello, World!".getBytes());

        service.store(file);

        Path storageLocation = Paths.get(properties.getLocation());
        Path expectedFilePath = storageLocation.resolve(file.getOriginalFilename());

        assertTrue(Files.exists(expectedFilePath));

        // Cleanup
        Files.deleteIfExists(expectedFilePath);
    }

    @Test
    void loadAll() throws IOException {
        // Initialize with a file
        Files.createFile(Paths.get(properties.getLocation(), "test.txt"));

        try (Stream<Path> allFiles = service.loadAll()) {
            assertNotNull(allFiles);
            assertTrue(allFiles.count() > 0);
        }

        // Cleanup
        FileSystemUtils.deleteRecursively(Paths.get(properties.getLocation()).toFile());
    }

    @Test
    void load() {
        String filename = "hello.txt";
        Path expectedFilePath = Paths.get(properties.getLocation()).resolve(filename);
        assertEquals(expectedFilePath, service.load(filename));
    }

    @Test
    void loadAsResource() throws IOException {
        String filename = "test.txt";
        Files.createFile(Paths.get(properties.getLocation(), filename));

        Resource resource = service.loadAsResource(filename);
        assertNotNull(resource);
        assertEquals(filename, resource.getFilename());

        // Cleanup
        FileSystemUtils.deleteRecursively(Paths.get(properties.getLocation()).toFile());
    }

    @Test
    void deleteAll() throws IOException {
        // Initialize with a file
        Files.createFile(Paths.get(properties.getLocation(), "test.txt"));
        assertTrue(Files.exists(Paths.get(properties.getLocation(), "test.txt")));

        service.deleteAll();
        assertFalse(Files.exists(Paths.get(properties.getLocation(), "test.txt")));
    }

    @Test
    void init() throws IOException {
        // Delete directory if exists
        FileSystemUtils.deleteRecursively(Paths.get(properties.getLocation()).toFile());
        assertFalse(Files.exists(Paths.get(properties.getLocation())));

        service.init();
        assertTrue(Files.exists(Paths.get(properties.getLocation())));
    }
}

