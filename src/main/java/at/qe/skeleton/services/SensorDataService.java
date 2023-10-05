package at.qe.skeleton.services;

import at.qe.skeleton.model.SensorData;
import at.qe.skeleton.repositories.SensorDataRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@Scope("application")
public class SensorDataService implements Serializable {
    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private UserxRepository userxRepository;
    @Autowired
    private SensorStationRepository sensorStationRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorData loadSensorData(Long id) {
        return sensorDataRepository.findFirstById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorData saveSensorData(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SensorData> saveSensorData(List<SensorData> sensorDataList) {
        return (List<SensorData>) sensorDataRepository.saveAll(sensorDataList);
    }

}
