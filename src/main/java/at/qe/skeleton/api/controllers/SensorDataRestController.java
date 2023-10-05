package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.dto.SensorDataDTO;

import at.qe.skeleton.model.SensorData;
import at.qe.skeleton.services.SensorDataService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class SensorDataRestController {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private ModelMapper modelMapper;


    /**
     Initializes a TypeMap between SensorData and SensorDataDTO using ModelMapper library and adds a mapping
     to map the id field of the SensorStation object in SensorData to the sensorStationId field in SensorDataDTO.
     */
    @PostConstruct
    public void init() {
        TypeMap<SensorData, SensorDataDTO> typeMap = modelMapper.createTypeMap(SensorData.class, SensorDataDTO.class);
        typeMap.addMappings(mapper -> mapper.map(src -> src.getSensorStation().getId(), SensorDataDTO::setSensorStationId));

        TypeMap<SensorDataDTO, SensorData> reverseTypeMap = modelMapper.createTypeMap(SensorDataDTO.class, SensorData.class);
        reverseTypeMap.addMappings(mapper -> mapper.map(SensorDataDTO::getSensorStationId, (dest, value) -> dest.getSensorStation().setId((Long) value)));
    }

    @PostMapping("api/SensorDataList")
    public List<SensorDataDTO> postSensorData(@RequestBody List<SensorDataDTO> sensorDataDTOs) {
        List<SensorData> sensorData = modelMapper.map(sensorDataDTOs, new TypeToken<List<SensorData>>() {}.getType());
        sensorData = sensorDataService.saveSensorData(sensorData);
        return modelMapper.map(sensorData, new TypeToken<List<SensorDataDTO>>() {}.getType());
    }

    @PostMapping("api/SensorData")
    public SensorDataDTO postSensorData(@RequestBody SensorDataDTO sensorDataDTO) {
        SensorData sensorData = modelMapper.map(sensorDataDTO, SensorData.class);
        sensorData = sensorDataService.saveSensorData(sensorData);
        return modelMapper.map(sensorData, SensorDataDTO.class);
    }

    @GetMapping("api/SensorData/{id}")
    public SensorDataDTO getSensorData(@PathVariable Long id) {
        SensorData sensorData = sensorDataService.loadSensorData(id);
        return modelMapper.map(sensorData, SensorDataDTO.class);
    }
}
