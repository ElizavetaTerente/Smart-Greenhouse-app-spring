package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.StationImage;
import jakarta.transaction.Transactional;

import java.util.Collection;

@Transactional
public interface StationImageRepository extends AbstractRepository<StationImage, String> {

    Collection<StationImage> findAllBySensorStationAndEnabled(SensorStation sensorStation,boolean enabled);

    void deleteById(Long id);

    StationImage getStationImageById(Long id);

}
