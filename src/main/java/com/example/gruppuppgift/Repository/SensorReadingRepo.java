package com.example.gruppuppgift.Repository;

import com.example.gruppuppgift.Model.SensorReading;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorReadingRepo extends MongoRepository<SensorReading, String> {
    SensorReading findTopByOrderByTimestampDesc();
}