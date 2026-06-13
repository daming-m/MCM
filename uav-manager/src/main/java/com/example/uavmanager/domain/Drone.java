package com.example.uavmanager.domain;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Drone {
    private Long id;

    private String name;
    private String model;
    private String serialNo;

    private Integer maxSpeedKmh;
    private Integer flightTimeMin;
    private Double payloadKg;
    private Integer batteryMah;
    private Integer aiScore;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}

