package com.example.gruppuppgift.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sensor_readings")
@Data
public class SensorReading {
    @Id
    private String id;
    private double temperature;
    private double humidity;
    private LocalDateTime timestamp;


    public SensorReading(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = LocalDateTime.now();
    }

}