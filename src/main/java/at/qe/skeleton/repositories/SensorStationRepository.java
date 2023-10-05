package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SensorStationRepository extends AbstractRepository<SensorStation, Long> {

    @Query("SELECT s FROM SensorStation s WHERE s.enabled = true")
    List<SensorStation> findAllActiveSensorstations();

    @Query("SELECT s FROM SensorStation s WHERE s.enabled = true and :user member of s.managingUsers")
    List<SensorStation> findAllActiveSensorstationsForUser(@Param("user") Userx user);

    @Query("SELECT s FROM SensorStation s WHERE s.enabled = true and s.accesspoint is null")
    List<SensorStation> findAllAvailableSensorstations();

    @Query("SELECT s FROM SensorStation s WHERE s.universallyUniqueIdentifier = :uuid")
    SensorStation findbyUuid(@Param("uuid") String uuid);

    @Query("SELECT s FROM SensorStation s WHERE s.accesspoint = :accesspoint")
    Collection<SensorStation> findAllByAccesspoint(@Param("accesspoint") Accesspoint accesspoint);

    SensorStation findFirstById(long id);

    SensorStation findFirstBySensorStationDipId(long dipId);

}
