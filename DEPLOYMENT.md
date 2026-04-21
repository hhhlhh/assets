# 固定资产管理系统部署指南

## 🚀 快速开始

### 环境要求
- **JDK**: 1.8 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Node.js**: 16 或更高版本
- **数据库连接**: 192.168.12.209:3306 (用户名: root, 密码: rootroot)

## 📋 部署步骤

### 1. 数据库准备 ✅

数据库已经创建完成，包含以下数据：
- 数据库: `asset_management`
- 表: `fixed_assets` (27个字段)
- 示例数据: 3条资产记录

验证数据库连接：
```bash
mysql -h 192.168.12.209 -u root -prootroot -D asset_management -e "SELECT COUNT(*) FROM fixed_assets;"
```

### 2. 后端部署

#### 2.1 编译项目
```bash
cd /soft/excl/optimized-backend

# 编译项目
mvn clean compile

# 打包项目
mvn clean package
```

#### 2.2 运行后端服务
```bash
# 方法1: 直接运行JAR包
java -jar target/fixed-asset-system-1.0.0.jar

# 方法2: 使用Maven运行
mvn spring-boot:run
```

#### 2.3 验证后端服务
服务启动后，访问以下URL验证：
- 健康检查: http://localhost:8080/api/actuator/health
- API测试: 使用Postman测试API接口

### 3. 前端部署

#### 3.1 安装依赖
```bash
cd /soft/excl/optimized-frontend

# 安装依赖
npm install
```

#### 3.2 启动开发服务器
```bash
# 启动前端开发服务器
npm run dev
```

#### 3.3 构建生产版本
```bash
# 构建生产版本
npm run build

# 预览生产版本
npm run preview
```

### 4. 系统访问

- **前端页面**: http://localhost:3000
- **后端API**: http://localhost:8080/api
- **资产列表API**: http://localhost:8080/api/assets
- **搜索API**: http://localhost:8080/api/assets/search

## 🔧 配置说明

### 后端配置 (application.yml)
```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://192.168.12.209:3306/asset_management
    username: root
    password: rootroot

# 服务器配置
server:
  port: 8080
  servlet:
    context-path: /api

# 跨域配置
cors:
  allowed-origins: "http://localhost:3000"
```

### 前端配置 (vite.config.js)
```javascript
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true
      }
    }
  }
})
```

## 📊 API接口文档

### 资产管理接口

#### 1. 搜索资产 (POST /api/assets/search)
```json
请求体:
{
  "assetName": "",
  "assetCode": "",
  "department": "",
  "assetStatus": "",
  "assetCategory": "",
  "userName": "",
  "location": "",
  "page": 0,
  "size": 10,
  "sortBy": "id",
  "sortDirection": "asc"
}

响应:
{
  "assets": [...],
  "currentPage": 0,
  "totalItems": 3,
  "totalPages": 1,
  "pageSize": 10
}
```

#### 2. 获取所有资产 (GET /api/assets)
获取所有资产的简单列表

#### 3. 根据ID获取资产 (GET /api/assets/{id})
获取指定ID的资产详情

#### 4. 创建资产 (POST /api/assets)
```json
请求体:
{
  "assetCode": "FA004",
  "assetName": "戴尔显示器",
  "assetCategory": "电子设备",
  "department": "技术部",
  "userName": "赵六",
  "location": "办公室A-102",
  "originalValue": 1200.00,
  "netValue": 1100.00,
  "assetStatus": "在用",
  "specification": "Dell U2419H",
  "brand": "戴尔",
  "startDate": "2023-03-01"
}
```

#### 5. 更新资产 (PUT /api/assets/{id})
更新指定ID的资产信息

#### 6. 删除资产 (DELETE /api/assets/{id})
删除指定ID的资产

#### 7. 批量删除 (POST /api/assets/batch-delete)
```json
请求体: [1, 2, 3]
```

#### 8. 导出Excel (GET /api/assets/export/excel)
下载Excel格式的资产数据

#### 9. 导出CSV (GET /api/assets/export/csv)
下载CSV格式的资产数据

#### 10. 获取统计数据 (GET /api/assets/statistics)
```json
响应:
{
  "departmentStats": [...],
  "statusStats": [...],
  "categoryStats": [...],
  "totalOriginalValue": 8500.00,
  "totalNetValue": 7500.00,
  "totalCount": 3
}
```

## 🎯 功能特性

### 核心功能
- ✅ **资产管理**: 完整的CRUD操作
- ✅ **多条件搜索**: 支持27个字段的搜索
- ✅ **分页显示**: 支持大数据量分页
- ✅ **数据导出**: Excel和CSV格式导出
- ✅ **数据验证**: 前端和后端双重验证
- ✅ **错误处理**: 统一的异常处理机制

### 技术特性
- ✅ **Spring Boot 2.7.10**
- ✅ **Vue 3 + Element Plus**
- ✅ **MySQL数据库**
- ✅ **RESTful API设计**
- ✅ **前后端分离架构**
- ✅ **响应式设计**

## 🔍 测试指南

### 1. 后端测试
```bash
# 运行单元测试
mvn test

# API测试 (使用curl)
curl -X GET "http://localhost:8080/api/assets"
curl -X POST "http://localhost:8080/api/assets/search" \
  -H "Content-Type: application/json" \
  -d '{"page": 0, "size": 10}'
```

### 2. 前端测试
```bash
# 启动前端
npm run dev

# 访问 http://localhost:3000
# 测试功能:
# - 查看资产列表
# - 搜索资产
# - 新增资产
# - 编辑资产
# - 删除资产
# - 导出数据
```

## 🚨 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查MySQL服务是否运行
   - 验证数据库连接参数
   - 检查防火墙设置

2. **端口冲突**
   - 修改 application.yml 中的 server.port
   - 修改 vite.config.js 中的端口配置

3. **跨域问题**
   - 检查CORS配置
   - 确保前端和后端端口配置正确

4. **依赖安装失败**
   - 清除Maven缓存: `mvn clean`
   - 重新安装npm依赖: `rm -rf node_modules && npm install`

## 📈 性能优化建议

### 数据库优化
- ✅ 已为常用字段创建索引
- 🔄 考虑添加更多复合索引
- 🔄 定期优化表结构

### 应用优化
- 🔄 添加Redis缓存
- 🔄 实现分页缓存
- 🔄 优化查询SQL

### 前端优化
- ✅ 已实现组件懒加载
- 🔄 添加数据缓存
- 🔄 优化大列表渲染

## 🔄 后续扩展

### 功能扩展
1. **用户权限管理** - 添加登录认证
2. **批量导入** - Excel文件导入功能
3. **资产调拨** - 资产转移流程
4. **维修记录** - 维护历史跟踪
5. **报表统计** - 更丰富的统计图表

### 技术升级
1. **Spring Boot 3.x** - 升级到最新版本
2. **Spring Security** - 添加安全认证
3. **Redis缓存** - 提升性能
4. **Docker部署** - 容器化部署

## 📞 技术支持

如有问题，请检查：
1. 日志文件: `logs/asset-management.log`
2. 控制台输出
3. 浏览器开发者工具
4. 数据库查询日志

---

**版本**: v1.0.0  
**最后更新**: 2024年  
**技术支持**: Claude Code