package com.example.guppuppgift.Controller;

import com.example.guppuppgift.Model.SensorReading;
import com.example.guppuppgift.Services.SensorReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    @Autowired
    public SensorReadingController(SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("latestReading", sensorReadingService.getLatestReading());
        model.addAttribute("history", sensorReadingService.getHistory());
        return "dashboard";
    }
}
