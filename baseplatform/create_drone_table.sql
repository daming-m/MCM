-- 无人机信息管理系统 - MySQL建表SQL
-- 执行前请确保已创建数据库，例如：CREATE DATABASE baseplatform DEFAULT CHARSET utf8mb4;

USE baseplatform;

-- 删除已存在的表（如果需要重新创建）
DROP TABLE IF EXISTS drone;

-- 创建无人机信息表
CREATE TABLE drone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    
    -- 基础字段（人工输入）
    name VARCHAR(100) NOT NULL COMMENT '无人机名称',
    model VARCHAR(100) NOT NULL COMMENT '型号',
    serial_no VARCHAR(100) NOT NULL UNIQUE COMMENT '序列号（唯一）',
    
    -- AI自动生成字段
    max_speed_kmh INT COMMENT '最大速度(km/h)',
    flight_time_min INT COMMENT '最大续航时间(分钟)',
    payload_kg DECIMAL(5,1) COMMENT '最大载重(kg)',
    battery_mah INT COMMENT '电池容量(mAh)',
    ai_score INT COMMENT 'AI评分(0-100)',
    
    -- 系统字段
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_name (name),
    INDEX idx_model (model),
    INDEX idx_serial_no (serial_no),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='无人机信息表';

-- 插入测试数据
INSERT INTO drone (name, model, serial_no, max_speed_kmh, flight_time_min, payload_kg, battery_mah, ai_score) VALUES
('天鹰一号', 'TY-001', 'SN20240001', 85, 120, 15.5, 12000, 88),
('雷鸟二号', 'LB-002', 'SN20240002', 95, 90, 8.2, 8500, 82),
('猎鹰三号', 'LY-003', 'SN20240003', 75, 150, 20.0, 15000, 92);

-- 查询验证
SELECT * FROM drone ORDER BY id;

-- 显示表结构
DESCRIBE drone;