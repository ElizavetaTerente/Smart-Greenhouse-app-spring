package at.qe.skeleton.tests;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.StationImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StationImageTest {

    private StationImage stationImage;
    private final LocalDateTime date = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        stationImage = new StationImage();
        stationImage.setImageId(1L);
        stationImage.setUploadDate(date);
        stationImage.setPicturePath("path/to/image");
        stationImage.setSensorStation(new SensorStation());
    }

    @Test
    void getId() {
        assertEquals(1L, stationImage.getImageId());
    }

    @Test
    void getUploadDate() {
        assertEquals(date, stationImage.getUploadDate());
    }

    @Test
    void getPicturePath() {
        assertEquals("path/to/image", stationImage.getPicturePath());
    }

    @Test
    void getSensorStation() {
        assertNotNull(stationImage.getSensorStation());
    }

    @Test
    void isNew() {
        assertFalse(stationImage.isNew());
        stationImage.setUploadDate(null);
        assertTrue(stationImage.isNew());
    }

    @Test
    void testEqualsAndHashCode() {
        StationImage stationImage2 = new StationImage();
        stationImage2.setImageId(1L);

        assertEquals(stationImage, stationImage2);
        assertEquals(stationImage.hashCode(), stationImage2.hashCode());

        stationImage2.setImageId(2L);

        assertNotEquals(stationImage, stationImage2);
        assertNotEquals(stationImage.hashCode(), stationImage2.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "at.qe.skeleton.model.StationImage[ id=1 ]";
        assertEquals(expectedString, stationImage.toString());
    }

    @Test
    void compareTo() {
        StationImage stationImage2 = new StationImage();
        stationImage2.setImageId(2L);

        assertTrue(stationImage.compareTo(stationImage2) < 0);
    }
}

