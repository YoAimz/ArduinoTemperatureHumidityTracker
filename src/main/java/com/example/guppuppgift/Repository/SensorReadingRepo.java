package com.example.guppuppgift.Repository;

import com.example.guppuppgift.Model.SensorReading;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorReadingRepo extends MongoRepository<SensorReading, String> {
    SensorReading findTopByOrderByTimestampDesc();
}