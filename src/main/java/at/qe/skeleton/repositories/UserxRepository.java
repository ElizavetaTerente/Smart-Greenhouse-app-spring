package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link Userx} entities.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
public interface UserxRepository extends AbstractRepository<Userx, String> {


    Userx findFirstByUsername(String username);

    List<Userx> findByUsernameContaining(String username);

    @Query("SELECT u FROM Userx u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<Userx> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM Userx u WHERE :role MEMBER OF u.roles")
    List<Userx> findByRole(@Param("role") UserRole role);

    @Query("SELECT u FROM Userx u WHERE u.enabled = true")
    List<Userx> findAllActiveUsers();

    @Query("SELECT u FROM Userx u WHERE u.enabled = true AND :gardenerRole MEMBER OF u.roles")
    List<Userx> findAllActiveGardeners(@Param("gardenerRole") UserRole gardenerRole);

}
