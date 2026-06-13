package com.example.uavmanager.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.uavmanager.domain.Drone;
import com.example.uavmanager.domain.DroneAiAttributes;
import com.example.uavmanager.domain.DroneForm;
import com.example.uavmanager.exception.DuplicateSerialNoException;
import com.example.uavmanager.repository.DroneMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {

    @Mock
    private DroneMapper droneMapper;

    @Mock
    private DroneAiAttributeServiceImpl aiAttributeService;

    @InjectMocks
    private DroneServiceImpl droneService;

    @Test
    void should_createDroneWithAiAttributes_when_serialNoUnique() {
        DroneForm form = new DroneForm();
        form.setName("A");
        form.setModel("M");
        form.setSerialNo("S1");

        when(droneMapper.selectIdBySerialNo("S1")).thenReturn(java.util.Optional.empty());
        when(aiAttributeService.generate("A", "M", "S1"))
                .thenReturn(new DroneAiAttributes(100, 60, 1.5, 5000, 88));
        when(droneMapper.insert(any())).thenAnswer(invocation -> {
            Drone d = invocation.getArgument(0);
            d.setId(10L);
            return 1;
        });

        long id = droneService.create(form);

        assertThat(id).isEqualTo(10L);
    }

    @Test
    void should_throwDuplicate_when_serialNoExists() {
        DroneForm form = new DroneForm();
        form.setName("A");
        form.setModel("M");
        form.setSerialNo("S1");

        when(droneMapper.selectIdBySerialNo("S1")).thenReturn(java.util.Optional.of(99L));

        assertThatThrownBy(() -> droneService.create(form))
                .isInstanceOf(DuplicateSerialNoException.class);
    }
}

