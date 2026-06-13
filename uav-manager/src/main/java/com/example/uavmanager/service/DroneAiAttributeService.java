package com.example.uavmanager.service;

import com.example.uavmanager.domain.DroneAiAttributes;

public interface DroneAiAttributeService {

    /**
     * Generate AI attributes for a drone.
     *
     * @param name drone name
     * @param model drone model
     * @param serialNo drone serial number
     * @return generated AI attributes
     */
    DroneAiAttributes generate(String name, String model, String serialNo);
}

