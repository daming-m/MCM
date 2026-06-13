package com.example.uavmanager.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class DroneForm {

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    @Size(max = 64)
    private String model;

    @NotBlank
    @Size(max = 64)
    private String serialNo;
}

