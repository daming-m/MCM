package com.example.uavmanager.domain;

import lombok.Value;

@Value
public class DroneAiAttributes {
    int maxSpeedKmh;
    int flightTimeMin;
    double payloadKg;
    int batteryMah;
    int aiScore;
}

