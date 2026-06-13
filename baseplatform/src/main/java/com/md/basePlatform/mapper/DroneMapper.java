package com.md.basePlatform.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.md.basePlatform.entity.Drone;

/**
 * 无人机数据访问接口
 * 
 * @author system
 */
public interface DroneMapper {

    /**
     * 插入无人机记录
     *
     * @param drone 无人机实体
     * @return 影响行数
     */
    int insert(Drone drone);

    /**
     * 根据ID查询无人机
     *
     * @param id 无人机ID
     * @return 无人机实体
     */
    Drone findById(Long id);

    /**
     * 查询所有无人机
     *
     * @return 无人机列表
     */
    List<Drone> findAll();

    /**
     * 根据条件分页查询无人机
     *
     * @param keyword 关键字（匹配名称、型号、序列号）
     * @param offset 偏移量
     * @param pageSize 页面大小
     * @return 无人机分页记录
     */
    List<Drone> findPageByCondition(@Param("keyword") String keyword,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize);

    /**
     * 根据条件统计无人机数量
     *
     * @param keyword 关键字（匹配名称、型号、序列号）
     * @return 总数量
     */
    Long countByCondition(@Param("keyword") String keyword);

    /**
     * 更新无人机信息
     *
     * @param drone 无人机实体
     * @return 影响行数
     */
    int update(Drone drone);

    /**
     * 根据ID删除无人机
     *
     * @param id 无人机ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据序列号查询无人机（用于唯一性校验）
     *
     * @param serialNo 序列号
     * @return 无人机实体
     */
    Drone findBySerialNo(@Param("serialNo") String serialNo);
}
