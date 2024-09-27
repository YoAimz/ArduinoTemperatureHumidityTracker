package com.example.gruppuppgift.Services;

import com.example.gruppuppgift.Model.SensorReading;
import com.fazecast.jSerialComm.SerialPort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

@Service
public class SerialCommunicationService {

    private static final Logger logger = LoggerFactory.getLogger(SerialCommunicationService.class);
    private final SensorReadingService sensorReadingService;
    private final ObjectMapper objectMapper;

    @Value("${serial.port:COM3}")
    private String serialPortName;

    @Value("${serial.baud-rate:9600}")
    private int baudRate;

    public SerialCommunicationService(SensorReadingService sensorReadingService, ObjectMapper objectMapper) {
        this.sensorReadingService = sensorReadingService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        SerialPort[] ports = SerialPort.getCommPorts();
        logger.info("Available serial ports: {}", Arrays.toString(ports));

        SerialPort comPort = SerialPort.getCommPort(serialPortName);
        comPort.setComPortParameters(baudRate, 8, 1, SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        if (comPort.openPort()) {
            logger.info("Successfully opened the port: {}", comPort.getSystemPortName());

            Thread serialThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(comPort.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info("Raw data received: {}", line);
                        try {
                            line = line.trim();
                            //checking if the line contains both temperature and humidity
                            if (line.contains("temperature") && line.contains("humidity")) {
                                String[] parts = line.split(",");
                                double temperature = Double.parseDouble(parts[0].split(":")[1].trim());
                                double humidity = Double.parseDouble(parts[1].split(":")[1].trim());

                                SensorReading reading = new SensorReading(temperature, humidity);
                                logger.info("Parsed reading: temperature={}, humidity={}",
                                        reading.getTemperature(), reading.getHumidity());
                                sensorReadingService.addReading(reading);
                            } else {
                                logger.warn("Received data does not contain temperature and humidity: {}", line);
                            }
                        } catch (Exception e) {
                            logger.error("Error parsing sensor data: {}", e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error reading from serial port: {}", e.getMessage());
                }
            });
            serialThread.start();
        } else {
            logger.error("Failed to open the port: {}", comPort.getSystemPortName());
        }
    }
}

