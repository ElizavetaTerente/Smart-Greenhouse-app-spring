package at.qe.skeleton.services;

import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.AccesspointRepository;
import at.qe.skeleton.repositories.UserxRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Scope("application")
public class AccesspointService implements Serializable {


    @Autowired
    private AccesspointRepository accesspointRepository;

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns a collection of all Accesspoints.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Accesspoint> getAllAccesspoints() {
        return accesspointRepository.findAll();
    }

    /**
     * Returns a collection of all active Accesspoints.
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','GARDENER')")
    public List<Accesspoint> getAllActiveAccesspoints() {
        return accesspointRepository.findAllActiveAccesspoints();
    }

    /**
     * Loads a single Accesspoint identified by its id.
     *
     * @param id the id to search for
     * @return the Accesspoint with the given id
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','GARDENER')")
    public Accesspoint loadAccesspoint(Long id) {
        return accesspointRepository.findFirstById(id);
    }

    /**
     * Saves the Accesspoint. This method will also set {@link Accesspoint#getCreateDate} for new
     * entities or {@link Accesspoint#getUpdateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Accesspoint#getCreateDate}
     * or {@link Accesspoint#getUpdateUser} respectively.
     *
     * @param accesspoint the Accesspoint to save
     * @return the updated Accesspoint
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','GARDENER')")
    public Accesspoint saveAccesspoint(Accesspoint accesspoint) {
        if (accesspoint.isNew()) {
            accesspoint.setCreateDate(LocalDateTime.now());
            accesspoint.setCreateUser(getAuthenticatedUser());
            accesspoint.setTransmissionInterval(Time.valueOf("00:05:00"));
            accesspoint.setEnabled(true);
        } else {
            accesspoint.setUpdateDate(LocalDateTime.now());
            accesspoint.setUpdateUser(getAuthenticatedUser());
        }
        return accesspointRepository.save(accesspoint);
    }

    /**
     * Deletes the Accesspoint (not from database, just sets enabled to false).
     *
     * @param accesspoint the Accesspoint to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAccesspoint(Accesspoint accesspoint) {
        accesspoint.setEnabled(false);
        saveAccesspoint(accesspoint);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Accesspoint createAccesspoint(Accesspoint accesspoint) {
        if (accesspoint != null) {
            accesspoint = saveAccesspoint(accesspoint);
        }
        return accesspoint;
    }

    private Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userxRepository.findFirstByUsername(auth.getName());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void accept(Accesspoint accesspoint) {
        accesspoint.setAccepted(true);
        accesspointRepository.save(accesspoint);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<SensorStation> loadSensorStations() {
        return sensorStationService.getAllAvailableSensorstations();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<SensorStation> searchSensorStations(Accesspoint accesspoint) {
        accesspoint.setSearchingSensorStation(true);
        accesspointRepository.save(accesspoint);
        return sensorStationService.getAllAvailableSensorstations();
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public void addSensorStation(Long sensorStationId,Long accesspointId){
        Accesspoint accesspoint = loadAccesspoint(accesspointId);
        sensorStationService.addAccesspoint(sensorStationId,accesspoint);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void stopSearchSensorStation(Long accesspointId) {
        Accesspoint accesspoint = loadAccesspoint(accesspointId);
        accesspoint.setSearchingSensorStation(false);
        accesspointRepository.save(accesspoint);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<SensorStation> searchSensorStations(Long accesspointId) {
        Accesspoint accesspoint = loadAccesspoint(accesspointId);
        //entityManager.refresh(accesspoint);  // refresh the entity to get the latest state
        accesspoint.setSearchingSensorStation(true);
        accesspoint = saveAccesspoint(accesspoint);
        // entityManager.refresh(accesspoint);  // refresh again after saving
        // Introduce a wait with a maximum duration of 5 minutes or until searchingSensorStation is set to false
        long startTime = System.currentTimeMillis();
        long maxWaitTime = TimeUnit.MINUTES.toMillis(5);
        while (((System.currentTimeMillis() - startTime) < 10000) && accesspoint.isSearchingSensorStation()) {
            try {
                TimeUnit.SECONDS.sleep(1);
                accesspoint = loadAccesspoint(accesspointId);
                //entityManager.refresh(accesspoint);  // refresh in each iteration
            } catch (InterruptedException e) {
                // Handle the InterruptedException
                Thread.currentThread().interrupt();
                // Log the exception and/or take any necessary actions
            }
        }
        accesspoint.setSearchingSensorStation(false);
        saveAccesspoint(accesspoint);
        return sensorStationService.getAllAvailableSensorstations();
    }


}
