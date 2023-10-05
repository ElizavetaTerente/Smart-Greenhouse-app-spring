package at.qe.skeleton.tests;

import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.AccesspointService;
import at.qe.skeleton.services.SensorStationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@WebAppConfiguration
public class AccesspointServiceTest {

    @Autowired
    AccesspointService accesspointService;

    @Autowired
    private SensorStationService sensorStationService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateAccesspoint() {
        Accesspoint accesspoint = new Accesspoint();
        accesspoint.setLocationName("Test Location");
        accesspoint = accesspointService.createAccesspoint(accesspoint);
        Accesspoint savedAccesspoint = accesspointService.loadAccesspoint(accesspoint.getAccesspointId());
        Assertions.assertNotNull(savedAccesspoint, "Accesspoint \"" + accesspoint.getId() + "\" could not be loaded from test data source");
        Assertions.assertTrue(savedAccesspoint.isEnabled());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testSaveAccesspoint() {
        Accesspoint accesspoint = new Accesspoint();
        accesspoint.setLocationName("Test Location");
        accesspoint.setEnabled(false);
        accesspoint.setAccessible(false);
        accesspoint.setAccepted(false);
        accesspoint.setSearchingSensorStation(false);
        Accesspoint savedAccesspoint = accesspointService.saveAccesspoint(accesspoint);
        Assertions.assertNotNull(savedAccesspoint, "Accesspoint \"" + accesspoint.getId() + "\" could not be saved");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteAccesspoint() {
        Accesspoint accesspoint = new Accesspoint();
        accesspoint.setLocationName("Test Location");
        accesspoint = accesspointService.createAccesspoint(accesspoint);
        Accesspoint testAccesspoint = accesspointService.loadAccesspoint(accesspoint.getId());
        Assertions.assertNotNull(testAccesspoint, "Accesspoint \"" + accesspoint.getId() + "\" could not be loaded from test data source");
        Assertions.assertTrue(testAccesspoint.isEnabled());
        accesspointService.deleteAccesspoint(testAccesspoint);
        Assertions.assertFalse(testAccesspoint.isEnabled(), "Accesspoint \"" + testAccesspoint.getId() + "\" was deleted from database, but should only be set to 'enabled' = false");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAcceptAccesspoint() {
        Accesspoint accesspoint = new Accesspoint();
        accesspoint.setLocationName("Test Location");
        accesspoint = accesspointService.createAccesspoint(accesspoint);
        Accesspoint testAccesspoint = accesspointService.loadAccesspoint(accesspoint.getId());
        Assertions.assertNotNull(testAccesspoint, "Accesspoint \"" + accesspoint.getId() + "\" could not be loaded from test data source");
        Assertions.assertFalse(testAccesspoint.isAccepted());
        accesspointService.accept(testAccesspoint);
        Assertions.assertTrue(testAccesspoint.isAccepted(), "Accesspoint \"" + testAccesspoint.getId() + "\" should be set to 'accepted' = true");
    }

/*    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void getAllAccesspoints() {
        Accesspoint a1 = new Accesspoint();
        accesspointService.saveAccesspoint(a1);
        List<Accesspoint> allAccesspoints = accesspointService.getAllAccesspoints();
        Assertions.assertEquals(3, allAccesspoints.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void getAllActiveAccesspoints() {
        List<Accesspoint> allAccesspoints = accesspointService.getAllActiveAccesspoints();
        Assertions.assertEquals(3, allAccesspoints.size());
    }*/

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void stopSearchSensorStation() {
        Accesspoint a1 = new Accesspoint();
        a1.setSearchingSensorStation(true);
        a1 = accesspointService.createAccesspoint(a1);
        accesspointService.stopSearchSensorStation(a1.getId());
        Assertions.assertFalse(a1.isSearchingSensorStation());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void addSensorStation() {
        Accesspoint a1 = new Accesspoint();
        a1 = accesspointService.createAccesspoint(a1);
        SensorStation s1 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        accesspointService.addSensorStation(s1.getId(), a1.getId());
        Assertions.assertEquals(s1.getAccesspoint().getId(), a1.getId());
    }


}