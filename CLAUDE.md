# 固定资产管理系统 - Claude Code开发规范

## 项目结构
```
fixed-asset-system/
├── src/main/java/com/fixedasset/system/    # Java源代码
│   ├── controller/                         # REST控制器
│   ├── service/                          # 业务逻辑层
│   ├── repository/                       # 数据访问层
│   ├── entity/                           # JPA实体类
│   ├── dto/                              # 数据传输对象
│   ├── exception/                        # 异常处理
│   └── util/                             # 工具类
├── src/main/resources/                     # 资源文件
│   ├── application.yml                   # 配置文件
│   ├── templates/                        # Thymeleaf模板
│   ├── static/                          # 静态资源
│   └── public/                          # 公共资源
├── src/test/java/com/fixedasset/system/  # 测试代码
├── target/                               # 编译输出目录
├── README.md                            # 项目文档
├── pom.xml                              # Maven配置
├── Dockerfile                           # Docker镜像配置
├── docker-compose.yml                   # Docker编排
└── startup.sh                          # 启动脚本
```

## 代码规范

### Java编码规范
- **命名规范**:
  - 类名: PascalCase (如 `AssetController`)
  - 方法名: camelCase (如 `getAssets()`)
  - 变量名: camelCase (如 `assetService`)
  - 常量: UPPER_SNAKE_CASE (如 `ASSET_STATUS_IN_USE`)

- **包结构**:
  ```
  com.fixedasset.system.controller
  com.fixedasset.system.service
  com.fixedasset.system.repository
  com.fixedasset.system.entity
  com.fixedasset.system.dto
  com.fixedasset.system.exception
  com.fixedasset.system.util
  ```

### 数据库规范
- **表名**: 使用复数形式 (如 `assets`, `users`)
- **字段名**: 小写字母加下划线 (如 `serial_number`)
- **主键**: `id` BIGINT AUTO_INCREMENT
- **时间字段**: `created_at`, `updated_at`
- **外键约束**: 使用`ON DELETE CASCADE`

### REST API设计
- **URL格式**: `/api/{resource}/{id?}`
- **HTTP方法**:
  - GET: 查询操作
  - POST: 创建操作
  - PUT: 更新操作
  - DELETE: 删除操作
  - PATCH: 部分更新
- **响应格式**:
  ```json
  {
    "data": {},
    "message": "成功信息",
    "timestamp": "2024-01-01T00:00:00Z"
  }
  ```

## 安全规范

### 认证授权
- JWT Token有效期: 24小时
- 密码加密: BCrypt
- CORS配置: 允许所有来源（生产环境需限制）
- CSRF保护: 禁用（API模式）

### 输入验证
- 必填字段验证
- 邮箱格式验证
- 序列号唯一性检查
- SQL注入防护: Spring Data JPA自动处理

## 事务管理
- 服务层方法默认开启事务 (`@Transactional`)
- 避免在Repository层使用事务
- 长事务需手动管理

## 性能优化
- 分页查询: 默认每页10条记录
- 缓存策略: Redis缓存常用数据
- 索引设计: 在常用查询字段上建立索引
- 批量操作: 支持批量导入导出

## 日志规范
- 日志级别: DEBUG (开发), INFO (生产)
- 日志格式: JSON格式
- 敏感信息: 不记录密码、Token等敏感信息

## Git工作流

### 分支策略
- `main`: 生产环境代码
- `develop`: 开发分支
- `feature/*`: 功能开发分支
- `hotfix/*`: 紧急修复分支

### 提交规范
```
type(scope): subject

body

footer
```

**type类型**:
- feat: 新功能
- fix: bug修复
- docs: 文档变更
- style: 代码格式
- refactor: 重构
- test: 测试相关
- chore: 构建过程或辅助工具的变动

**scope范围**:
- controller: 控制器层
- service: 服务层
- repository: 数据访问层
- entity: 实体类
- dto: 数据传输对象

**示例**:
```
feat(controller): add asset search functionality

Adds search endpoint for assets by name, model, or serial number.
Supports pagination and filtering.

Closes #123
```

## 部署规范

### 本地开发
```bash
# 启动MySQL和Redis
docker-compose up -d mysql redis

# 运行应用
mvn spring-boot:run

# 访问地址
http://localhost:8080
```

### Docker部署
```bash
# 构建镜像
docker build -t fixed-asset-system .

# 运行容器
docker run -p 8080:8080 fixed-asset-system

# 或使用docker-compose
docker-compose up -d
```

### 生产环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- 最小内存: 2GB
- 最小磁盘空间: 10GB

## 监控与运维

### 健康检查
- `/actuator/health`: 应用健康状态
- `/actuator/metrics`: 系统指标
- `/actuator/info`: 应用信息

### 日志收集
- 应用日志: 输出到控制台和文件
- 错误日志: 单独文件存储
- 访问日志: Nginx反向代理层记录

## 常见问题排查

### 数据库连接问题
1. 检查MySQL服务是否运行
2. 验证连接字符串配置
3. 检查防火墙设置
4. 查看数据库用户权限

### 应用启动失败
1. 检查端口占用情况 (8080)
2. 查看application.yml配置
3. 验证依赖是否正确下载
4. 检查日志文件定位错误

### 性能问题
1. 检查SQL执行计划
2. 分析慢查询日志
3. 检查索引使用情况
4. 监控JVM内存使用

## 联系方式
- 技术问题: tech-support@example.com
- 紧急故障: emergency@example.com
- 文档反馈: docs@example.com