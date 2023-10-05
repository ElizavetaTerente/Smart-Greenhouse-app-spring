package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.SensorLimit;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorType;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.frontEndDto.CurrentStationValuesDto;
import at.qe.skeleton.model.frontEndDto.SensorDataOfAllSensorsInTimeRangeDto;
import at.qe.skeleton.model.frontEndDto.SensorStationAccesspointDto;
import at.qe.skeleton.model.frontEndDto.SensorStationLimitsDto;
import at.qe.skeleton.services.SensorLimitService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
public class SensorStationsController {

    @Autowired
    SensorStationService sensorStationService;

    @Autowired
    SensorLimitService sensorLimitService;

    @Autowired
    UserService userService;

    @GetMapping("/loadHouse/{id}")
    public SensorStationAccesspointDto loadHouse(@PathVariable Long id){
        SensorStation sensorStation = sensorStationService.loadSensorstation(id);
        return new SensorStationAccesspointDto(sensorStation,sensorStation.getAccesspoint());
    }
    @PostMapping("/admin/addGardenerToHouse/{id}/{gardener}")
    public void addGardenerToHouse(@PathVariable Long id,@PathVariable String gardener){
        sensorStationService.addGardenerToSensorStation(id,gardener);
    }

    @PostMapping("/admin/deleteGardenerFromHouse/{id}/{gardener}")
    public void deleteGardenerFromHouse(@PathVariable Long id,@PathVariable String gardener){
        sensorStationService.deleteGardenerFromSensorStation(id,gardener);
    }

    @PostMapping("/admin/saveHouseChanges/{id}/{uuid}/{name}")
    public void changeHouseData(@PathVariable Long id,@PathVariable String uuid,@PathVariable String name){
        sensorStationService.changeHouseData(id,uuid,name);
    }

    @GetMapping("/getSensorDataInRange/{id}/{startDate}/{endDate}")
    public SensorDataOfAllSensorsInTimeRangeDto getSensorDataInTimeRange(@PathVariable Long id, @PathVariable String startDate, @PathVariable String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);
        SensorStation sensorStation = sensorStationService.loadSensorstation(id);
        return  sensorStationService.getSensorDataInTimeRange(sensorStation,startDateTime,endDateTime);
    }

    @GetMapping("/getCurrentStationValues/{id}")
    public CurrentStationValuesDto getCurrentStationValues(@PathVariable Long id){
        return sensorStationService.getCurrentStationValues(sensorStationService.loadSensorstation(id));
    }

    @GetMapping("/getTransmissionInterval/{id}")
    public int getTransmissionInterval(@PathVariable Long id){
        return sensorStationService.loadSensorstation(id).getAccesspoint().getTransmissionInterval().toLocalTime().toSecondOfDay();
    }


    @PostMapping("/editLimits/{id}/{sensorType}/{min}/{max}")
    public void editLimits(@PathVariable Long id,@PathVariable SensorType sensorType,@PathVariable int min,@PathVariable int max){
        sensorStationService.editLimits(sensorStationService.loadSensorstation(id),sensorType,min,max);
    }

    @DeleteMapping("/admin/deleteSensorStation/{id}")
    public void deleteSensorStation(@PathVariable Long id){
        sensorStationService.deleteSensorstation(sensorStationService.loadSensorstation(id));
    }

    @GetMapping("/getLimitsOfSensorStation/{id}")
    public SensorStationLimitsDto getLimitsOfSensorStation(@PathVariable Long id){
        SensorStation sensorStation = sensorStationService.loadSensorstation(id);
        Set<SensorLimit> limits = sensorStation.getSensorLimits();
        SensorStationLimitsDto sensorStationLimitsDto = new SensorStationLimitsDto();
       return sensorLimitService.getAllLimitsOfSensorStation(sensorStation);
    }

    //for frontEnd checkBox
    @PostMapping("/addHouseToObserve/{id}")
    public void addHouseToObserve(@PathVariable Long id){
        sensorStationService.addHouseToObserve(id);
    }

    //for all houses page
    //better replace to service
    @GetMapping("/loadAllActiveSensorStations")
    public Collection<SensorStationAccesspointDto> loadActiveSensorStations(){
        Collection<SensorStation> activeSensorStations = sensorStationService.getAllActiveSensorstations();
        Collection<SensorStationAccesspointDto> sensorStationAccesspointDtos = new ArrayList<>();
        activeSensorStations.forEach(ss-> sensorStationAccesspointDtos.add(loadHouse(ss.getSensorStationId())));
        sensorStationAccesspointDtos.forEach(ssd-> ssd.setChecked(ssd.getHouse().getObservingUsers().contains(userService.getAuthenticatedUser())));
        return sensorStationAccesspointDtos;
    }
/*
    @GetMapping("/admin/loadAllCheckedHouses")
    public Collection<SensorStationAccesspointDto> loadAllCheckedHouses(){
        Collection<SensorStation> activeSensorStations = sensorStationService.getAllCheckedHouses();
        Collection<SensorStationAccesspointDto> sensorStationAccesspointDtos = new ArrayList<>();
        activeSensorStations.forEach(ss-> sensorStationAccesspointDtos.add(loadHouse(ss.getSensorStationId())));
        return sensorStationAccessploadAllHousesointDtos;
    }

 */

    @GetMapping("/gardener/loadAllHousesOfGardener")
    public Collection<SensorStationAccesspointDto> loadAllHousesOfGardener(){
        Collection<SensorStation> activeSensorStations = sensorStationService.getAllHousesOfGardener();
        Collection<SensorStationAccesspointDto> sensorStationAccesspointDtos = new ArrayList<>();
        activeSensorStations.forEach(ss-> sensorStationAccesspointDtos.add(loadHouse(ss.getSensorStationId())));
        return sensorStationAccesspointDtos;
    }

    @GetMapping("/loadMyHouses")
    public Collection<SensorStationAccesspointDto> loadMyHouses(){
        Collection<SensorStation> activeSensorStations = sensorStationService.getMyHouses();
        Collection<SensorStationAccesspointDto> sensorStationAccesspointDtos = new ArrayList<>();
        activeSensorStations.forEach(ss-> sensorStationAccesspointDtos.add(loadHouse(ss.getSensorStationId())));
        return sensorStationAccesspointDtos;
    }
}
