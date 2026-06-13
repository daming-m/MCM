# 无人机信息管理 — API / 页面路由说明

> 初期版本以 Thymeleaf 页面为主，本文件描述页面路由与表单提交接口。

## 1. 登录
- GET `/login`：登录页
- POST `/login`：提交登录
- POST `/logout`：退出登录

## 2. 无人机管理
- GET `/drones`：列表 + 查询（query 参数：`q`）
- GET `/drones/new`：新增页
- POST `/drones`：创建（基础字段；AI 字段服务端生成）
- GET `/drones/{id}/edit`：编辑页
- POST `/drones/{id}`：更新（可选参数：`regenAi=true`）
- POST `/drones/{id}/delete`：删除

