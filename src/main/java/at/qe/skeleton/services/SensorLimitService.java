package at.qe.skeleton.services;

import at.qe.skeleton.model.SensorLimit;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorType;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.frontEndDto.Limit;
import at.qe.skeleton.model.frontEndDto.SensorStationLimitsDto;
import at.qe.skeleton.repositories.SensorLimitRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Scope("application")
public class SensorLimitService implements Serializable {

    @Autowired
    private SensorLimitRepository sensorLimitRepository;

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SensorLimit> getAllSensorLimits() {
        return sensorLimitRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorLimit loadSensorLimit(Long id) {
        return sensorLimitRepository.findFirstById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GARDENER')")
    public SensorLimit saveSensorLimit(SensorLimit sensorLimit) {
        return sensorLimitRepository.save(sensorLimit);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSensorLimit(SensorLimit sensorLimit) {
        sensorLimitRepository.delete(sensorLimit);
    }

    public List<SensorLimit> getSensorLimitsBySensorStation(SensorStation sensorStation) {
        return sensorLimitRepository.findBySensorStation(sensorStation);
    }

    public Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userxRepository.findFirstByUsername(auth.getName());
    }

    public Set<SensorLimit> createSensorLimitsForSensorStation(SensorStation sensorStation) {
        Set<SensorLimit> sensorLimits = new HashSet<>();

        for (SensorType sensorType : SensorType.values()) {
            SensorLimit sensorLimit = new SensorLimit();
            sensorLimit.setSensorType(sensorType);
            sensorLimit.setSensorStation(sensorStation);
            // fill in duration in setOverrunInterval
            sensorLimit.setOverrunInterval(Duration.ofMinutes(10));
            sensorLimits.add(saveSensorLimit(sensorLimit));
        }

        return sensorLimits;
    }

    public SensorStationLimitsDto getAllLimitsOfSensorStation(SensorStation sensorStation){
        SensorStationLimitsDto sensorStationLimitsDto = new SensorStationLimitsDto();

        SensorLimit limitSoilHumidity = sensorLimitRepository.findFirstBySensorStationAndSensorType(sensorStation,SensorType.SOIL_HUMIDITY);
        sensorStationLimitsDto.setSoilHumidity(new Limit(limitSoilHumidity.getThresholdMin(),limitSoilHumidity.getThresholdMax()));

        SensorLimit limitTemperature = sensorLimitRepository.findFirstBySensorStationAndSensorType(sensorStation,SensorType.TEMPERATURE);
        sensorStationLimitsDto.setTemperature(new Limit(limitTemperature.getThresholdMin(),limitTemperature.getThresholdMax()));

        SensorLimit limitAirHumidity = sensorLimitRepository.findFirstBySensorStationAndSensorType(sensorStation,SensorType.AIR_HUMIDITY);
        sensorStationLimitsDto.setAirHumidity(new Limit(limitAirHumidity.getThresholdMin(),limitAirHumidity.getThresholdMax()));

        SensorLimit limitAirPressure = sensorLimitRepository.findFirstBySensorStationAndSensorType(sensorStation,SensorType.AIR_PRESSURE);
        sensorStationLimitsDto.setAirPressure(new Limit(limitAirPressure.getThresholdMin(),limitAirPressure.getThresholdMax()));

        SensorLimit limitAirQuality = sensorLimitRepository.findFirstBySensorStationAndSensorType(sensorStation,SensorType.AIR_QUALITY);
        sensorStationLimitsDto.setAirQuality(new Limit(limitAirQuality.getThresholdMin(),limitAirQuality.getThresholdMax()));

        SensorLimit limitLight = sensorLimitRepository.findFirstBySensorStationAndSensorType(sensorStation,SensorType.LIGHT);
        sensorStationLimitsDto.setLight(new Limit(limitLight.getThresholdMin(),limitLight.getThresholdMax()));

        return sensorStationLimitsDto;
    }
}
