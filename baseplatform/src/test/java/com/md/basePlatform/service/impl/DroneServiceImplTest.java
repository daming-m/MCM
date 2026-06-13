package com.md.basePlatform.service.impl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.md.basePlatform.entity.Drone;
import com.md.basePlatform.entity.PageResult;
import com.md.basePlatform.mapper.DroneMapper;

/**
 * 无人机业务服务实现类测试
 * 
 * @author system
 */
@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {

    @Mock
    private DroneMapper droneMapper;

    @InjectMocks
    private DroneServiceImpl droneService;

    @Test
    void should_create_drone_with_ai_attributes_when_valid_request() {
        // Given
        Drone request = new Drone();
        request.setName("测试无人机");
        request.setModel("TY-001");
        request.setSerialNo("SN20240001");
        
        when(droneMapper.findBySerialNo("SN20240001")).thenReturn(null);

        // When
        Drone result = droneService.create(request);

        // Then
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertNotNull(result.getMaxSpeedKmh());
        assertNotNull(result.getFlightTimeMin());
        assertNotNull(result.getPayloadKg());
        assertNotNull(result.getBatteryMah());
        assertNotNull(result.getAiScore());
        assertTrue(result.getMaxSpeedKmh() >= 20 && result.getMaxSpeedKmh() <= 120);
        assertTrue(result.getFlightTimeMin() >= 10 && result.getFlightTimeMin() <= 180);
        assertTrue(result.getAiScore() >= 60 && result.getAiScore() <= 95);
        verify(droneMapper).insert(request);
    }

    @Test
    void should_throw_exception_when_serial_no_exists() {
        // Given
        Drone request = new Drone();
        request.setName("测试无人机");
        request.setModel("TY-001");
        request.setSerialNo("SN20240001");
        
        Drone existing = new Drone();
        existing.setId(1L);
        when(droneMapper.findBySerialNo("SN20240001")).thenReturn(existing);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> droneService.create(request));
        assertEquals("序列号已存在：SN20240001", exception.getMessage());
    }

    @Test
    void should_get_drone_when_id_exists() {
        // Given
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setName("测试无人机");
        when(droneMapper.findById(1L)).thenReturn(drone);

        // When
        Drone result = droneService.getById(1L);

        // Then
        assertEquals(1L, result.getId());
        assertEquals("测试无人机", result.getName());
    }

    @Test
    void should_list_drones_when_call_list() {
        // Given
        Drone drone = new Drone();
        drone.setName("测试无人机");
        when(droneMapper.findAll()).thenReturn(Collections.singletonList(drone));

        // When
        List<Drone> result = droneService.list();

        // Then
        assertEquals(1, result.size());
        assertEquals("测试无人机", result.get(0).getName());
    }

    @Test
    void should_return_page_result_when_valid_page_request() {
        // Given
        Drone drone = new Drone();
        drone.setName("测试无人机");
        when(droneMapper.findPageByCondition(anyString(), any(Integer.class), any(Integer.class)))
            .thenReturn(Collections.singletonList(drone));
        when(droneMapper.countByCondition(anyString())).thenReturn(1L);

        // When
        PageResult<Drone> result = droneService.page(1, 10, "测试");

        // Then
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void should_update_drone_when_valid_request() {
        // Given
        Long id = 1L;
        Drone existing = new Drone();
        existing.setId(id);
        existing.setSerialNo("SN20240001");
        existing.setMaxSpeedKmh(80);
        
        Drone updateRequest = new Drone();
        updateRequest.setName("更新后的无人机");
        updateRequest.setModel("TY-002");
        updateRequest.setSerialNo("SN20240001");
        
        Drone updated = new Drone();
        updated.setId(id);
        updated.setName("更新后的无人机");
        
        when(droneMapper.findById(id)).thenReturn(existing).thenReturn(updated);

        // When
        Drone result = droneService.update(id, updateRequest);

        // Then
        assertEquals("更新后的无人机", result.getName());
        verify(droneMapper).update(any(Drone.class));
    }

    @Test
    void should_throw_exception_when_update_nonexistent_drone() {
        // Given
        Long id = 999L;
        Drone updateRequest = new Drone();
        when(droneMapper.findById(id)).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> droneService.update(id, updateRequest));
        assertEquals("无人机不存在：999", exception.getMessage());
    }

    @Test
    void should_delete_true_when_row_affected() {
        // Given
        when(droneMapper.deleteById(2L)).thenReturn(1);

        // When
        boolean result = droneService.delete(2L);

        // Then
        assertTrue(result);
    }

    @Test
    void should_regenerate_ai_attributes_when_valid_id() {
        // Given
        Long id = 1L;
        Drone existing = new Drone();
        existing.setId(id);
        existing.setName("测试无人机");
        existing.setModel("TY-001");
        existing.setSerialNo("SN20240001");
        existing.setMaxSpeedKmh(80);
        
        when(droneMapper.findById(id)).thenReturn(existing);

        // When
        Drone result = droneService.regenerateAiAttributes(id);

        // Then
        assertNotNull(result.getMaxSpeedKmh());
        assertNotNull(result.getFlightTimeMin());
        assertNotNull(result.getPayloadKg());
        assertNotNull(result.getBatteryMah());
        assertNotNull(result.getAiScore());
        verify(droneMapper).update(existing);
    }

    @Test
    void should_throw_exception_when_regenerate_nonexistent_drone() {
        // Given
        Long id = 999L;
        when(droneMapper.findById(id)).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> droneService.regenerateAiAttributes(id));
        assertEquals("无人机不存在：999", exception.getMessage());
    }
}
