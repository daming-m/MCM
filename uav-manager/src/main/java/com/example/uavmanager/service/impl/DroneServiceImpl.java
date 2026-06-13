package com.example.uavmanager.service.impl;

import com.example.uavmanager.domain.Drone;
import com.example.uavmanager.domain.DroneAiAttributes;
import com.example.uavmanager.domain.DroneForm;
import com.example.uavmanager.exception.DuplicateSerialNoException;
import com.example.uavmanager.exception.NotFoundException;
import com.example.uavmanager.repository.DroneMapper;
import com.example.uavmanager.service.DroneAiAttributeService;
import com.example.uavmanager.service.DroneService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneMapper droneMapper;
    private final DroneAiAttributeService aiAttributeService;

    @Override
    public List<Drone> list(String q) {
        return droneMapper.selectAll(normalizeQuery(q));
    }

    @Override
    public Optional<Drone> getById(long id) {
        return droneMapper.selectById(id);
    }

    @Override
    @Transactional
    public long create(DroneForm form) {
        validateSerialNoUnique(form.getSerialNo(), null);

        DroneAiAttributes attrs = aiAttributeService.generate(form.getName(), form.getModel(), form.getSerialNo());
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        Drone drone = new Drone();
        drone.setName(form.getName());
        drone.setModel(form.getModel());
        drone.setSerialNo(form.getSerialNo());

        drone.setMaxSpeedKmh(attrs.getMaxSpeedKmh());
        drone.setFlightTimeMin(attrs.getFlightTimeMin());
        drone.setPayloadKg(attrs.getPayloadKg());
        drone.setBatteryMah(attrs.getBatteryMah());
        drone.setAiScore(attrs.getAiScore());

        drone.setCreatedAt(now);
        drone.setUpdatedAt(now);

        droneMapper.insert(drone);
        return drone.getId();
    }

    @Override
    @Transactional
    public void update(long id, DroneForm form, boolean regenAi) {
        Drone existing = droneMapper.selectById(id)
                .orElseThrow(() -> new NotFoundException("Drone not found: " + id));

        validateSerialNoUnique(form.getSerialNo(), id);

        existing.setName(form.getName());
        existing.setModel(form.getModel());
        existing.setSerialNo(form.getSerialNo());

        if (regenAi) {
            DroneAiAttributes attrs = aiAttributeService.generate(form.getName(), form.getModel(), form.getSerialNo());
            existing.setMaxSpeedKmh(attrs.getMaxSpeedKmh());
            existing.setFlightTimeMin(attrs.getFlightTimeMin());
            existing.setPayloadKg(attrs.getPayloadKg());
            existing.setBatteryMah(attrs.getBatteryMah());
            existing.setAiScore(attrs.getAiScore());
        }

        existing.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        int updated = droneMapper.update(existing);
        if (updated <= 0) {
            throw new NotFoundException("Drone not found: " + id);
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        int deleted = droneMapper.deleteById(id);
        if (deleted <= 0) {
            throw new NotFoundException("Drone not found: " + id);
        }
    }

    private String normalizeQuery(String q) {
        if (q == null) {
            return null;
        }
        String trimmed = q.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void validateSerialNoUnique(String serialNo, Long currentIdOrNull) {
        droneMapper.selectIdBySerialNo(serialNo).ifPresent(existingId -> {
            if (currentIdOrNull == null || existingId.longValue() != currentIdOrNull.longValue()) {
                throw new DuplicateSerialNoException("serialNo already exists: " + serialNo);
            }
        });
    }
}

