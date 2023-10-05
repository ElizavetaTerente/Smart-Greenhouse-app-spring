package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SensorLimit;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorType;

import java.util.List;

public interface SensorLimitRepository extends AbstractRepository<SensorLimit, String> {

    SensorLimit findFirstById(Long id);

    List<SensorLimit> findBySensorStation(SensorStation sensorStation);

    SensorLimit findFirstBySensorStationAndSensorType(SensorStation sensorStation, SensorType sensorType);

}
