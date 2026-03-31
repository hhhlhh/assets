# 固定资产管理系统

基于Spring Boot的现代化固定资产管理解决方案，支持台式机、笔记本、打印机等各类设备的管理。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.3
- **语言**: Java 17
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **缓存**: Redis
- **消息队列**: RabbitMQ (可选)

### 前端
- **Web框架**: Thymeleaf
- **UI组件**: Bootstrap 5
- **图标库**: Font Awesome 6
- **图表库**: Chart.js (预留)

## 功能特性

### 核心功能
1. **资产管理**
   - 资产登记与信息管理
   - 多类型设备支持（台式机、笔记本、打印机等）
   - 资产状态跟踪（在用、闲置、维修、报废）
   - 序列号唯一性验证

2. **用户权限管理**
   - 基于角色的访问控制（RBAC）
   - 管理员和普通用户角色
   - JWT认证机制
   - 密码加密存储

3. **维护管理**
   - 维修记录追踪
   - 保养计划管理
   - 成本统计分析
   - 维护历史查询

4. **统计报表**
   - 资产数量统计
   - 按类型和状态分类统计
   - 维护成本分析
   - 导出功能支持

### 系统架构
```
com.fixedasset.system/
├── FixedAssetSystemApplication.java        # 应用入口
├── config/                                 # 配置类
│   ├── SecurityConfig.java                # 安全配置
│   ├── JwtAuthenticationFilter.java       # JWT过滤器
│   └── WebConfig.java                     # Web配置
├── controller/                             # REST控制器层
│   ├── AuthController.java               # 认证控制器
│   ├── UserController.java               # 用户管理
│   ├── AssetController.java              # 资产管理
│   └── MaintenanceRecordController.java  # 维护记录
├── service/                              # 业务逻辑层
│   ├── UserService.java                  # 用户服务
│   ├── AssetService.java                 # 资产服务
│   └── MaintenanceRecordService.java     # 维护记录服务
├── repository/                           # 数据访问层
│   ├── UserRepository.java               # 用户仓库
│   ├── AssetRepository.java              # 资产仓库
│   └── MaintenanceRecordRepository.java  # 维护记录仓库
├── entity/                               # 实体类
│   ├── User.java                         # 用户实体
│   ├── Asset.java                        # 资产实体
│   ├── MaintenanceRecord.java            # 维护记录实体
│   └── 枚举类                            # 各种枚举
├── dto/                                  # 数据传输对象
│   ├── UserDto.java                      # 用户DTO
│   ├── AssetDto.java                     # 资产DTO
│   └── MaintenanceRecordDto.java         # 维护记录DTO
├── exception/                            # 异常处理
│   ├── GlobalExceptionHandler.java       # 全局异常处理器
│   └── 自定义异常类                      # 业务异常
└── util/                                 # 工具类
    └── JwtUtil.java                      # JWT工具类
```

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis (可选)
- RabbitMQ (可选)

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd fixed-asset-system
   ```

2. **创建数据库（可选）**
   ```sql
   CREATE DATABASE fixed_asset_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

4. **访问系统**
   - 应用地址: http://localhost:8080
   - 默认管理员账户: admin / admin123

4. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

5. **访问系统**
   - 应用地址: http://localhost:8080
   - API文档: http://localhost:8080/swagger-ui.html (待添加)

### 默认账户
- **用户名**: admin
- **密码**: admin123

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/profile` - 获取用户信息

### 用户管理
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 获取用户详情
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 资产管理
- `GET /api/assets` - 获取资产列表（分页）
- `GET /api/assets/{id}` - 获取资产详情
- `POST /api/assets` - 创建资产
- `PUT /api/assets/{id}` - 更新资产
- `DELETE /api/assets/{id}` - 删除资产
- `GET /api/assets/search` - 搜索资产
- `GET /api/assets/stats/*` - 统计信息

### 维护记录
- `GET /api/maintenance` - 获取维护记录
- `POST /api/maintenance` - 创建维护记录
- `PUT /api/maintenance/{id}` - 更新维护记录
- `DELETE /api/maintenance/{id}` - 删除维护记录

## 数据库设计

### 主要表结构

#### users 表
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') DEFAULT 'USER',
    department VARCHAR(50),
    phone VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### assets 表
```sql
CREATE TABLE assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type ENUM('DESKTOP', 'LAPTOP', 'PRINTER', 'MONITOR', 'SERVER', 'NETWORK_DEVICE', 'FURNITURE', 'OTHER') NOT NULL,
    model VARCHAR(100),
    serial_number VARCHAR(100) UNIQUE,
    purchase_date DATE,
    price DECIMAL(10,2),
    status ENUM('IN_USE', 'IDLE', 'MAINTENANCE', 'SCRAPPED') DEFAULT 'IN_USE',
    location VARCHAR(100),
    description TEXT,
    assigned_to VARCHAR(50),
    department VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### maintenance_records 表
```sql
CREATE TABLE maintenance_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    asset_id BIGINT NOT NULL,
    maintenance_date DATETIME NOT NULL,
    type ENUM('REPAIR', 'MAINTENANCE', 'UPGRADE') NOT NULL,
    description TEXT,
    cost DECIMAL(10,2),
    technician VARCHAR(50),
    location VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE
);
```

## 开发规范

### 代码规范
- 遵循Google Java风格指南
- 使用Lombok简化代码
- 采用DTO模式进行数据传输
- 统一异常处理机制

### 命名规范
- 实体类：PascalCase（如 `Asset`, `User`）
- 方法名：camelCase（如 `getAssets()`, `createAsset()`）
- 变量名：camelCase（如 `assetService`, `userRepository`）
- 常量：UPPER_SNAKE_CASE（如 `ASSET_STATUS_IN_USE`）

### 事务管理
- 使用 `@Transactional` 注解
- 服务层方法默认开启事务
- 避免在Repository层使用事务

## 扩展建议

### 功能扩展
1. **RFID集成** - 硬件条码扫描
2. **移动端支持** - React Native应用
3. **数据分析** - 资产利用率分析
4. **API集成** - 与企业系统集成
5. **审批流程** - 资产申请和调拨审批

### 性能优化
1. **缓存策略** - Redis缓存常用数据
2. **分页优化** - 大数据集的分页查询
3. **索引优化** - 数据库表索引设计
4. **异步处理** - 邮件通知和报表生成

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱: support@example.com
- 微信: FixedAssetSupport