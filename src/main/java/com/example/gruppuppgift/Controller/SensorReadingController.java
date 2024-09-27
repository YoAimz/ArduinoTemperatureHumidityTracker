package com.example.gruppuppgift.Controller;

import com.example.gruppuppgift.Model.SensorReading;
import com.example.gruppuppgift.Services.SensorReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @GetMapping("/api/sensor/latest")
    @ResponseBody
    public SensorReading getLatestReading() {
        return sensorReadingService.getLatestReading();
    }

    @GetMapping("/api/sensor/history")
    @ResponseBody
    public List<SensorReading> getHistory() {
        return sensorReadingService.getHistory();
    }
}
