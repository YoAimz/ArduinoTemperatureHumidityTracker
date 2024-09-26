package com.example.gruppuppgift.Services;

import com.example.gruppuppgift.Model.SensorReading;
import com.example.gruppuppgift.Repository.SensorReadingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorReadingService {

    private final SensorReadingRepo repository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SensorReadingService(SensorReadingRepo repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public SensorReading addReading(SensorReading reading) {
        reading.setTimestamp(LocalDateTime.now());
        SensorReading savedReading = repository.save(reading);

        messagingTemplate.convertAndSend("/topic/readings", savedReading);

        return savedReading;
    }

    public SensorReading getLatestReading() {
        return repository.findTopByOrderByTimestampDesc();
    }

    public List<SensorReading> getHistory() {
        return repository.findAll();
    }
}
