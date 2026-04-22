# 固定资产管理系统 📊

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.10-brightgreen.svg" />
  <img src="https://img.shields.io/badge/Vue%203-3.2.47-blue.svg" />
  <img src="https://img.shields.io/badge/MySQL-8.0+-orange.svg" />
  <img src="https://img.shields.io/badge/Element%20Plus-2.2.36-purple.svg" />
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" />
</p>

一个基于 **Spring Boot + Vue 3** 的前后端分离固定资产管理系统，具备完整的资产管理、统计分析、批量导入导出等功能。

## ✨ 核心特性

### 🎯 资产管理
- ✅ **完整的CRUD操作** - 资产的新增、查询、更新、删除
- ✅ **智能搜索** - 多条件组合搜索，支持模糊查询
- ✅ **分页显示** - 大数据量下的流畅浏览体验
- ✅ **批量操作** - 支持批量删除和批量导入

### 📈 数据分析
- 📊 **可视化图表** - 使用ECharts实现丰富的数据可视化
- 📋 **统计报表** - 部门分布、状态分布、类别分布统计
- 💰 **价值分析** - 原值总额、净值总额、折旧率计算
- 📱 **响应式设计** - 完美适配桌面端和移动端

### 🔄 数据导入导出
- 📥 **Excel批量导入** - 支持.xlsx和.xls格式，带数据验证
- 📤 **Excel/CSV导出** - 一键导出当前数据
- ⚠️ **错误报告** - 详细的导入错误信息和处理建议
- 📋 **模板下载** - 提供标准Excel导入模板

### 🛡️ 系统特性
- 🔒 **数据验证** - 前后端双重验证保障数据质量
- ⚡ **异常处理** - 统一的异常处理机制
- 📱 **响应式设计** - 完美支持各种设备
- 🎨 **现代化UI** - 使用Element Plus构建美观界面

## 🏗️ 技术架构

### 前端技术栈
```
Vue 3 + Vite + Element Plus + Vue Router + Pinia + Axios + ECharts
```

### 后端技术栈
```
Spring Boot 2.7 + MySQL 8.0 + Spring Data JPA + Apache POI + Lombok
```

### 架构图
```
┌─────────────────────────────────────────────────────────────┐
│                    Vue 3 前端应用                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   资产管理页面   │  │   统计报表页面  │  │   全局组件   │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP/REST
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                Spring Boot 后端服务                         │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   控制器层       │  │   服务层        │  │  数据访问层  │ │
│  │  - REST API     │  │  - 业务逻辑    │  │  - JPA/JDBC  │ │
│  │  - 异常处理     │  │  - 事务管理    │  │  - 查询优化  │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ JDBC
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     MySQL 数据库                           │
│  ┌───────────────────────────────────────────────────────┐ │
│  │                 fixed_assets 表                       │ │
│  │  - 主键索引、唯一索引、普通索引                      │ │
│  │  - 数据完整性约束                                    │ │
│  │  - 查询性能优化                                      │ │
│  └───────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 快速开始

### 环境要求

- **前端**: Node.js 16+、npm 或 yarn
- **后端**: JDK 8+、Maven 3.6+、MySQL 8.0+

### 安装步骤

#### 1. 数据库设置

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE asset_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构和示例数据
mysql -u root -p asset_management < create_optimized_database.sql
```

#### 2. 后端部署

```bash
cd optimized-backend

# 编译项目
mvn clean package

# 启动应用
java -jar target/fixed-asset-system-1.0.0.jar

# 应用将在 http://localhost:8080 启动
```

#### 3. 前端部署

```bash
cd optimized-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 访问 http://localhost:3000
```

### 默认访问地址

- **前端应用**: http://localhost:3000
- **后端API**: http://localhost:8080/api
- **API文档**: 启动后访问 http://localhost:8080/swagger-ui.html (如配置)

## 📋 功能演示

### 1. 资产管理功能

#### 添加资产
1. 点击"新增资产"按钮
2. 填写完整的资产信息
3. 系统自动验证数据完整性
4. 提交后实时更新列表

#### 搜索和筛选
1. 使用多条件搜索框
2. 支持按名称、编号、部门、状态等筛选
3. 实时显示搜索结果
4. 支持分页浏览

### 2. 统计报表功能

#### 数据可视化
- **饼图**: 显示各部门、状态、类别的分布比例
- **柱状图**: 展示各部门资产价值对比
- **统计卡片**: 显示关键指标（总数、总价值、折旧率）

#### 详细统计
- **部门统计**: 各部门资产数量和占比
- **状态统计**: 各状态资产的分布情况
- **类别统计**: 不同类别资产的统计信息

### 3. Excel导入导出

#### 批量导入
1. 准备标准格式的Excel文件
2. 点击"导入Excel"按钮
3. 选择文件并上传
4. 系统自动验证并导入数据
5. 显示导入结果和错误报告

#### 数据导出
1. 点击"导出Excel"或"导出CSV"
2. 系统自动生成文件
3. 浏览器自动下载
4. 支持当前筛选结果的导出

## 🗂️ 项目结构

### 前端结构
```
optimized-frontend/
├── src/
│   ├── api/                    # API接口封装
│   │   └── asset.js           # 资产相关API
│   ├── components/            # 可复用组件
│   │   └── AssetForm.vue      # 资产表单组件
│   ├── views/                 # 页面组件
│   │   ├── AssetManagement.vue # 资产管理主页面
│   │   └── Statistics.vue     # 统计报表页面
│   ├── router/                # 路由配置
│   │   └── index.js           # 路由定义
│   ├── App.vue               # 应用根组件
│   └── main.js               # 应用入口
├── package.json              # 项目依赖和脚本
├── vite.config.js            # Vite构建配置
└── index.html                # HTML模板
```

### 后端结构
```
optimized-backend/
src/main/java/com/assetmanagement/
├── AssetManagementApplication.java      # Spring Boot启动类
├── controller/                         # REST控制器层
│   └── AssetController.java            # 资产控制器
├── service/                            # 业务逻辑层
│   ├── AssetService.java               # 资产服务
│   ├── ExcelExportService.java         # Excel导出服务
│   └── ExcelImportService.java         # Excel导入服务
├── repository/                         # 数据访问层
│   └── AssetRepository.java            # 资产数据访问
├── entity/                             # JPA实体类
│   └── Asset.java                      # 资产实体
├── dto/                                # 数据传输对象
│   ├── AssetDto.java                   # 资产数据传输对象
│   └── AssetSearchDto.java             # 资产搜索条件
└── exception/                          # 异常处理
    ├── GlobalExceptionHandler.java     # 全局异常处理器
    ├── ResourceNotFoundException.java  # 资源未找到异常
    └── DuplicateResourceException.java # 重复资源异常
```

### 数据库结构
```
optimized-backend/
src/main/resources/
├── application.yml                     # 应用配置文件
└── schema.sql                         # 数据库建表脚本

根目录/
├── create_optimized_database.sql       # 完整的数据库脚本
└── 固定资产台账格式.xlsx             # Excel导入模板
```

## 🔌 API接口文档

### 基础信息
- **基础URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **跨域支持**: 已配置支持所有来源

### 主要接口

| 方法 | 路径 | 描述 | 参数 |
|------|------|------|------|
| GET | `/assets` | 获取所有资产 | 无 |
| POST | `/assets/search` | 搜索资产（分页） | 搜索条件对象 |
| GET | `/assets/{id}` | 根据ID获取资产 | id |
| POST | `/assets` | 创建资产 | 资产对象 |
| PUT | `/assets/{id}` | 更新资产 | id, 资产对象 |
| DELETE | `/assets/{id}` | 删除资产 | id |
| POST | `/assets/batch-delete` | 批量删除 | ID列表 |
| POST | `/assets/import/excel` | Excel导入 | 文件 |
| GET | `/assets/export/excel` | Excel导出 | 无 |
| GET | `/assets/export/csv` | CSV导出 | 无 |
| GET | `/assets/statistics` | 获取统计数据 | 无 |

### 示例请求

```bash
# 创建资产
curl -X POST http://localhost:8080/api/assets \
  -H "Content-Type: application/json" \
  -d '{
    "assetCode": "FA001",
    "assetName": "联想台式电脑",
    "assetCategory": "电子设备",
    "department": "技术部",
    "userName": "张三",
    "location": "办公室A-101",
    "assetStatus": "在用",
    "originalValue": 5000.00,
    "netValue": 4500.00,
    "brand": "联想",
    "specification": "ThinkCentre M720"
  }'

# 搜索资产
curl -X POST http://localhost:8080/api/assets/search \
  -H "Content-Type: application/json" \
  -d '{
    "assetName": "电脑",
    "department": "技术部",
    "page": 0,
    "size": 10,
    "sortBy": "id",
    "sortDirection": "asc"
  }'
```

## 🗄️ 数据库设计

### 核心表结构

```sql
CREATE TABLE fixed_assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    asset_code VARCHAR(50) UNIQUE NOT NULL COMMENT '资产编号',
    asset_name VARCHAR(200) NOT NULL COMMENT '资产名称',
    asset_category VARCHAR(100) COMMENT '资产类别',
    department VARCHAR(100) COMMENT '使用部门',
    user_name VARCHAR(50) COMMENT '使用人',
    location VARCHAR(200) COMMENT '存放地点',
    asset_status VARCHAR(20) DEFAULT '在用' COMMENT '资产状态',
    original_value DECIMAL(12,2) COMMENT '原值',
    net_value DECIMAL(12,2) COMMENT '净值',
    brand VARCHAR(100) COMMENT '品牌',
    specification VARCHAR(200) COMMENT '规格型号',
    start_date DATE COMMENT '开始使用日期',
    responsible_person VARCHAR(50) COMMENT '负责人',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);
```

### 索引设计

```sql
-- 唯一索引：资产编号
CREATE UNIQUE INDEX uk_asset_code ON fixed_assets(asset_code);

-- 普通索引：提高查询性能
CREATE INDEX idx_department ON fixed_assets(department);
CREATE INDEX idx_asset_status ON fixed_assets(asset_status);
CREATE INDEX idx_asset_name ON fixed_assets(asset_name);
CREATE INDEX idx_created_at ON fixed_assets(created_at);
```

## 🚀 部署指南

### 开发环境

#### 前端开发服务器
```bash
cd optimized-frontend
npm install
npm run dev
```

#### 后端开发服务器
```bash
cd optimized-backend
mvn spring-boot:run
```

### 生产环境

#### 前端构建和部署
```bash
# 构建生产版本
npm run build

# 使用Nginx部署
location / {
    root /path/to/dist;
    index index.html;
    try_files $uri $uri/ /index.html;
}

location /api {
    proxy_pass http://localhost:8080/api;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
}
```

#### 后端生产部署
```bash
# 打包应用
mvn clean package -DskipTests

# 运行应用
java -jar target/fixed-asset-system-1.0.0.jar

# 使用systemd服务管理
[Unit]
Description=Asset Management System
After=syslog.target

[Service]
User=appuser
ExecStart=/usr/bin/java -jar /opt/asset-management/fixed-asset-system-1.0.0.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

### Docker部署

```yaml
# docker-compose.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootroot
      MYSQL_DATABASE: asset_management
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./optimized-backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql

  frontend:
    build: ./optimized-frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

## 📚 学习资源

### 快速入门
- 📖 [快速入门指南](QUICK_START.md) - 5分钟快速上手
- 📚 [详细学习指南](LEARNING_GUIDE.md) - 深入理解项目架构
- 📋 [项目总结](PROJECT_SUMMARY.md) - 项目功能和技术总结

### 官方文档
- [Vue 3 官方文档](https://v3.vuejs.org/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Element Plus 组件库](https://element-plus.org/)
- [MySQL 官方文档](https://dev.mysql.com/doc/)

### 开发工具
- [VS Code](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=johnsoncodehk.volar)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [MySQL Workbench](https://www.mysql.com/products/workbench/)

## 🔧 配置说明

### 后端配置 (application.yml)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/asset_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
    username: root
    password: rootroot
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB
```

### 前端配置 (vite.config.js)

```javascript
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

## 🐛 常见问题

### 启动问题

**Q: 端口被占用怎么办？**
A: 修改 `application.yml` 中的 `server.port` 或终止占用进程

**Q: 数据库连接失败？**
A: 检查MySQL服务状态、连接配置、防火墙设置

### 开发问题

**Q: 前端API调用跨域错误？**
A: 检查 `vite.config.js` 中的代理配置

**Q: 文件上传大小限制？**
A: 修改 `application.yml` 中的 `spring.servlet.multipart.max-file-size`

### 部署问题

**Q: 生产环境如何配置？**
A: 使用环境变量或单独的配置文件覆盖默认配置

**Q: 如何监控应用性能？**
A: 集成Spring Boot Actuator或使用APM工具

## 📈 性能优化

### 数据库优化
- ✅ 添加合适的索引
- ✅ 使用分页查询
- ✅ 避免N+1查询问题
- ✅ 使用连接池优化

### 前端优化
- ✅ 组件懒加载
- ✅ 代码分割
- ✅ 资源压缩
- ✅ CDN加速

### 后端优化
- ✅ 缓存常用数据
- ✅ 异步处理耗时操作
- ✅ 连接池配置优化
- ✅ 日志级别调整

## 🔐 安全考虑

### 数据安全
- ✅ 输入验证和过滤
- ✅ SQL注入防护
- ✅ XSS防护
- ✅ CSRF防护

### 访问控制
- ✅ 文件上传限制
- ✅ API访问频率限制
- ✅ 敏感数据脱敏
- ✅ 操作日志记录

## 🚀 后续规划

### 短期计划
- [ ] 用户权限管理系统
- [ ] 操作日志记录
- [ ] 数据备份恢复
- [ ] 系统监控告警

### 中期计划
- [ ] 移动端应用
- [ ] 微信小程序
- [ ] 消息通知系统
- [ ] 工作流引擎

### 长期计划
- [ ] 微服务架构
- [ ] 容器化部署
- [ ] 自动化运维
- [ ] 智能分析

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请提交 Issue 或联系开发团队。

---

<p align="center">
  <b>固定资产管理系统</b> - 让资产管理更简单高效 🎯
</p>