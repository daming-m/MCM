# uav-manager

无人机信息管理系统（初期版本）。

## 运行
### SQLite（默认）
在 `uav-manager` 目录执行：

```bash
mvn spring-boot:run
```

访问：`http://localhost:8080/`

默认账号：`admin / admin`

SQLite 数据文件：`./data/uav.db`

### MySQL
确保已创建数据库 `uav_manager`，然后：

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

> MySQL 连接信息在 `src/main/resources/application-mysql.yml` 中调整。

