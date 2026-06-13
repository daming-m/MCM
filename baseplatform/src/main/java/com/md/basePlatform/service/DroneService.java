package com.md.basePlatform.service;

import java.util.List;

import com.md.basePlatform.entity.Drone;
import com.md.basePlatform.entity.PageResult;

/**
 * 无人机业务服务接口
 * 
 * @author system
 */
public interface DroneService {

    /**
     * 创建无人机（自动生成AI属性）
     *
     * @param drone 无人机基础信息
     * @return 创建后的无人机（包含AI属性）
     */
    Drone create(Drone drone);

    /**
     * 根据ID查询无人机
     *
     * @param id 无人机ID
     * @return 无人机实体
     */
    Drone getById(Long id);

    /**
     * 查询所有无人机
     *
     * @return 无人机列表
     */
    List<Drone> list();

    /**
     * 根据关键字分页查询无人机
     *
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @param keyword 搜索关键字（匹配名称、型号、序列号）
     * @return 分页结果
     */
    PageResult<Drone> page(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 更新无人机信息
     *
     * @param id 无人机ID
     * @param drone 无人机信息
     * @return 更新后的无人机
     */
    Drone update(Long id, Drone drone);

    /**
     * 删除无人机
     *
     * @param id 无人机ID
     * @return 删除结果
     */
    boolean delete(Long id);

    /**
     * 重新生成AI属性
     *
     * @param id 无人机ID
     * @return 更新后的无人机
     */
    Drone regenerateAiAttributes(Long id);
}
