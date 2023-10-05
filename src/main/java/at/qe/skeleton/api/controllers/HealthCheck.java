package at.qe.skeleton.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/api/HealthCheck")
    public int healthCheck() {
        return 200;
    }
}
