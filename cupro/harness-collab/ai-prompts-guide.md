# 无人机管理系统 — Claude Code AI 提示词指南

**创建时间**：2026-05-29
**最后更新**：2026-05-29
**用途**：指导使用 Claude Code 完成无人机管理项目的全流程开发

---

## 1. 项目初始化提示词

使用以下提示词初始化 Spring Boot + Vue 3 项目骨架：

```
创建一个无人机信息管理系统，技术栈：
- 后端：Spring Boot 2.2.13 + MyBatis 3.5.x + Apache Shiro 1.7 + Alibaba Druid 1.2.x
- 前端：Vue 3 + Element Plus + Bootstrap 4 + Vite
- 数据库：MySQL 8.0（默认），支持切换 SQLite

项目结构按照 Spring Boot 四层架构分层：
- controller：REST API 控制器
- service：业务服务接口 + service/impl：实现类
- repository：数据访问接口 + repository/impl：实现类
- domain：领域实体

要求：
1. 使用构造器注入，不用 @Autowired
2. 拦截器独立包，打印请求日志
3. 统一 ApiResponse 响应格式
4. application.yml 支持 mysql 和 sqlite 双 profile
```

---

## 2. 无人机 CRUD 功能提示词

```
基于已有的项目骨架，实现无人机信息管理的完整 CRUD：

1. Drone 实体：id, serialNumber, model, batteryPercent, maxFlightMinutes, status
2. DroneUpsertRequest：请求 DTO，所有字段可选，带 @Valid 校验注解
3. DroneController：REST API，路径 /api/drones
4. DroneService 接口 + DroneServiceImpl 实现：
   - createDrone：新增时用 fillByAiStrategy 自动补齐空字段
   - updateDrone：更新时也补齐空字段
   - deleteDrone：删除后自动 decrementIdsAfter 保持 ID 连续
5. DroneMapper.xml：MyBatis SQL 映射文件
6. 生成对应的单元测试类
```

---

## 3. 添加 Shiro 安全提示词

```
为无人机管理系统添加 Apache Shiro 1.7 安全认证：

1. 在 pom.xml 添加 shiro-spring-boot-starter 1.7.1
2. 创建 ShiroRealm：支持 admin/user 两种角色
   - admin：拥有所有权限
   - user：仅有 drone:view 权限
3. 创建 ShiroConfig：配置过滤器链
4. 开发阶段 /api/** 放行，正式上线时改为 authc 认证
```

---

## 4. 添加 Bootstrap 4 提示词

```
前端项目已使用 Element Plus，需要同时引入 Bootstrap 4：

1. npm install bootstrap@4.6.2 --save
2. 在 main.js 中 import 'bootstrap/dist/css/bootstrap.min.css'
3. Bootstrap 提供栅格系统和工具类，Element Plus 负责复杂组件
```

---

## 5. 生成项目文档提示词

```
为无人机管理系统生成完整的项目文档：

1. 需求文档（01-product-specs/drone-management-spec.md）：
   - 功能背景、目标、用户故事、非功能性需求

2. 设计文档（02-design-docs/drone-management-design.md）：
   - 架构设计（Mermaid 组件关系图）、数据流向、API 接口定义
   - 数据模型、技术选型、测试策略

3. API 文档（04-api-docs/drone-api.md）：
   - 所有 REST 端点的详细文档，包含请求/响应示例

4. 更新 func.md 功能资产表
```

---

## 6. 验证与测试提示词

```
对无人机管理系统进行质量验证：

1. 执行 mvn clean verify -Pharness-new 检查代码质量
2. 确保 Checkstyle 检查通过
3. 确保单元测试覆盖率 >= 80%
4. 修复所有编译警告和测试失败
5. 验证前后端联调：启动后端 8080 + 前端 5173
```

---

## 提示词使用说明

### 顺序建议

按编号顺序执行：项目初始化 → CRUD → 安全 → 前端样式 → 文档 → 验证

### 注意事项

1. 每个提示词执行后，检查生成结果是否符合预期
2. 如 Claude Code 生成代码不符合规范（如使用 @Autowired），在下一轮提示中指出修正
3. 分层架构是强制约束，不允许跨层调用
4. 数据库切换通过 Spring Profile `--spring.profiles.active=mysql|sqlite` 实现
