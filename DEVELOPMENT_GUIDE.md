# 固定资产管理系统 - 开发者指南

## 🎯 项目概览

本系统采用现代化的前后端分离架构，基于 Spring Boot + Vue 3 技术栈构建。系统提供了完整的固定资产管理功能，包括资产的增删改查、批量操作、Excel导入导出以及统计报表等核心功能。

## 📁 详细项目结构

### 后端项目结构 (optimized-backend/)

```
src/main/java/com/assetmanagement/
├── AssetManagementApplication.java           # 应用启动类
│
├── config/                                  # 配置类
│   └── WebConfig.java                      # Web配置（跨域、拦截器等）
│
├── controller/                              # 控制器层
│   └── AssetController.java                 # 资产控制器（RESTful API）
│
├── dto/                                     # 数据传输对象
│   └── AssetSearchDto.java                  # 搜索参数DTO
│
├── entity/                                  # 实体层
│   └── Asset.java                          # 资产实体类（JPA注解）
│
├── exception/                               # 异常处理
│   ├── GlobalExceptionHandler.java         # 全局异常处理器
│   ├── ResourceNotFoundException.java      # 资源未找到异常
│   ├── DuplicateResourceException.java     # 重复资源异常
│   └── ErrorResponse.java                  # 错误响应格式
│
├── repository/                              # 数据访问层
│   └── AssetRepository.java                # 资产仓库（JPA Repository）
│
├── service/                                 # 服务层
│   ├── AssetService.java                   # 资产业务服务
│   ├── ExcelExportService.java             # Excel导出服务
│   └── ExcelImportService.java             # Excel导入服务
│
└── util/                                   # 工具类
    └── ExcelUtil.java                      # Excel处理工具
```

### 前端项目结构 (optimized-frontend/)

```
src/
├── api/                                    # API接口封装
│   └── asset.js                           # 资产相关API调用
│
├── assets/                                # 静态资源
│   ├── icons/                            # 图标资源
│   └── images/                           # 图片资源
│
├── components/                            # 公共组件
│   ├── AssetTable.vue                     # 资产表格组件
│   ├── AssetForm.vue                      # 资产表单组件
│   ├── SearchPanel.vue                    # 搜索面板组件
│   ├── StatisticsChart.vue                # 统计图表组件
│   └── ConfirmDialog.vue                  # 确认对话框组件
│
├── views/                                 # 页面组件
│   ├── AssetManagement.vue               # 资产管理主页面
│   └── Statistics.vue                    # 统计报表页面
│
├── router/                                # 路由配置
│   └── index.js                          # Vue Router配置
│
├── App.vue                               # 根组件
├── main.js                               # 应用入口
└── style.css                             # 全局样式
```

## 🔧 核心模块详解

### 1. 实体设计 (Entity)

```java
@Entity
@Table(name = "fixed_assets", indexes = {
    @Index(name = "idx_asset_code", columnList = "asset_code"),
    @Index(name = "idx_department", columnList = "department"),
    @Index(name = "idx_asset_status", columnList = "asset_status"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String assetCode;

    @Column(nullable = false, length = 200)
    private String assetName;

    @Column(length = 100)
    private String assetCategory;

    @Column(length = 100)
    private String department;

    @Column(length = 50)
    private String userName;

    @Column(length = 200)
    private String location;

    @Column(length = 20, columnDefinition = "VARCHAR(20) DEFAULT '在用'")
    private String assetStatus;

    @Column(precision = 12, scale = 2)
    private BigDecimal originalValue;

    @Column(precision = 12, scale = 2)
    private BigDecimal netValue;

    @Column(length = 100)
    private String brand;

    @Column(length = 200)
    private String specification;

    private LocalDate startDate;

    @Column(length = 50)
    private String responsiblePerson;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### 2. 控制器设计 (Controller)

```java
@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    @Autowired
    private AssetService assetService;

    /**
     * 多条件搜索资产（推荐使用）
     */
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAssets(@RequestBody AssetSearchDto searchDto) {
        // 实现分页+多条件搜索
    }

    /**
     * 获取所有资产（简单列表）
     */
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        // 返回所有资产列表
    }

    /**
     * 创建新资产
     */
    @PostMapping
    public ResponseEntity<?> createAsset(@Valid @RequestBody Asset asset) {
        // 验证并保存资产
    }

    /**
     * 批量删除资产
     */
    @PostMapping("/batch-delete")
    public ResponseEntity<?> batchDeleteAssets(@RequestBody List<Long> ids) {
        // 批量删除指定ID的资产
    }
}
```

### 3. 前端页面结构

#### AssetManagement.vue 主要功能
- **页面布局**: 顶部操作栏 + 搜索面板 + 数据表格 + 分页控件
- **操作按钮**: 新增、导入、导出、批量删除
- **搜索功能**: 可折叠搜索面板，支持多条件筛选
- **表格功能**: 排序、选择、批量操作
- **弹窗组件**: 新增/编辑资产表单
- **状态管理**: 使用Pinia进行全局状态管理

#### Statistics.vue 统计功能
- **图表展示**: ECharts集成，显示各类统计图表
- **数据汇总**: 部门统计、状态统计、价值统计
- **实时更新**: 与资产数据联动，实时反映统计结果

## 🚀 开发环境搭建

### 环境要求
- JDK 8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- npm 或 yarn

### 启动步骤

1. **数据库准备**
   ```bash
   mysql -u root -p
   CREATE DATABASE asset_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **导入数据库脚本**
   ```bash
   mysql -u root -p asset_management < create_optimized_database.sql
   ```

3. **启动后端服务**
   ```bash
   cd optimized-backend
   mvn spring-boot:run
   # 服务地址: http://localhost:8080
   ```

4. **启动前端服务**
   ```bash
   cd optimized-frontend
   npm install
   npm run dev
   # 服务地址: http://localhost:3000
   ```

## 🔍 API 使用示例

### 搜索资产（推荐方式）
```javascript
// 前端调用
const searchParams = {
  assetName: "电脑",
  department: "技术部",
  assetStatus: "在用",
  page: 0,
  size: 10,
  sortBy: "assetName",
  sortDirection: "asc"
};

await assetApi.search(searchParams);
```

### 批量操作
```javascript
// 批量删除
const selectedIds = [1, 2, 3];
await assetApi.batchDelete(selectedIds);

// Excel导入
const fileInput = document.getElementById('excel-file');
const file = fileInput.files[0];
await assetApi.importExcel(file);
```

## 📊 数据库设计

### 表结构设计
```sql
CREATE TABLE fixed_assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    asset_code VARCHAR(50) UNIQUE NOT NULL,
    asset_name VARCHAR(200) NOT NULL,
    asset_category VARCHAR(100),
    department VARCHAR(100),
    user_name VARCHAR(50),
    location VARCHAR(200),
    asset_status VARCHAR(20) DEFAULT '在用',
    original_value DECIMAL(12,2),
    net_value DECIMAL(12,2),
    brand VARCHAR(100),
    specification VARCHAR(200),
    start_date DATE,
    responsible_person VARCHAR(50),
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 索引优化
- `idx_asset_code`: 资产编号唯一索引
- `idx_department`: 部门查询优化
- `idx_asset_status`: 状态查询优化
- `idx_created_at`: 创建时间排序优化

## 🛠️ 开发技巧

### 1. 代码规范
- **Java**: 遵循Spring Boot最佳实践，使用Lombok简化代码
- **Vue**: 使用Composition API，保持组件单一职责
- **API**: RESTful风格，统一的响应格式

### 2. 调试技巧
- **后端**: 使用`mvn spring-boot:run`配合IDE调试
- **前端**: 使用`npm run dev`配合浏览器开发者工具
- **API测试**: 使用Postman或curl进行测试

### 3. 性能优化
- **数据库**: 合理使用索引，避免全表扫描
- **分页**: 大数据量时使用分页查询
- **缓存**: 考虑添加Redis缓存热点数据

## 🚨 常见问题解决

### 1. 跨域问题
检查 `application.yml` 中的跨域配置：
```yaml
spring:
  cors:
    allowed-origins: "*"
    allowed-methods: "*"
    allowed-headers: "*"
```

### 2. 端口冲突
修改配置文件中的端口号：
- 后端: `application.yml` 中的 `server.port`
- 前端: `vite.config.js` 中的 `server.port`

### 3. 数据库连接失败
检查 `application.yml` 中的数据库连接配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/asset_management?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
```

## 📚 学习资源

- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [Vue 3官方文档](https://v3.vuejs.org/)
- [Element Plus组件库](https://element-plus.org/)
- [MyBatis官方文档](https://mybatis.org/mybatis-3/zh/index.html)

## 🤝 贡献指南

1. Fork项目到你的GitHub账号
2. 创建新的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 📞 联系方式

如有问题或建议，请：
- 提交Issue
- 联系开发团队
- 查看项目文档

---

**祝您开发愉快！**