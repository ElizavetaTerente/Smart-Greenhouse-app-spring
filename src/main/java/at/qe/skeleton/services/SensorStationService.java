package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.model.frontEndDto.CurrentStationValuesDto;
import at.qe.skeleton.model.frontEndDto.SensorDataOfAllSensorsInTimeRangeDto;
import at.qe.skeleton.repositories.SensorDataRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope("application")
public class SensorStationService implements Serializable {

    @Autowired
    private SensorStationRepository sensorStationRepository;

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private SensorLimitService sensorLimitService;

    @Autowired
    private UserService userService;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    /**
     * Returns a collection of all Sensorstations.
     *
     * @return List<SensorStation>
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SensorStation> getAllSensorstations() {
        return sensorStationRepository.findAll();
    }

    /**
     * Returns a List of all Sensorstations of a given Gardener.
     *
     * @return List<SensorStation>
     */
    public List<SensorStation> getAllActiveSensorstationsForLoggedInUser() {
        if (!userService.loggedInUserIsGardener(getAuthenticatedUser())) {
            return Collections.emptyList();
        }
        return sensorStationRepository.findAllActiveSensorstationsForUser(getAuthenticatedUser());
    }

    /**
     * Returns a collection of all active Sensorstations.
     *
     * @return List<SensorStation>
     */
    public List<SensorStation> getAllActiveSensorstations() {
        return sensorStationRepository.findAllActiveSensorstations();
    }

    /**
     * Returns a List of all available Sensorstations (where no accesspoint is assigned).
     *
     * @return List<SensorStation>
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SensorStation> getAllAvailableSensorstations() {
        return sensorStationRepository.findAllAvailableSensorstations();
    }

    /**
     * Loads a single Sensorstation identified by its id.
     *
     * @param id the universally unique identifier to search for
     * @return the Sensorstation with the given uuid
     */
    public SensorStation loadSensorstation(Long id) {
        return sensorStationRepository.findFirstById(id);
    }

    /**
     * Saves the Sensorstation. This method will also set {SensorStation#createDate} for new
     * entities or {SensorStation#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {SensorStation#createDate}
     * or {SensorStation#updateUser} respectively.
     *
     * @param sensorStation the sensorstation to save
     * @return the updated sensorstation
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorStation saveSensorstation(SensorStation sensorStation) {
        if (sensorStation.isNew()) {
            sensorStation.setCreateDate(LocalDateTime.now());
            sensorStation.setCreateUser(getAuthenticatedUser());
            sensorStation.setEnabled(true);
            sensorStation = sensorStationRepository.save(sensorStation);
            Set<SensorLimit> sensorLimits = sensorLimitService.createSensorLimitsForSensorStation(sensorStation);
            sensorStation.setSensorLimits(sensorLimits);
        } else {
            sensorStation.setUpdateDate(LocalDateTime.now());
            sensorStation.setUpdateUser(getAuthenticatedUser());
        }
        return sensorStationRepository.save(sensorStation);
    }

    /**
     * Deletes the Sensorstation.
     *
     * @param sensorStation the sensorstation to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSensorstation(SensorStation sensorStation) {
        sensorStation.setEnabled(false);
        saveSensorstation(sensorStation);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorStation createSensorstation(SensorStation sensorStation) {
        if (sensorStation != null) {
            sensorStation = saveSensorstation(sensorStation);
        }
        return sensorStation;
    }

    private Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userxRepository.findFirstByUsername(auth.getName());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void acceptSensorStation(Long id){
        SensorStation sensorStation = loadSensorstation(id);
        sensorStation.setAccepted(true);
        saveSensorstation(sensorStation);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void addAccesspoint(Long sensorStationId,Accesspoint accesspoint){
        SensorStation sensorStation = loadSensorstation(sensorStationId);
        sensorStation.setAccesspoint(accesspoint);
        sensorStation.setAccessible(true);
        saveSensorstation(sensorStation);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GARDENER')")
    public Collection<SensorStation> getAllSensorstationsOfAccesspoint(Accesspoint accesspoint){
        return sensorStationRepository.findAllByAccesspoint(accesspoint);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void removeFromAccesspoint(Long sensorStationId){
        SensorStation sensorStation = loadSensorstation(sensorStationId);
        sensorStation.setAccesspoint(null);
        saveSensorstation(sensorStation);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void addGardenerToSensorStation(Long id,String gardenerName){
        SensorStation sensorStation = loadSensorstation(id);
        Set<Userx> managingUsers = sensorStation.getManagingUsers();
        if (managingUsers == null) {
            managingUsers = new HashSet<>();
        }
        managingUsers.add(userService.getUserById(gardenerName));
        sensorStation.setManagingUsers(managingUsers);
        saveSensorstation(sensorStation);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorStation deleteGardenerFromSensorStation(Long id, String gardenerName) {
        SensorStation sensorStation = loadSensorstation(id);
        Set<Userx> managingUsers = sensorStation.getManagingUsers();
        managingUsers.remove(userService.getUserById(gardenerName));
        sensorStation.setManagingUsers(managingUsers);
        sensorStation = saveSensorstation(sensorStation);
        return sensorStation;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void changeHouseData(Long id, String uuid, String name) {
        SensorStation sensorStation = loadSensorstation(id);
        sensorStation.setUniversallyUniqueIdentifier(uuid);
        sensorStation.setSensorStationName(name);
        saveSensorstation(sensorStation);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GARDENER')")
    public SensorDataOfAllSensorsInTimeRangeDto getSensorDataInTimeRange(SensorStation sensorStation, LocalDateTime startDate, LocalDateTime endDate) {
        Collection<SensorData> sensorData = sensorDataRepository.findAllBySensorStation(sensorStation);
        sensorData = sensorData.stream().filter(sd -> sd.getDataTime().isAfter(startDate)).filter(sd -> sd.getDataTime().isBefore(endDate)).collect(Collectors.toList());
        SensorDataOfAllSensorsInTimeRangeDto sensorDataOfAllSensorsInTimeRangeDto = new SensorDataOfAllSensorsInTimeRangeDto();
        sensorDataOfAllSensorsInTimeRangeDto.setSoilHumidity(sensorData.stream().filter(sd -> sd.getSensorType().equals(SensorType.SOIL_HUMIDITY)).collect(Collectors.toList()));
        sensorDataOfAllSensorsInTimeRangeDto.setTemperature(sensorData.stream().filter(sd -> sd.getSensorType().equals(SensorType.TEMPERATURE)).collect(Collectors.toList()));
        sensorDataOfAllSensorsInTimeRangeDto.setAirHumidity(sensorData.stream().filter(sd -> sd.getSensorType().equals(SensorType.AIR_HUMIDITY)).collect(Collectors.toList()));
        sensorDataOfAllSensorsInTimeRangeDto.setAirPressure(sensorData.stream().filter(sd -> sd.getSensorType().equals(SensorType.AIR_PRESSURE)).collect(Collectors.toList()));
        sensorDataOfAllSensorsInTimeRangeDto.setAirQuality(sensorData.stream().filter(sd -> sd.getSensorType().equals(SensorType.AIR_QUALITY)).collect(Collectors.toList()));
        sensorDataOfAllSensorsInTimeRangeDto.setLight(sensorData.stream().filter(sd -> sd.getSensorType().equals(SensorType.LIGHT)).collect(Collectors.toList()));

        return sensorDataOfAllSensorsInTimeRangeDto;
    }

    public CurrentStationValuesDto getCurrentStationValues(SensorStation sensorStation){
        CurrentStationValuesDto currentStationValuesDto = new CurrentStationValuesDto();

        if(sensorStation.getAccesspoint() == null) {
            return currentStationValuesDto;
        }

        LocalTime transmissionInterval = sensorStation.getAccesspoint().getTransmissionInterval().toLocalTime();

        SensorData soilHumidity = sensorDataRepository.findFirstBySensorStationAndSensorTypeOrderByDataTimeDesc(sensorStation,SensorType.SOIL_HUMIDITY);
        //if(soilHumidity != null && soilHumidity.getDataTime().isAfter(LocalDateTime.now().minusSeconds(transmissionInterval.getSecond() * 25 ))){
            currentStationValuesDto.setSoilHumidity(soilHumidity);
        //}
        SensorData temperature = sensorDataRepository.findFirstBySensorStationAndSensorTypeOrderByDataTimeDesc(sensorStation,SensorType.TEMPERATURE);
        //if(temperature != null && temperature.getDataTime().isAfter(LocalDateTime.now().minusSeconds(transmissionInterval.getSecond() *25 ))){
            currentStationValuesDto.setTemperature(temperature);
        //}
        SensorData airHumidity = sensorDataRepository.findFirstBySensorStationAndSensorTypeOrderByDataTimeDesc(sensorStation,SensorType.AIR_HUMIDITY);
        //if(airHumidity != null && airHumidity.getDataTime().isAfter(LocalDateTime.now().minusSeconds(transmissionInterval.getSecond() *25 ))){
            currentStationValuesDto.setAirHumidity(airHumidity);
        //}
        SensorData airPressure = sensorDataRepository.findFirstBySensorStationAndSensorTypeOrderByDataTimeDesc(sensorStation,SensorType.AIR_PRESSURE);
        //if(airPressure != null && airPressure.getDataTime().isAfter(LocalDateTime.now().minusSeconds(transmissionInterval.getSecond() *25))){
            currentStationValuesDto.setAirPressure(airPressure);
        //}
        SensorData airQuality = sensorDataRepository.findFirstBySensorStationAndSensorTypeOrderByDataTimeDesc(sensorStation,SensorType.AIR_QUALITY);
        //if(airQuality != null && airQuality.getDataTime().isAfter(LocalDateTime.now().minusSeconds(transmissionInterval.getSecond() *25))){
            currentStationValuesDto.setAirQuality(airQuality);
        //}
        SensorData light = sensorDataRepository.findFirstBySensorStationAndSensorTypeOrderByDataTimeDesc(sensorStation,SensorType.LIGHT);
        //if(light != null && light.getDataTime().isAfter(LocalDateTime.now().minusSeconds(transmissionInterval.getSecond() *25 ))){
            currentStationValuesDto.setLight(light);
        //}

        if(currentStationValuesDto.getAirQuality()==null && currentStationValuesDto.getLight()==null && currentStationValuesDto.getSoilHumidity()==null && currentStationValuesDto.getTemperature()==null && currentStationValuesDto.getAirHumidity()==null && currentStationValuesDto.getAirPressure()==null){
            sensorStation.setAccessible(false);
        }else{
            sensorStation.setAccessible(true);
        }
        saveSensorstation(sensorStation);
        currentStationValuesDto.setAccessible(sensorStation.isAccessible());

        return currentStationValuesDto;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','GARDENER')")
    public void editLimits(SensorStation sensorStation,SensorType sensorType,int min, int max){
        SensorLimit sensorLimit = sensorStation.getSensorLimits().stream().filter(l -> l.getSensorType().equals(sensorType)).findAny().orElse(new SensorLimit());
        sensorLimit.setThresholdMin(min);
        sensorLimit.setThresholdMax(max);
        sensorLimitService.saveSensorLimit(sensorLimit);

    }


    @PreAuthorize("hasAuthority('GARDENER')")
    public Collection<SensorStation> getAllHousesOfGardener(){
        return userService.getAuthenticatedUser().getManagedSensorStations();
    }
    public Collection<SensorStation> getMyHouses(){
        return userService.getAuthenticatedUser().getObservedSensorStations();
    }
    public void addHouseToObserve(Long id){
        SensorStation sensorStation = loadSensorstation(id);
        Set<Userx> observingUsers = sensorStation.getObservingUsers();
        if(!observingUsers.contains(userService.getAuthenticatedUser())) {
            observingUsers.add(userService.getAuthenticatedUser());
            sensorStation.setObservingUsers(observingUsers);
        }else{
            observingUsers.remove(userService.getAuthenticatedUser());
            sensorStation.setObservingUsers(observingUsers);
        }
        saveSensorstation(sensorStation);
    }
}
