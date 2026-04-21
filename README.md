# 固定资产管理系统

基于 Spring Boot + Vue + MySQL 的前后端分离固定资产管理系统

## 技术栈

### 后端
- **Spring Boot 2.7.10**
- **Spring Data JPA**
- **MySQL 8.0+**
- **Maven**
- **Java 8+**

### 前端
- **Vue 3**
- **Element Plus**
- **Pinia**
- **Vue Router**
- **Axios**
- **Vite**

## 快速开始

### 1. 数据库配置

#### 创建数据库
```sql
-- 执行 create_database.sql 文件中的SQL语句
-- 数据库连接信息：
-- 主机: 192.168.12.209
-- 用户名: root
-- 密码: rootroot
-- 数据库: asset_management
```

#### 手动创建数据库命令
```bash
mysql -h 192.168.12.209 -u root -prootroot < create_database.sql
```

### 2. 后端部署

#### 环境要求
- JDK 8+
- Maven 3.6+

#### 编译和运行
```bash
cd asset-management-backend

# 编译项目
mvn clean package

# 运行项目
java -jar target/asset-management-backend-1.0.0.jar

# 或者使用Maven运行
mvn spring-boot:run
```

#### 配置文件说明
修改 `src/main/resources/application.yml`：
- 数据库连接信息
- 服务器端口（默认8080）
- 跨域配置

### 3. 前端部署

#### 环境要求
- Node.js 16+
- npm 或 yarn

#### 安装和运行
```bash
cd asset-management-frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产环境构建
npm run build
```

#### 配置说明
修改 `vite.config.js`：
- 代理配置（指向后端API地址）
- 端口配置（默认3000）

## 项目结构

### 后端结构
```
asset-management-backend/
├── src/main/java/
│   └── com/assetmanagement/
│       ├── AssetManagementApplication.java    # 主应用类
│       ├── controller/                        # 控制器层
│       │   └── FixedAssetController.java      # 资产控制器
│       ├── service/                           # 服务层
│       │   └── FixedAssetService.java         # 资产服务
│       ├── repository/                       # 数据访问层
│       │   └── FixedAssetRepository.java     # 资产仓库
│       └── entity/                           # 实体层
│           └── FixedAsset.java               # 资产实体
├── src/main/resources/
│   ├── application.yml                       # 配置文件
│   └── data.sql                              # 初始化数据（可选）
└── pom.xml                                   # Maven配置
```

### 前端结构
```
asset-management-frontend/
├── src/
│   ├── assets/                               # 静态资源
│   ├── components/                           # 公共组件
│   ├── views/                                # 页面组件
│   │   ├── AssetList.vue                     # 资产列表
│   │   ├── AddAsset.vue                      # 新增资产
│   │   ├── EditAsset.vue                     # 编辑资产
│   │   ├── Statistics.vue                    # 统计报表
│   │   └── Home.vue                          # 首页
│   ├── stores/                               # 状态管理
│   │   └── asset.js                          # 资产状态管理
│   ├── router/                               # 路由配置
│   │   └── index.js                          # 路由定义
│   ├── App.vue                               # 根组件
│   └── main.js                               # 入口文件
├── package.json                              # npm配置
└── vite.config.js                           # Vite配置
```

## API接口文档

### 基础URL
```
http://localhost:8080/api
```

### 资产相关接口

#### 获取资产列表（分页）
```
GET /assets?page=0&size=10&sortBy=id
```

#### 搜索资产
```
GET /assets/search?assetName=&department=&assetStatus=&page=0&size=10
```

#### 创建资产
```
POST /assets
Content-Type: application/json

{
  "assetCode": "FA001",
  "assetName": "联想台式电脑",
  "assetCategory": "电子设备",
  "department": "技术部",
  "userName": "张三",
  "location": "办公室A-101",
  "assetStatus": "在用",
  "originalValue": 5000.00,
  "netValue": 4500.00,
  "startDate": "2023-01-15",
  "brand": "联想",
  "specification": "ThinkCentre M720"
}
```

#### 更新资产
```
PUT /assets/{id}
Content-Type: application/json
```

#### 删除资产
```
DELETE /assets/{id}
```

#### 获取统计数据
```
GET /assets/statistics
```

## 功能特性

### 资产管理
- ✅ 资产新增、编辑、删除
- ✅ 资产列表分页显示
- ✅ 多条件搜索和筛选
- ✅ 资产状态管理
- ✅ 批量操作支持

### 统计报表
- ✅ 部门资产统计
- ✅ 资产状态统计
- ✅ 资产价值统计
- ✅ 图表展示

### 系统特性
- ✅ 响应式设计
- ✅ 数据验证
- ✅ 错误处理
- ✅ 加载状态
- ✅ 消息提示

## 开发说明

### 数据库表结构
详细的表结构请参考 `create_database.sql` 文件

### 字段说明
- **asset_code**: 资产编号（唯一）
- **asset_name**: 资产名称
- **asset_category**: 资产类别
- **department**: 使用部门
- **user_name**: 使用人
- **asset_status**: 资产状态
- **original_value**: 原值
- **net_value**: 净值
- **location**: 存放地点

### 状态管理
- 在用：资产正常使用中
- 闲置：资产暂时未使用
- 维修中：资产正在维修
- 报废：资产已报废

## 常见问题

### 1. 数据库连接失败
检查 `application.yml` 中的数据库连接配置是否正确

### 2. 跨域问题
确保前端和后端的跨域配置正确

### 3. 端口冲突
修改 `application.yml` 中的 `server.port` 或 `vite.config.js` 中的端口配置

## 后续优化建议

### 功能扩展
- [ ] 用户权限管理
- [ ] 资产导入导出（Excel）
- [ ] 资产调拨管理
- [ ] 维修记录管理
- [ ] 报废处理流程
- [ ] 资产盘点功能

### 技术优化
- [ ] 添加缓存（Redis）
- [ ] 文件上传功能
- [ ] 日志记录
- [ ] 数据备份
- [ ] 性能监控

## 许可证

MIT License

## 联系方式

如有问题或建议，请提交 Issue 或联系开发团队。