package at.qe.skeleton.tests;

import at.qe.skeleton.model.AuditEvent;
import at.qe.skeleton.model.SensorLimit;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorType;
import at.qe.skeleton.model.frontEndDto.SensorStationLimitsDto;
import at.qe.skeleton.services.SensorLimitService;
import at.qe.skeleton.services.SensorStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@WebAppConfiguration
class SensorLimitTest {
    private SensorLimit sensorLimit;

    @Autowired
    private SensorLimitService sensorLimitService;

    @Autowired
    private SensorStationService sensorStationService;

    @BeforeEach
    void setUp() {
        sensorLimit = new SensorLimit();
    }

    @Test
    void testId() {
        Long id = 1L;
        sensorLimit.setSensorLimitId(id);
        assertEquals(id, sensorLimit.getSensorLimitId());
        assertEquals(id, sensorLimit.getId());
    }

    @Test
    void testSensorType() {
        SensorType sensorType = SensorType.TEMPERATURE;  // adjust this as necessary
        sensorLimit.setSensorType(sensorType);
        assertEquals(sensorType, sensorLimit.getSensorType());
    }

    @Test
    void testOverrunInterval() {
        Duration overrunInterval = Duration.ofHours(1);  // example duration
        sensorLimit.setOverrunInterval(overrunInterval);
        assertEquals(overrunInterval, sensorLimit.getOverrunInterval());
    }

    @Test
    void testThresholds() {
        double min = 0.0;
        double max = 1.0;
        sensorLimit.setThresholdMin(min);
        sensorLimit.setThresholdMax(max);
        assertEquals(min, sensorLimit.getThresholdMin());
        assertEquals(max, sensorLimit.getThresholdMax());
    }

    @Test
    void testSensorStation() {
        SensorStation sensorStation = new SensorStation();  // adjust this as necessary
        sensorLimit.setSensorStation(sensorStation);
        assertEquals(sensorStation, sensorLimit.getSensorStation());
    }

    @Test
    void testEquals() {
        SensorLimit sensorLimit1 = new SensorLimit();
        SensorLimit sensorLimit2 = new SensorLimit();

        // Reflexive
        assertTrue(sensorLimit1.equals(sensorLimit1));

        // Symmetric
        sensorLimit1.setSensorLimitId(1L);
        sensorLimit2.setSensorLimitId(1L);
        assertTrue(sensorLimit1.equals(sensorLimit2));
        assertTrue(sensorLimit2.equals(sensorLimit1));

        // Transitive
        SensorLimit sensorLimit3 = new SensorLimit();
        sensorLimit3.setSensorLimitId(1L);
        assertTrue(sensorLimit1.equals(sensorLimit3));
        assertTrue(sensorLimit2.equals(sensorLimit3));

        // Not equal
        sensorLimit2.setSensorLimitId(2L);
        assertFalse(sensorLimit1.equals(sensorLimit2));
    }

    @Test
    void testHashCode() {
        sensorLimit.setSensorLimitId(1L);
        int expectedHashCode = 7 * 59 + sensorLimit.getSensorLimitId().hashCode();
        assertEquals(expectedHashCode, sensorLimit.hashCode());
    }

    @Test
    void testToString() {
        sensorLimit.setSensorLimitId(1L);
        String expectedString = "at.qe.skeleton.model.SensorLimit[ id=" + sensorLimit.getSensorLimitId() + " ]";
        assertEquals(expectedString, sensorLimit.toString());
    }

    @Test
    void testCompareTo() {
        SensorLimit sensorLimit1 = new SensorLimit();
        SensorLimit sensorLimit2 = new SensorLimit();

        sensorLimit1.setSensorLimitId(1L);
        sensorLimit2.setSensorLimitId(2L);

        assertTrue(sensorLimit1.compareTo(sensorLimit2) < 0);
        assertTrue(sensorLimit2.compareTo(sensorLimit1) > 0);

        sensorLimit2.setSensorLimitId(1L);
        assertTrue(sensorLimit1.compareTo(sensorLimit2) == 0);
    }

    @Test
    void testIsNew() {
        SensorLimit sensorLimit = new SensorLimit();
        assertTrue(sensorLimit.isNew());
        sensorLimit.setSensorType(SensorType.TEMPERATURE);  // adjust this as necessary
        assertFalse(sensorLimit.isNew());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getAllSensorLimits() {
        List<SensorLimit> allSensorLimits = sensorLimitService.getAllSensorLimits();
        assertEquals(3, allSensorLimits.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void loadSensorLimit() {
        SensorLimit loaded = sensorLimitService.loadSensorLimit(9991L);
        assertEquals(9991, loaded.getId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteSensorLimit() {
        SensorLimit loaded = sensorLimitService.loadSensorLimit(9991L);
        sensorLimitService.deleteSensorLimit(loaded);
        loaded = sensorLimitService.loadSensorLimit(9991L);
        assertEquals(null, loaded);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getSensorLimitsBySensorStation() {
        SensorStation s1 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        assertEquals(6, sensorLimitService.getSensorLimitsBySensorStation(s1).size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getAllLimitsOfSensorStation() {
        SensorStation sensorStation = new SensorStation();
        sensorStation = sensorStationService.saveSensorstation(sensorStation);

        sensorStationService.editLimits(sensorStation, SensorType.TEMPERATURE, 10, 50);
        sensorStationService.editLimits(sensorStation, SensorType.AIR_HUMIDITY, 10, 50);
        sensorStationService.editLimits(sensorStation, SensorType.AIR_QUALITY, 10, 50);
        sensorStationService.editLimits(sensorStation, SensorType.SOIL_HUMIDITY, 10, 50);
        sensorStationService.editLimits(sensorStation, SensorType.LIGHT, 10, 50);
        sensorStationService.editLimits(sensorStation, SensorType.AIR_PRESSURE, 10, 50);

        SensorStationLimitsDto sensorStationLimitsDto = sensorLimitService.getAllLimitsOfSensorStation(sensorStation);

        assertNotNull(sensorStationLimitsDto);
        assertEquals(10, sensorStationLimitsDto.getTemperature().getMin());
        assertEquals(50, sensorStationLimitsDto.getTemperature().getMax());
        assertEquals(10, sensorStationLimitsDto.getAirHumidity().getMin());
        assertEquals(50, sensorStationLimitsDto.getAirHumidity().getMax());
        assertEquals(10, sensorStationLimitsDto.getAirPressure().getMin());
        assertEquals(50, sensorStationLimitsDto.getAirPressure().getMax());
        assertEquals(10, sensorStationLimitsDto.getAirQuality().getMin());
        assertEquals(50, sensorStationLimitsDto.getAirQuality().getMax());
        assertEquals(10, sensorStationLimitsDto.getLight().getMin());
        assertEquals(50, sensorStationLimitsDto.getLight().getMax());
    }

}


