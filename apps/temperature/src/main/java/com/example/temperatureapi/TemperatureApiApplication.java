package com.example.temperatureapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@SpringBootApplication
public class TemperatureApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemperatureApiApplication.class, args);
    }
}

@RestController
@RequestMapping("/temperature")
class TemperatureController {
    private final Random random = new Random();

    @GetMapping
    public TemperatureResponse getTemperature(
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "sensorId", required = false) String sensorId
    ) {
        // Normalize nulls to empty strings
        if (location == null) location = "";
        if (sensorId == null) sensorId = "";

        // If no location is provided, use a default based on sensor ID
        if (location.isEmpty()) {
            switch (sensorId) {
                case "1":
                    location = "Living Room";
                    break;
                case "2":
                    location = "Bedroom";
                    break;
                case "3":
                    location = "Kitchen";
                    break;
                default:
                    location = "Unknown";
            }
        }

        // If no sensor ID is provided, generate one based on location
        if (sensorId.isEmpty()) {
            switch (location) {
                case "Living Room":
                    sensorId = "1";
                    break;
                case "Bedroom":
                    sensorId = "2";
                    break;
                case "Kitchen":
                    sensorId = "3";
                    break;
                default:
                    sensorId = "0";
            }
        }

        // Generate a random temperature between -10.0 and 35.0 °C
        double temperature = -10.0 + (45.0 * random.nextDouble());

        return new TemperatureResponse(location, sensorId, round(temperature, 1));
    }

    private double round(double value, int decimals) {
        double scale = Math.pow(10, decimals);
        return Math.round(value * scale) / scale;
    }
}

class TemperatureResponse {
    private String location;
    private String sensorId;
    private double temperature;

    public TemperatureResponse(String location, String sensorId, double temperature) {
        this.location = location;
        this.sensorId = sensorId;
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public String getSensorId() {
        return sensorId;
    }

    public double getTemperature() {
        return temperature;
    }
}