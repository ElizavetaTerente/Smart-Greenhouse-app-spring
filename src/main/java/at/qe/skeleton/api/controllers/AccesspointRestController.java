package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.dto.AccesspointDTO;
import at.qe.skeleton.model.Accesspoint;
import at.qe.skeleton.services.AccesspointService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccesspointRestController {

    @Autowired
    AccesspointService accesspointService;
    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("/api/AccessPoint/{id}")
    public AccesspointDTO getAccesspoint(@PathVariable Long id) {
        return modelMapper.map(accesspointService.loadAccesspoint(id), AccesspointDTO.class);
    }


    @PostMapping("/api/AccessPoint")
    public AccesspointDTO postAccesspoint(@RequestBody AccesspointDTO accesspointDTO) {
        Accesspoint accesspoint = accesspointService.saveAccesspoint(modelMapper.map(accesspointDTO,Accesspoint.class));
        return modelMapper.map(accesspoint, AccesspointDTO.class);
    }
}
