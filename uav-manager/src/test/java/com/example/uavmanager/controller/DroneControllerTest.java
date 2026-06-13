package com.example.uavmanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.uavmanager.domain.Drone;
import com.example.uavmanager.service.DroneService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DroneController.class)
@AutoConfigureMockMvc(addFilters = false)
class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    @Test
    void should_renderListView_when_getDrones() throws Exception {
        when(droneService.list(null)).thenReturn(Collections.<Drone>emptyList());

        mockMvc.perform(get("/drones"))
                .andExpect(status().isOk())
                .andExpect(view().name("drones/list"));
    }
}

