package com.example.uavmanager.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.uavmanager.domain.DroneAiAttributes;
import org.junit.jupiter.api.Test;

class DroneAiAttributeServiceImplTest {

    @Test
    void should_generateStableAttributes_when_sameInput() {
        DroneAiAttributeServiceImpl service = new DroneAiAttributeServiceImpl();

        DroneAiAttributes a1 = service.generate("n", "m", "s");
        DroneAiAttributes a2 = service.generate("n", "m", "s");

        assertThat(a1).isEqualTo(a2);
    }
}

