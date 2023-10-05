package at.qe.skeleton.tests;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.frontEndDto.CurrentStationValuesDto;
import at.qe.skeleton.model.frontEndDto.SensorDataOfAllSensorsInTimeRangeDto;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.services.AccesspointService;
import at.qe.skeleton.services.SensorDataService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@WebAppConfiguration
public class SensorStationServiceTest {

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private AccesspointService accesspointService;

    @Autowired
    private UserService userService;

    @Autowired
    private SensorDataService sensorDataService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateSensorStation() {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("Test Station");
        sensorStation = sensorStationService.createSensorstation(sensorStation);
        SensorStation savedSensorStation = sensorStationService.loadSensorstation(sensorStation.getId());
        Assertions.assertNotNull(savedSensorStation, "SensorStation could not be saved");
        assertEquals("Test Station", savedSensorStation.getSensorStationName());
        Assertions.assertTrue(savedSensorStation.isEnabled());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteSensorStation() {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("Active Station");
        sensorStation.setCreateDate(LocalDateTime.now());
        sensorStation.setEnabled(true);

        SensorStation savedSensorStation = sensorStationService.saveSensorstation(sensorStation);

        sensorStationService.deleteSensorstation(savedSensorStation);
        SensorStation disabledSensorStation = sensorStationService.loadSensorstation(savedSensorStation.getId());

        Assertions.assertNotNull(disabledSensorStation, "SensorStation could not be loaded");
        Assertions.assertFalse(disabledSensorStation.isEnabled(), "SensorStation was not disabled");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateSensorStation() {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("Old Name");
        sensorStation.setEnabled(true);
        sensorStation.setCreateDate(LocalDateTime.now());

        SensorStation savedSensorStation = sensorStationService.saveSensorstation(sensorStation);

        savedSensorStation.setSensorStationName("New Name");
        savedSensorStation.setEnabled(false);

        SensorStation updatedSensorStation = sensorStationService.saveSensorstation(savedSensorStation);

        Assertions.assertNotNull(updatedSensorStation, "SensorStation could not be updated");
        assertEquals("New Name", updatedSensorStation.getSensorStationName());
        Assertions.assertFalse(updatedSensorStation.isEnabled());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAddAccesspointToSensorStation() {
        Accesspoint accesspoint = new Accesspoint();
        accesspoint.setLocationName("Test Accesspoint");
        accesspoint = accesspointService.createAccesspoint(accesspoint);

        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("Station without Accesspoint");
        sensorStation = sensorStationService.createSensorstation(sensorStation);

        sensorStationService.addAccesspoint(sensorStation.getId(), accesspoint);

        SensorStation updatedSensorStation = sensorStationService.loadSensorstation(sensorStation.getId());
        Assertions.assertNotNull(updatedSensorStation.getAccesspoint(), "Accesspoint was not added to the SensorStation");
        assertEquals("Test Accesspoint", updatedSensorStation.getAccesspoint().getLocationName());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testRemoveAccesspointFromSensorStation() {
        Accesspoint accesspoint = new Accesspoint();
        accesspoint.setLocationName("Test Accesspoint");
        accesspoint = accesspointService.createAccesspoint(accesspoint);

        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("Station with Accesspoint");
        sensorStation.setAccesspoint(accesspoint);
        sensorStation = sensorStationService.createSensorstation(sensorStation);

        sensorStationService.removeFromAccesspoint(sensorStation.getId());

        SensorStation updatedSensorStation = sensorStationService.loadSensorstation(sensorStation.getId());
        Assertions.assertNull(updatedSensorStation.getAccesspoint(), "Accesspoint was not removed from the SensorStation");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAddGardenerToSensorStation() {
        Userx gardener = new Userx();
        gardener.setUsername("testGardener");
        gardener.setPassword("password");
        gardener = userService.createUser(gardener);

        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("Station without Gardener");
        sensorStation.setCreateDate(LocalDateTime.now());
        sensorStation.setEnabled(true);

        SensorStation savedSensorStation = sensorStationService.saveSensorstation(sensorStation);

        sensorStationService.addGardenerToSensorStation(savedSensorStation.getId(), gardener.getUsername());

        SensorStation updatedSensorStation = sensorStationService.loadSensorstation(savedSensorStation.getId());
        Assertions.assertTrue(updatedSensorStation.getManagingUsers().contains(gardener), "Gardener was not added to the SensorStation");
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getAllSensorstations() {
        sensorStationService.createSensorstation(new SensorStation());
        sensorStationService.createSensorstation(new SensorStation());
        sensorStationService.createSensorstation(new SensorStation());
        List<SensorStation> allSensorstations = sensorStationService.getAllSensorstations();
        assertEquals(3, allSensorstations.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getAllActiveSensorstations() {
        SensorStation s1 = new SensorStation();
        SensorStation s2 = new SensorStation();
        SensorStation s3 = new SensorStation();
        sensorStationService.createSensorstation(s1);
        sensorStationService.createSensorstation(s2);
        s3 = sensorStationService.createSensorstation(s3);
        sensorStationService.deleteSensorstation(s3);
        List<SensorStation> allSensorstations = sensorStationService.getAllActiveSensorstations();
        assertEquals(2, allSensorstations.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getAllAvailableSensorstations() {
        SensorStation s1 = new SensorStation();
        SensorStation s2 = new SensorStation();
        SensorStation s3 = new SensorStation();
        sensorStationService.createSensorstation(s1);
        sensorStationService.createSensorstation(s2);
        sensorStationService.createSensorstation(s3);
        List<SensorStation> allSensorstations = sensorStationService.getAllAvailableSensorstations();
        assertEquals(3, allSensorstations.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void acceptSensorStation() {
        SensorStation s1 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        s1.setAccepted(false);
        sensorStationService.acceptSensorStation(s1.getId());
        assertTrue(s1.isAccepted());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getAllSensorstationsOfAccesspoint() {
        SensorStation s1 = new SensorStation();
        SensorStation s2 = new SensorStation();
        SensorStation s3 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        s2 = sensorStationService.createSensorstation(s2);
        s3 = sensorStationService.createSensorstation(s3);
        Accesspoint a1 = new Accesspoint();
        a1 = accesspointService.createAccesspoint(a1);
        Set<SensorStation> sensorStationSet = new HashSet<>(List.of(s1, s2, s3));
        a1.setSensorStations(sensorStationSet);
        a1 = accesspointService.saveAccesspoint(a1);
        s1.setAccesspoint(a1);
        s2.setAccesspoint(a1);
        s3.setAccesspoint(a1);
        s1 = sensorStationService.saveSensorstation(s1);
        s2 = sensorStationService.saveSensorstation(s2);
        s3 = sensorStationService.saveSensorstation(s3);
        Collection<SensorStation> allSensorstationsOfAccesspoint = sensorStationService.getAllSensorstationsOfAccesspoint(a1);
        assertEquals(3, allSensorstationsOfAccesspoint.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteGardenerFromSensorStation() {
        SensorStation s1 = new SensorStation();
        Userx user1 = new Userx();
        Userx user2 = new Userx();
        Userx user3 = new Userx();
        Set<UserRole> gardenerRole = new HashSet<>(List.of(UserRole.GARDENER));
        user1.setUsername("user1");
        user1.setPassword("user1pwd");
        user1.setRoles(gardenerRole);
        user2.setUsername("user2");
        user2.setPassword("user2pwd");
        user2.setRoles(gardenerRole);
        user3.setUsername("user3");
        user3.setPassword("user3pwd");
        user3.setRoles(gardenerRole);
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        Set<Userx> gardeners = new HashSet<>(List.of(user1, user2, user3));
        s1.setManagingUsers(gardeners);
        s1 = sensorStationService.createSensorstation(s1);
        assertEquals(3, sensorStationService.loadSensorstation(s1.getId()).getManagingUsers().size());
        sensorStationService.deleteGardenerFromSensorStation(s1.getId(), "user1");
        assertEquals(2, sensorStationService.loadSensorstation(s1.getId()).getManagingUsers().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void changeHouseData() {
        SensorStation s1 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        sensorStationService.changeHouseData(s1.getId(), "myUUID", "myName");
        assertEquals(s1.getUniversallyUniqueIdentifier(), "myUUID");
        assertEquals(s1.getSensorStationName(), "myName");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void getSensorDataInTimeRange() {
        // setup data
        LocalDateTime startDate = LocalDateTime.now().minusDays(5);
        LocalDateTime endDate = LocalDateTime.now();

        SensorStation station = new SensorStation();
        station = sensorStationService.createSensorstation(station);

        SensorData data1 = new SensorData();
        data1.setSensorStation(station);
        data1.setSensorType(SensorType.SOIL_HUMIDITY);
        data1.setDataTime(LocalDateTime.now().minusDays(3));

        SensorData data2 = new SensorData();
        data2.setSensorStation(station);
        data2.setSensorType(SensorType.AIR_QUALITY);
        data2.setDataTime(LocalDateTime.now().minusDays(1));

        data1 = sensorDataService.saveSensorData(data1);
        data2 = sensorDataService.saveSensorData(data2);

        // Call the method under test
        SensorDataOfAllSensorsInTimeRangeDto dto = sensorStationService.getSensorDataInTimeRange(station, startDate, endDate);

        // Assert the results
        assertEquals(1, dto.getSoilHumidity().size());
        assertEquals(data1, dto.getSoilHumidity().get(0));
        assertEquals(1, dto.getAirQuality().size());
        assertEquals(data2, dto.getAirQuality().get(0));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void editLimits() {
        SensorStation s1 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        sensorStationService.editLimits(s1, SensorType.TEMPERATURE, 4, 5);
        Set<SensorLimit> sensorLimits = s1.getSensorLimits();
        SensorLimit temperature = null;
        for (SensorLimit sensorLimit : sensorLimits) {
            if (sensorLimit.getSensorType().equals(SensorType.TEMPERATURE)) {
                temperature = sensorLimit;
            }
        }
        assertEquals(temperature.getThresholdMin(), 4);
        assertEquals(temperature.getThresholdMax(), 5);
    }
}