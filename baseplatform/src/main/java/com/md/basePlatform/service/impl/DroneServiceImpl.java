package com.md.basePlatform.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.md.basePlatform.entity.Drone;
import com.md.basePlatform.entity.PageResult;
import com.md.basePlatform.mapper.DroneMapper;
import com.md.basePlatform.service.DroneService;

/**
 * 无人机业务服务实现类
 * 
 * @author system
 */
@Service
public class DroneServiceImpl implements DroneService {

    private final DroneMapper droneMapper;

    public DroneServiceImpl(DroneMapper droneMapper) {
        this.droneMapper = droneMapper;
    }

    @Override
    public Drone create(Drone drone) {
        // 校验序列号唯一性
        if (droneMapper.findBySerialNo(drone.getSerialNo()) != null) {
            throw new IllegalArgumentException("序列号已存在：" + drone.getSerialNo());
        }

        LocalDateTime now = LocalDateTime.now();
        drone.setCreatedAt(now);
        drone.setUpdatedAt(now);
        
        // 生成AI属性
        generateAiAttributes(drone);
        
        droneMapper.insert(drone);
        return drone;
    }

    @Override
    public Drone getById(Long id) {
        return droneMapper.findById(id);
    }

    @Override
    public List<Drone> list() {
        return droneMapper.findAll();
    }

    @Override
    public PageResult<Drone> page(Integer pageNum, Integer pageSize, String keyword) {
        int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        int offset = (safePageNum - 1) * safePageSize;

        PageResult<Drone> result = new PageResult<>();
        result.setPageNum(safePageNum);
        result.setPageSize(safePageSize);
        result.setRecords(droneMapper.findPageByCondition(keyword, offset, safePageSize));
        result.setTotal(droneMapper.countByCondition(keyword));
        return result;
    }

    @Override
    public Drone update(Long id, Drone drone) {
        Drone existing = droneMapper.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("无人机不存在：" + id);
        }

        // 如果序列号发生变化，需要校验唯一性
        if (!existing.getSerialNo().equals(drone.getSerialNo())) {
            Drone duplicated = droneMapper.findBySerialNo(drone.getSerialNo());
            if (duplicated != null && !duplicated.getId().equals(id)) {
                throw new IllegalArgumentException("序列号已存在：" + drone.getSerialNo());
            }
        }

        drone.setId(id);
        drone.setUpdatedAt(LocalDateTime.now());
        
        // 保留原有AI属性，除非明确要求重新生成
        if (drone.getMaxSpeedKmh() == null) {
            drone.setMaxSpeedKmh(existing.getMaxSpeedKmh());
        }
        if (drone.getFlightTimeMin() == null) {
            drone.setFlightTimeMin(existing.getFlightTimeMin());
        }
        if (drone.getPayloadKg() == null) {
            drone.setPayloadKg(existing.getPayloadKg());
        }
        if (drone.getBatteryMah() == null) {
            drone.setBatteryMah(existing.getBatteryMah());
        }
        if (drone.getAiScore() == null) {
            drone.setAiScore(existing.getAiScore());
        }
        
        droneMapper.update(drone);
        return droneMapper.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        return droneMapper.deleteById(id) > 0;
    }

    @Override
    public Drone regenerateAiAttributes(Long id) {
        Drone drone = droneMapper.findById(id);
        if (drone == null) {
            throw new IllegalArgumentException("无人机不存在：" + id);
        }
        
        generateAiAttributes(drone);
        drone.setUpdatedAt(LocalDateTime.now());
        droneMapper.update(drone);
        
        return drone;
    }

    /**
     * 基于名称、型号、序列号生成稳定的AI属性
     * 
     * @param drone 无人机实体
     */
    private void generateAiAttributes(Drone drone) {
        // 使用名称+型号+序列号作为种子，确保相同输入产生相同结果
        String seed = (drone.getName() + drone.getModel() + drone.getSerialNo()).toLowerCase();
        Random random = new Random(seed.hashCode());
        
        // 最大速度：20-120 km/h
        drone.setMaxSpeedKmh(20 + random.nextInt(101));
        
        // 最大续航：10-180分钟
        drone.setFlightTimeMin(10 + random.nextInt(171));
        
        // 最大载重：0.5-25.0 kg
        drone.setPayloadKg(0.5 + random.nextDouble() * 24.5);
        drone.setPayloadKg(Math.round(drone.getPayloadKg() * 10.0) / 10.0); // 保留1位小数
        
        // 电池容量：1000-15000 mAh
        drone.setBatteryMah(1000 + random.nextInt(14001));
        
        // AI评分：基于其他属性计算，60-95分
        int baseScore = 60;
        int speedBonus = drone.getMaxSpeedKmh() > 80 ? 10 : 5;
        int flightBonus = drone.getFlightTimeMin() > 120 ? 10 : 5;
        int payloadBonus = drone.getPayloadKg() > 15.0 ? 10 : 5;
        int batteryBonus = drone.getBatteryMah() > 10000 ? 10 : 5;
        
        drone.setAiScore(Math.min(95, baseScore + speedBonus + flightBonus + payloadBonus + batteryBonus));
    }
}
