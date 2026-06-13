package com.md.basePlatform.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.md.basePlatform.entity.ApiResponse;
import com.md.basePlatform.entity.Drone;
import com.md.basePlatform.entity.PageResult;
import com.md.basePlatform.service.DroneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 无人机管理控制器
 * 
 * @author system
 */
@RestController
@RequestMapping("/api/drone")
@Tag(name = "Drone API", description = "无人机信息管理接口")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping
    @Operation(summary = "新增无人机")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Drone> create(@RequestBody Drone drone) {
        try {
            return ApiResponse.success(droneService.create(drone));
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询无人机")
    public ApiResponse<Drone> getById(@PathVariable("id") Long id) {
        Drone drone = droneService.getById(id);
        if (drone == null) {
            return ApiResponse.failure("无人机不存在");
        }
        return ApiResponse.success(drone);
    }

    @GetMapping("/list")
    @Operation(summary = "查询无人机列表")
    public ApiResponse<List<Drone>> list() {
        return ApiResponse.success(droneService.list());
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询无人机列表")
    public ApiResponse<PageResult<Drone>> page(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ApiResponse.success(droneService.page(pageNum, pageSize, keyword));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新无人机")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Drone> update(@PathVariable("id") Long id, @RequestBody Drone drone) {
        try {
            return ApiResponse.success(droneService.update(id, drone));
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除无人机")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        boolean deleted = droneService.delete(id);
        if (!deleted) {
            return ApiResponse.failure("无人机不存在");
        }
        return ApiResponse.successMessage("删除成功");
    }

    @PostMapping("/{id}/regenerate-ai")
    @Operation(summary = "重新生成AI属性")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Drone> regenerateAiAttributes(@PathVariable("id") Long id) {
        try {
            return ApiResponse.success(droneService.regenerateAiAttributes(id));
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }
}
