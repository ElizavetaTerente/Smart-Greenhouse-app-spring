package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.frontEndDto.AccesspointSensorStationsDto;
import at.qe.skeleton.model.frontEndDto.CustomTime;
import at.qe.skeleton.services.AccesspointService;
import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

@RestController
public class AccesspointsController {

    @Autowired
    AccesspointService accesspointService;

    @Autowired
    SensorStationService sensorStationService;

    /*
    @GetMapping("/admin/loadAccesspoints")
    public Collection<Accesspoint> loadAccesspoints(){
        return accesspointService.getAllActiveAccesspoints();
    }
     */


    @GetMapping("/loadAccesspoints")
    public Collection<AccesspointSensorStationsDto> loadAccesspoints(){

        List<AccesspointSensorStationsDto> accesspointSensorStationsDtos = new ArrayList<>();
        Collection<Accesspoint> accesspoints = accesspointService.getAllActiveAccesspoints();

        accesspoints.forEach(a -> {
            accesspointSensorStationsDtos.add(new AccesspointSensorStationsDto(a,sensorStationService.getAllSensorstationsOfAccesspoint(a)));
        });

        accesspointSensorStationsDtos.forEach(a -> {
            Time interval = a.getAccesspoint().getTransmissionInterval();
            if (interval != null) {
                a.setTime(new CustomTime(interval.getHours(), interval.getMinutes(), interval.getSeconds()));
            } else {
                a.setTime(new CustomTime(0,5,0));
            }
        });
        return accesspointSensorStationsDtos;
    }

    @DeleteMapping("/admin/deleteAccesspoint/{id}")
    public void deleteAccesspoint(@PathVariable Long id){
        accesspointService.deleteAccesspoint(accesspointService.loadAccesspoint(id));
    }
/*
    @PostMapping("/admin/newAccesspoint/{location}/{transmissionInterval}")
    public void addNewAccesspoint(@PathVariable String location, @PathVariable Time transmissionInterval){
        Accesspoint accesspoint = new Accesspoint();
        accesspoint.setLocationName(location);
        accesspoint.setTransmissionInterval(transmissionInterval);
        accesspointService.createAccesspoint(accesspoint);
    }

 */

    @PostMapping("/admin/acceptAccesspoint/{id}")
    public void accept(@PathVariable Long id){

        accesspointService.accept(accesspointService.loadAccesspoint(id));
    }

    @GetMapping("/admin/searchSensorStations/{id}")
    public Collection<SensorStation> searchSensorStations(@PathVariable Long id){
        return accesspointService.searchSensorStations(id);
    }

    @GetMapping("/loadSensorStations")
    public Collection<SensorStation> loadAvalibleSensorStations(){
        return accesspointService.loadSensorStations();
    }

    @PostMapping("/admin/acceptSensorStation/{id}")
    public void acceptSensorStation(@PathVariable Long id){
        sensorStationService.acceptSensorStation(id);
    }

    @PostMapping("/admin/addSensorStationToAccesspoint/{sensorStationId}/{accesspointId}")
    public void addSensorStation(@PathVariable Long sensorStationId,@PathVariable Long accesspointId){
        accesspointService.addSensorStation(sensorStationId,accesspointId);
    }

    @GetMapping("/admin/getSensorStationsOfAccesspoint/{accesspointId}")
    public Collection<SensorStation> getSensorStationsOfAccesspoint(@PathVariable Long accesspointId){
        Accesspoint accesspoint = accesspointService.loadAccesspoint(accesspointId);
       return sensorStationService.getAllSensorstationsOfAccesspoint(accesspoint);
    }

    @PostMapping("/admin/deleteSensorStationFromAccesspoint/{sensorStationId}")
    public void deleteSensorStationFromAccesspoint(@PathVariable Long sensorStationId){
        sensorStationService.removeFromAccesspoint(sensorStationId);
    }

    @PostMapping("/editAccesspoint/{id}/{locationName}/{hours}/{minutes}/{seconds}")
    public void editAccesspoint(@PathVariable Long id,@PathVariable String locationName,@PathVariable int hours,@PathVariable int minutes,@PathVariable int seconds){
        Accesspoint accesspoint = accesspointService.loadAccesspoint(id);
        if(!locationName.equals("undefined")) {
            accesspoint.setLocationName(locationName);
        }
        Time interval = Time.valueOf(LocalTime.of(hours,minutes,seconds));
        accesspoint.setTransmissionInterval(interval);
        accesspointService.saveAccesspoint(accesspoint);
    }

}
