package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AuditEvent;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class AuditLogController {

    @Autowired
    AuditLogService auditLogService;


    @GetMapping("/admin/getLog")
    public List<AuditEvent> searchSensorStations(){
        return auditLogService.findAll();
    }
}
