package com.md.basePlatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Root API", description = "根路径接口")
public class RootController {

    @GetMapping("/")
    @Operation(summary = "项目启动探活")
    public String home() {
        return "无人机管理项目启动成功！";
    }
}
