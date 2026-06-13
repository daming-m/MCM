package com.example.uavmanager.repository;

import com.example.uavmanager.domain.Drone;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DroneMapper {

    List<Drone> selectAll(@Param("q") String q);

    Optional<Drone> selectById(@Param("id") long id);

    int insert(Drone drone);

    int update(Drone drone);

    int deleteById(@Param("id") long id);

    Optional<Long> selectIdBySerialNo(@Param("serialNo") String serialNo);
}

