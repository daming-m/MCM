# 无人机管理 API 文档

**关联设计文档**：[无人机管理设计文档](../02-design-docs/drone-management-design.md)
**文档版本**：v1.0
**创建时间**：2026-05-29
**最后更新**：2026-05-29
**负责人**：@dev

---

## 概述

- **基础路径**：`/api/drones`
- **API 版本**：v1
- **认证方式**：Apache Shiro 认证（开发阶段临时放行）
- **内容类型**：`application/json`
- **字符编码**：UTF-8

---

## 接口列表

| 方法 | 路径 | 描述 | 认证 | 引入版本 |
|------|------|------|------|---------|
| GET | `/api/drones` | 查询全部无人机列表 | 开发阶段放行 | v1.0 |
| GET | `/api/drones/{id}` | 根据 ID 查询无人机 | 开发阶段放行 | v1.0 |
| POST | `/api/drones` | 新增无人机 | 开发阶段放行 | v1.0 |
| PUT | `/api/drones/{id}` | 更新无人机 | 开发阶段放行 | v1.0 |
| DELETE | `/api/drones/{id}` | 删除无人机 | 开发阶段放行 | v1.0 |

---

## 接口详情

### GET /api/drones

**描述**：查询全部无人机列表，按 ID 升序排列。

**响应示例（200 OK）**：

```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "id": 1,
      "serialNumber": "SN-1704067200000",
      "model": "Mavic-Air",
      "batteryPercent": 85,
      "maxFlightMinutes": 35,
      "status": "IDLE"
    }
  ]
}
```

**响应字段说明**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 无人机主键 ID |
| serialNumber | String | 序列号，如 SN-1704067200000 |
| model | String | 型号，如 Mavic-Air |
| batteryPercent | Integer | 电池百分比 0-100 |
| maxFlightMinutes | Integer | 最大飞行时长（分钟） |
| status | String | 状态：IDLE / CHARGING / IN_MAINTENANCE |

---

### GET /api/drones/{id}

**描述**：根据 ID 查询单个无人机。

**请求参数**：

| 参数名 | 位置 | 类型 | 必填 | 描述 |
|--------|------|------|------|------|
| id | path | Long | 是 | 无人机主键 ID |

**响应示例（200 OK）**：

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "id": 1,
    "serialNumber": "SN-1704067200000",
    "model": "Mavic-Air",
    "batteryPercent": 85,
    "maxFlightMinutes": 35,
    "status": "IDLE"
  }
}
```

**错误响应示例（400 Bad Request）**：

```json
{
  "success": false,
  "message": "无人机不存在，id=999",
  "data": null
}
```

---

### POST /api/drones

**描述**：新增无人机。未填写的字段由后端 AI 策略自动生成默认值。

**请求体**：

```json
{
  "serialNumber": "SN-1704067200000",
  "model": "Phantom-4",
  "batteryPercent": 90,
  "maxFlightMinutes": 40,
  "status": "IDLE"
}
```

**请求体字段说明**：

| 字段名 | 类型 | 必填 | 校验规则 | 默认值策略 |
|--------|------|------|----------|------------|
| serialNumber | String | 否 | 长度 ≤ 64 | "SN-{当前时间毫秒}" |
| model | String | 否 | 长度 ≤ 64 | 随机选取：Mavic-Air/Inspire-2/Phantom-4/Mini-3 |
| batteryPercent | Integer | 否 | 0-100 | 随机 60-100 |
| maxFlightMinutes | Integer | 否 | 1-300 | 随机 20-50 |
| status | String | 否 | 长度 ≤ 32 | 随机：IDLE/CHARGING/IN_MAINTENANCE |

**响应示例（200 OK）**：

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "id": 3,
    "serialNumber": "SN-1704067200000",
    "model": "Phantom-4",
    "batteryPercent": 90,
    "maxFlightMinutes": 40,
    "status": "IDLE"
  }
}
```

---

### PUT /api/drones/{id}

**描述**：更新指定 ID 的无人机信息。未填写的字段由后端补齐默认值。

**请求参数**：

| 参数名 | 位置 | 类型 | 必填 | 描述 |
|--------|------|------|------|------|
| id | path | Long | 是 | 无人机主键 ID |

**请求体**：

```json
{
  "model": "Inspire-2",
  "status": "CHARGING"
}
```

**响应示例（200 OK）**：

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "id": 3,
    "serialNumber": "SN-1704067200000",
    "model": "Inspire-2",
    "batteryPercent": 75,
    "maxFlightMinutes": 30,
    "status": "CHARGING"
  }
}
```

---

### DELETE /api/drones/{id}

**描述**：删除指定 ID 的无人机。删除后自动将后续记录的 ID 前移填补空缺。

**请求参数**：

| 参数名 | 位置 | 类型 | 必填 | 描述 |
|--------|------|------|------|------|
| id | path | Long | 是 | 无人机主键 ID |

**响应示例（200 OK）**：

```json
{
  "success": true,
  "message": "OK",
  "data": null
}
```

---

## 错误码汇总

| HTTP 状态码 | 说明 | 触发场景 |
|------------|------|----------|
| 200 | 成功 | 请求处理成功 |
| 400 | 请求参数错误 | 参数校验失败 / 业务异常（如 ID 不存在） |

### 校验失败响应示例（400 Bad Request）

```json
{
  "success": false,
  "message": "batteryPercent 不能大于100",
  "data": null
}
```

---

## 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0 | 2026-05-29 | 初始版本，包含 CRUD 全部接口 | @dev |
