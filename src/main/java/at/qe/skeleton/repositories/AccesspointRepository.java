package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Accesspoint;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccesspointRepository extends AbstractRepository<Accesspoint, Long> {

    @Query("SELECT a FROM Accesspoint a WHERE a.enabled = true")
    List<Accesspoint> findAllActiveAccesspoints();

    Accesspoint findFirstById(Long id);

}
