# 无人机信息管理系统 — 技术设计文档

## 1. 技术栈约束（按需求）
- Java EE 8（Java 8 编译目标）
- Servlet 3.0
- Spring Boot 2.2.x / Spring Framework 5.2.x
- Shiro 1.7
- MyBatis 3.5.x
- Hibernate Validator 6.0.x
- Druid 1.2.x
- Thymeleaf 3.0.x + Bootstrap 3.3.7
- 数据库：SQLite（默认）/ MySQL（可切换）

## 2. 架构与分层
- controller：Web 层（Thymeleaf 页面路由 + 表单提交）
- service：业务接口 + 实现分包（`service` 与 `service.impl`）
- repository：MyBatis Mapper 接口 + XML（`repository` 与 `resources/mapper`）
- domain：领域模型（实体/DTO）
- interceptor：请求日志拦截器
- config：Shiro、MyBatis、WebMvc 等配置

依赖方向遵循：controller -> service -> repository/domain。

## 3. 数据库切换方案
- 使用 Spring Profile：
  - `sqlite`：SQLite JDBC + Druid
  - `mysql`：MySQL JDBC + Druid
- 默认启用 `sqlite`

## 4. AI 属性生成方案（初期版本）
- 不引入外部模型依赖
- 通过规则/随机种子（基于 name/model/serialNo）生成一组稳定的“AI 属性”
- 支持在编辑时点击“重新生成”覆盖原属性

## 5. 安全设计（Shiro）
- Form 登录
- 业务路径 `/drones/**` 需要认证
- 登录页 `/login` 放行

