package at.qe.skeleton.tests;
import at.qe.skeleton.model.SensorData;
import at.qe.skeleton.model.SensorType;
import at.qe.skeleton.services.SensorDataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@WebAppConfiguration
public class SensorDataServiceTest {

    @Autowired
    private SensorDataService sensorDataService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testLoadSensorData() {
        SensorData sensorData = new SensorData();
        sensorData.setSensorType(SensorType.TEMPERATURE);
        sensorData.setDataTime(LocalDateTime.now());
        sensorData.setDataValue(25.5);

        sensorData = sensorDataService.saveSensorData(sensorData);
        SensorData loadedSensorData = sensorDataService.loadSensorData(sensorData.getId());

        Assertions.assertNotNull(loadedSensorData, "SensorData was not loaded");
        Assertions.assertEquals(sensorData.getId(), loadedSensorData.getId(), "Loaded SensorData ID does not match");
        Assertions.assertEquals(sensorData.getDataValue(), loadedSensorData.getDataValue(), "Loaded SensorData Data value does not match");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testSaveSensorData() {
        SensorData sensorData = new SensorData();
        sensorData.setSensorType(SensorType.TEMPERATURE);
        sensorData.setDataTime(LocalDateTime.now());
        sensorData.setDataValue(25.5);

        sensorData = sensorDataService.saveSensorData(sensorData);

        Assertions.assertNotNull(sensorData, "SensorData was not saved");
        Assertions.assertEquals(SensorType.TEMPERATURE, sensorData.getSensorType(), "Saved SensorData type does not match");
        Assertions.assertEquals(25.5, sensorData.getDataValue(),0.001, "Saved SensorData value does not match");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testSaveSensorDataList() {
        SensorData sensorData1 = new SensorData();
        sensorData1.setSensorType(SensorType.TEMPERATURE);
        sensorData1.setDataTime(LocalDateTime.now());
        sensorData1.setDataValue(25.5);

        SensorData sensorData2 = new SensorData();
        sensorData2.setSensorType(SensorType.AIR_HUMIDITY);
        sensorData2.setDataTime(LocalDateTime.now());
        sensorData2.setDataValue(60.0);

        List<SensorData> sensorDataList = Arrays.asList(sensorData1, sensorData2);
        sensorDataList = sensorDataService.saveSensorData(sensorDataList);

        Assertions.assertNotNull(sensorDataList, "SensorData list was not saved");
        Assertions.assertEquals(2, sensorDataList.size(), "Saved SensorData list size does not match");

        Assertions.assertEquals(SensorType.TEMPERATURE, sensorData1.getSensorType(), "Saved SensorData type does not match");
        Assertions.assertEquals(25.5, sensorData1.getDataValue(), 0.001, "Saved SensorData value does not match");

        Assertions.assertEquals(SensorType.AIR_HUMIDITY, sensorData2.getSensorType(), "Saved SensorData type does not match");
        Assertions.assertEquals(60.0, sensorData2.getDataValue(), 0.001, "Saved SensorData value does not match");
    }
}