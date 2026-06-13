package com.md.basePlatform.entity;

import java.time.LocalDateTime;

/**
 * 无人机实体类
 * 
 * @author system
 */
public class Drone {

    private Long id;
    
    // 基础字段（人工输入）
    private String name;        // 名称（必填）
    private String model;       // 型号（必填）
    private String serialNo;    // 序列号（必填，唯一）
    
    // AI 自动生成字段
    private Integer maxSpeedKmh;    // 最大速度
    private Integer flightTimeMin;  // 最大续航
    private Double payloadKg;       // 最大载重
    private Integer batteryMah;     // 电池容量
    private Integer aiScore;        // AI 评分（0-100）
    
    // 系统字段
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getMaxSpeedKmh() {
        return maxSpeedKmh;
    }

    public void setMaxSpeedKmh(Integer maxSpeedKmh) {
        this.maxSpeedKmh = maxSpeedKmh;
    }

    public Integer getFlightTimeMin() {
        return flightTimeMin;
    }

    public void setFlightTimeMin(Integer flightTimeMin) {
        this.flightTimeMin = flightTimeMin;
    }

    public Double getPayloadKg() {
        return payloadKg;
    }

    public void setPayloadKg(Double payloadKg) {
        this.payloadKg = payloadKg;
    }

    public Integer getBatteryMah() {
        return batteryMah;
    }

    public void setBatteryMah(Integer batteryMah) {
        this.batteryMah = batteryMah;
    }

    public Integer getAiScore() {
        return aiScore;
    }

    public void setAiScore(Integer aiScore) {
        this.aiScore = aiScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
