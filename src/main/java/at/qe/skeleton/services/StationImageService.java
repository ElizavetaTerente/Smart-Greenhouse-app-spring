package at.qe.skeleton.services;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.StationImage;
import at.qe.skeleton.repositories.StationImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Scope("application")
public class StationImageService {


    @Autowired
    SensorStationService sensorStationService;

    @Autowired
    StationImageRepository stationImageRepository;

    public void uploadPic(Long sensorStationId,String picName){
        SensorStation sensorStation = sensorStationService.loadSensorstation(sensorStationId);
        StationImage stationImage = new StationImage();
        stationImage.setSensorStation(sensorStation);
        stationImage.setPicturePath("../gallery/upload-dir/" + picName);
        stationImageRepository.save(stationImage);
    }

    public Collection<StationImage> loadAllActivePicsOfSensorStation(Long id){
        SensorStation sensorStation = sensorStationService.loadSensorstation(id);
        return stationImageRepository.findAllBySensorStationAndEnabled(sensorStation,true);
    }


    public StationImage loadById(Long id){
       return stationImageRepository.getStationImageById(id);
    }



}
