package com.md.basePlatform.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.md.basePlatform.mapper.DroneMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RootController.class)
@AutoConfigureMockMvc(addFilters = false)
class RootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneMapper droneMapper;

    @Test
    void should_return_success_message_when_visit_root_path() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("无人机管理项目启动成功！"));
    }
}
