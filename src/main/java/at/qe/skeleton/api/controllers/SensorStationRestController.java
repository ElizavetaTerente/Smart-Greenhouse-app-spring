package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.dto.SensorStationDTO;
import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.AccesspointService;
import at.qe.skeleton.services.SensorStationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SensorStationRestController {

    @Autowired
    SensorStationService sensorstationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccesspointService accesspointService;

    @PostMapping("/api/NewSensorStation/{accesspointId}")
    public SensorStationDTO postSensorStation(@RequestBody SensorStationDTO sensorStationDTO, @PathVariable Long accesspointId) {
        Accesspoint accesspoint = accesspointService.loadAccesspoint(accesspointId);
        accesspoint.setSearchingSensorStation(false);
        accesspointService.saveAccesspoint(accesspoint);
        long newDipId = sensorStationDTO.getSensorStationDipId();
        List<Long> dipIds = sensorstationService.getAllSensorstations().stream().map(SensorStation::getSensorStationDipId).toList();
        if (newDipId < 0 || dipIds.contains(newDipId)) {
            return null;
        }
        SensorStation sensorStation = sensorstationService.saveSensorstation(modelMapper.map(sensorStationDTO,SensorStation.class));
        return modelMapper.map(sensorStation, SensorStationDTO.class);
    }
}
