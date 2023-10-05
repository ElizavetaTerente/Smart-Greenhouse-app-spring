package at.qe.skeleton.tests;

import at.qe.skeleton.model.SensorLimit;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorType;
import at.qe.skeleton.model.StationImage;
import at.qe.skeleton.model.frontEndDto.SensorStationLimitsDto;
import at.qe.skeleton.services.SensorLimitService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.services.StationImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@WebAppConfiguration
class StationImageServiceTest {
    private SensorLimit sensorLimit;

    @Autowired
    private SensorLimitService sensorLimitService;

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private StationImageService stationImageService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void uploadPic() {
        SensorStation s1 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        StationImage stationImage = new StationImage();
        stationImage.setSensorStation(s1);
        stationImageService.uploadPic(s1.getId(), "picName");
        Collection<StationImage> stationImages = stationImageService.loadAllActivePicsOfSensorStation(s1.getId());
        Assertions.assertEquals(1, stationImages.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void loadById() {
        SensorStation s1 = new SensorStation();
        s1 = sensorStationService.createSensorstation(s1);
        StationImage stationImage = new StationImage();
        stationImage.setSensorStation(s1);
        stationImageService.uploadPic(s1.getId(), "picName");
        Collection<StationImage> stationImages = stationImageService.loadAllActivePicsOfSensorStation(s1.getId());
        StationImage si = stationImages.iterator().next();
        StationImage loaded = stationImageService.loadById(si.getId());
        Assertions.assertEquals(si.getId(), loaded.getId());
        
    }

}


