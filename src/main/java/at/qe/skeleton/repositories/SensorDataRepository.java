package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SensorData;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface SensorDataRepository extends AbstractRepository<SensorData, String> {
    SensorData findFirstById(Long id);

    Collection<SensorData> findAllBySensorStation(SensorStation sensorStation);

    SensorData findFirstBySensorStationAndSensorTypeOrderByDataTimeDesc(SensorStation sensorStation, SensorType sensorType);

/*
    @Query("select sd from SensorData sd where sd.sensorStation = :sensorStation AND sd.dataTime >= :startDataTime AND sd.dataTime <= :endDataTime")
    Collection<SensorData> findAllSensorDataInGivenRange(@Param("sensorStation") SensorStation sensorStation, @Param("startDataTime") LocalDateTime startDataTime, @Param("endDataTime") LocalDateTime endDataTime);

    Collection<SensorData> findByDataTimeBetween(LocalDateTime from, LocalDateTime to);

 */
}
