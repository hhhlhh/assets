# 固定资产管理系统 - 学习指南

## 目录

1. [项目概述](#项目概述)
2. [技术架构](#技术架构)
3. [前端开发指南](#前端开发指南)
4. [后端开发指南](#后端开发指南)
5. [API接口文档](#api接口文档)
6. [数据库设计](#数据库设计)
7. [部署指南](#部署指南)
8. [常见问题](#常见问题)

## 项目概述

这是一个基于Spring Boot + Vue的前后端分离固定资产管理系统。系统实现了资产的增删改查、批量导入导出、统计报表等核心功能。

### 核心功能
- ✅ 资产管理（CRUD操作）
- ✅ 多条件搜索和分页
- ✅ Excel批量导入/导出
- ✅ 统计报表和图表展示
- ✅ 响应式设计

## 技术架构

### 前端技术栈
```
Vue 3 + Vite + Element Plus + Vue Router + Pinia + Axios + ECharts
```

### 后端技术栈
```
Spring Boot 2.7 + MySQL + Spring Data JPA + Apache POI + Lombok
```

### 架构图
```
┌─────────────────┐    HTTP     ┌─────────────────┐    JDBC     ┌─────────────────┐
│   Vue 3 前端     │ ◄────────── │ Spring Boot后端  │ ◄────────── │   MySQL数据库   │
│                 │   RESTful   │                 │             │                 │
│  - 组件化开发    │             │  - 控制器层      │             │  - 数据持久化   │
│  - 响应式设计    │             │  - 服务层        │             │  - 事务管理     │
│  - 状态管理      │             │  - 数据访问层    │             │  - 索引优化     │
└─────────────────┘             └─────────────────┘             └─────────────────┘
```

## 前端开发指南

### 1. 项目结构

```
optimized-frontend/
├── src/
│   ├── api/                    # API接口封装
│   │   └── asset.js           # 资产相关API
│   ├── components/            # 可复用组件
│   │   └── AssetForm.vue      # 资产表单组件
│   ├── views/                 # 页面组件
│   │   ├── AssetManagement.vue # 资产管理页面
│   │   └── Statistics.vue     # 统计报表页面
│   ├── router/                # 路由配置
│   │   └── index.js           # 路由定义
│   ├── App.vue               # 根组件
│   └── main.js               # 应用入口
├── package.json              # 依赖配置
├── vite.config.js            # Vite配置
└── index.html                # HTML模板
```

### 2. Vue 3 核心概念应用

#### Composition API
```javascript
// AssetManagement.vue 中的 setup 函数
<script setup>
import { ref, reactive, onMounted, computed } from 'vue'

// ref - 响应式基本类型
const loading = ref(false)
const showAddDialog = ref(false)

// reactive - 响应式对象
const searchForm = reactive({
  assetName: '',
  assetCode: '',
  department: '',
  // ...
})

// computed - 计算属性
const totalPages = computed(() => {
  return Math.ceil(pagination.total / pagination.pageSize)
})
</script>
```

#### 组件通信
```vue
<!-- 父子组件通信 -->
<!-- AssetManagement.vue -->
<AssetForm
  :asset-data="currentAsset"
  :edit-mode="editMode"
  @submit="handleFormSubmit"
  @cancel="showAddDialog = false"
/>

<!-- AssetForm.vue -->
<script setup>
const props = defineProps({
  assetData: { type: Object, default: () => null },
  editMode: { type: Boolean, default: false }
})

const emit = defineEmits(['submit', 'cancel'])

const handleSubmit = () => {
  emit('submit', formData)
}
</script>
```

### 3. API调用方式

#### Axios封装
```javascript
// src/api/asset.js
import axios from 'axios'

const API_BASE_URL = '/api'

export const assetApi = {
  // GET请求 - 获取数据
  getAll: () => {
    return axios.get(`${API_BASE_URL}/assets`)
  },

  // POST请求 - 创建数据
  create: (assetData) => {
    return axios.post(`${API_BASE_URL}/assets`, assetData)
  },

  // PUT请求 - 更新数据
  update: (id, assetData) => {
    return axios.put(`${API_BASE_URL}/assets/${id}`, assetData)
  },

  // DELETE请求 - 删除数据
  delete: (id) => {
    return axios.delete(`${API_BASE_URL}/assets/${id}`)
  },

  // POST请求 - 复杂查询
  search: (searchParams) => {
    return axios.post(`${API_BASE_URL}/assets/search`, searchParams)
  },

  // 文件上传
  importExcel: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return axios.post(`${API_BASE_URL}/assets/import/excel`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  // 文件下载
  exportExcel: () => {
    return axios.get(`${API_BASE_URL}/assets/export/excel`, {
      responseType: 'blob'  // 重要：指定响应类型为blob
    })
  }
}
```

#### API调用示例
```javascript
// 在Vue组件中调用API
const fetchAssets = async () => {
  loading.value = true
  try {
    const response = await assetApi.search(searchParams)
    assets.value = response.data.assets
    pagination.total = response.data.totalItems
  } catch (error) {
    ElMessage.error('获取数据失败：' + error.message)
  } finally {
    loading.value = false
  }
}
```

### 4. 状态管理

#### 响应式数据
```javascript
// 使用ref创建响应式基本类型
const count = ref(0)
const message = ref('Hello Vue 3')

// 使用reactive创建响应式对象
const user = reactive({
  name: '张三',
  age: 25,
  department: '技术部'
})

// 使用computed创建计算属性
const doubleCount = computed(() => count.value * 2)
```

#### 生命周期
```javascript
import { onMounted, onUnmounted } from 'vue'

onMounted(() => {
  // 组件挂载后执行
  fetchAssets()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  // 组件卸载前执行
  window.removeEventListener('resize', handleResize)
})
```

### 5. Element Plus 组件使用

#### 表单组件
```vue
<el-form :model="formData" :rules="rules" ref="formRef">
  <el-form-item label="资产名称" prop="assetName">
    <el-input v-model="formData.assetName" placeholder="请输入资产名称" />
  </el-form-item>

  <el-form-item label="资产类别" prop="assetCategory">
    <el-select v-model="formData.assetCategory" placeholder="请选择类别">
      <el-option label="电子设备" value="电子设备" />
      <el-option label="办公设备" value="办公设备" />
    </el-select>
  </el-form-item>

  <el-form-item label="原值" prop="originalValue">
    <el-input-number
      v-model="formData.originalValue"
      :min="0"
      :precision="2"
      placeholder="请输入原值"
    />
  </el-form-item>
</el-form>
```

#### 表格组件
```vue
<el-table :data="assets" style="width: 100%" border stripe>
  <el-table-column prop="assetCode" label="资产编号" width="120" fixed />
  <el-table-column prop="assetName" label="资产名称" min-width="150" />
  <el-table-column prop="originalValue" label="原值" width="100" align="right">
    <template #default="scope">
      ¥{{ scope.row.originalValue?.toLocaleString() || '0.00' }}
    </template>
  </el-table-column>
  <el-table-column prop="assetStatus" label="状态" width="80" align="center">
    <template #default="scope">
      <el-tag :type="getStatusType(scope.row.assetStatus)" size="small">
        {{ scope.row.assetStatus || '未知' }}
      </el-tag>
    </template>
  </el-table-column>
</el-table>
```

#### 对话框组件
```vue
<el-dialog
  v-model="showDialog"
  title="新增资产"
  width="800px"
  :close-on-click-modal="false"
>
  <AssetForm
    v-if="showDialog"
    :asset-data="currentAsset"
    :edit-mode="editMode"
    @submit="handleFormSubmit"
    @cancel="showDialog = false"
  />
</el-dialog>
```

### 6. 路由配置

```javascript
// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'AssetManagement',
    component: () => import('../views/AssetManagement.vue'),
    meta: { title: '资产管理' }
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: () => import('../views/Statistics.vue'),
    meta: { title: '统计报表' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - 固定资产管理系统'
  }
  next()
})
```

### 7. 图表集成（ECharts）

```javascript
// Statistics.vue 中的图表初始化
import * as echarts from 'echarts'

const initChart = () => {
  const chartDom = document.getElementById('chart-container')
  const chart = echarts.init(chartDom)

  const option = {
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: '部门分布',
      type: 'pie',
      radius: ['40%', '70%'],
      data: chartData.value
    }]
  }

  chart.setOption(option)

  // 响应式处理
  window.addEventListener('resize', () => chart.resize())
}
```

## 后端开发指南

### 1. 项目结构

```
optimized-backend/
src/main/java/com/assetmanagement/
├── AssetManagementApplication.java      # 启动类
├── controller/                         # 控制器层
│   └── AssetController.java            # 资产控制器
├── service/                            # 业务层
│   ├── AssetService.java               # 资产服务
│   ├── ExcelExportService.java         # Excel导出服务
│   └── ExcelImportService.java         # Excel导入服务
├── repository/                         # 数据访问层
│   └── AssetRepository.java            # 资产仓库
├── entity/                             # 实体类
│   └── Asset.java                      # 资产实体
├── dto/                                # 数据传输对象
│   ├── AssetDto.java                   # 资产DTO
│   └── AssetSearchDto.java             # 资产搜索DTO
└── exception/                          # 异常处理
    ├── GlobalExceptionHandler.java     # 全局异常处理器
    ├── ErrorResponse.java              # 错误响应
    ├── ResourceNotFoundException.java  # 资源未找到异常
    └── DuplicateResourceException.java # 重复资源异常
```

### 2. Spring Boot核心概念

#### 依赖注入
```java
@Service
@Transactional
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    // 构造函数注入（推荐）
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }
}
```

#### 事务管理
```java
@Service
@Transactional  // 类级别事务
public class AssetService {

    @Transactional(propagation = Propagation.REQUIRED)
    public Asset save(Asset asset) {
        // 事务性操作
        return assetRepository.save(asset);
    }

    @Modifying
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 批量删除操作
        assetRepository.deleteAllByIdInBatch(ids);
    }
}
```

### 3. RESTful API设计

#### 控制器层
```java
@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    @Autowired
    private AssetService assetService;

    // GET - 查询所有
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.findAll());
    }

    // GET - 根据ID查询
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return assetService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - 创建
    @PostMapping
    public ResponseEntity<?> createAsset(@Valid @RequestBody Asset asset) {
        try {
            Asset savedAsset = assetService.save(asset);
            return ResponseEntity.ok(savedAsset);
        } catch (DuplicateResourceException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT - 更新
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(@PathVariable Long id, @Valid @RequestBody Asset assetDetails) {
        try {
            Asset updatedAsset = assetService.update(id, assetDetails);
            return ResponseEntity.ok(updatedAsset);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - 删除
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAsset(@PathVariable Long id) {
        try {
            assetService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

### 4. 数据访问层（JPA）

```java
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    // 自定义查询方法
    Optional<Asset> findByAssetCode(String assetCode);

    boolean existsByAssetCode(String assetCode);

    // 自定义JPQL查询
    @Query("SELECT a FROM Asset a WHERE " +
           "(:assetName IS NULL OR a.assetName LIKE %:assetName%) AND " +
           "(:assetCode IS NULL OR a.assetCode LIKE %:assetCode%) AND " +
           "(:department IS NULL OR a.department = :department) AND " +
           "(:assetStatus IS NULL OR a.assetStatus = :assetStatus)")
    Page<Asset> findBySearchCriteria(
        @Param("assetName") String assetName,
        @Param("assetCode") String assetCode,
        @Param("department") String department,
        @Param("assetStatus") String assetStatus,
        Pageable pageable
    );

    // 原生SQL查询 - 统计
    @Query(value = "SELECT department, COUNT(*) FROM fixed_assets GROUP BY department",
           nativeQuery = true)
    List<Object[]> countByDepartment();

    @Query(value = "SELECT asset_status, COUNT(*) FROM fixed_assets GROUP BY asset_status",
           nativeQuery = true)
    List<Object[]> countByAssetStatus();

    @Query(value = "SELECT SUM(original_value), SUM(net_value) FROM fixed_assets",
           nativeQuery = true)
    Object[] sumAssetValues();
}
```

### 5. 异常处理

#### 自定义异常
```java
// 资源未找到异常
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: %s", resource, field, value));
    }
}

// 重复资源异常
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format("%s already exists with %s: %s", resource, field, value));
    }
}
```

#### 全局异常处理器
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        // 处理参数验证异常
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors.toString(),
                request.getDescription(false),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
```

### 6. Excel处理（Apache POI）

#### Excel导入
```java
@Service
public class ExcelImportService {

    public ImportResult importFromExcel(MultipartFile file) throws IOException {
        List<Asset> assets = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            // 跳过标题行
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) continue;

                try {
                    Asset asset = parseRowToAsset(row);
                    assets.add(asset);
                } catch (Exception e) {
                    errors.add(String.format("第%d行: %s", rowIndex + 1, e.getMessage()));
                }
            }

            // 批量保存
            for (Asset asset : assets) {
                assetService.save(asset);
            }

        } catch (Exception e) {
            throw new IOException("读取Excel文件失败: " + e.getMessage());
        }

        return new ImportResult(assets.size(), errors.size(), errors);
    }

    private Asset parseRowToAsset(Row row) {
        Asset asset = new Asset();
        // 解析每一列数据
        asset.setAssetCode(getCellValueAsString(row.getCell(0)));
        asset.setAssetName(getCellValueAsString(row.getCell(1)));
        // ... 其他字段
        return asset;
    }
}
```

#### Excel导出
```java
@Service
public class ExcelExportService {

    public void exportToExcel(List<Asset> assets, HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("固定资产");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"资产编号", "资产名称", "资产类别", "使用部门", "使用人",
                               "存放地点", "原值", "净值", "状态", "品牌"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            int rowNum = 1;
            for (Asset asset : assets) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(asset.getAssetCode());
                row.createCell(1).setCellValue(asset.getAssetName());
                row.createCell(2).setCellValue(asset.getAssetCategory());
                // ... 其他字段
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=assets.xlsx");

            // 写入响应
            workbook.write(response.getOutputStream());
        }
    }
}
```

## API接口文档

### 基础信息
- **基础URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **跨域**: 支持所有来源

### 资产相关接口

#### 1. 获取资产列表
```
GET /api/assets
```
**响应示例**:
```json
[
  {
    "id": 1,
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
  }
]
```

#### 2. 搜索资产（分页）
```
POST /api/assets/search
```
**请求参数**:
```json
{
  "assetName": "电脑",
  "assetCode": "FA001",
  "department": "技术部",
  "assetStatus": "在用",
  "assetCategory": "电子设备",
  "userName": "张三",
  "location": "办公室",
  "page": 0,
  "size": 10,
  "sortBy": "id",
  "sortDirection": "asc"
}
```

#### 3. 创建资产
```
POST /api/assets
Content-Type: application/json
```
**请求参数**:
```json
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
  "brand": "联想",
  "specification": "ThinkCentre M720"
}
```

#### 4. 更新资产
```
PUT /api/assets/{id}
Content-Type: application/json
```
**请求参数**: 同创建资产

#### 5. 删除资产
```
DELETE /api/assets/{id}
```

#### 6. 批量删除
```
POST /api/assets/batch-delete
Content-Type: application/json
```
**请求参数**:
```json
[1, 2, 3]
```

#### 7. Excel导入
```
POST /api/assets/import/excel
Content-Type: multipart/form-data
```

**请求参数**: 文件上传

**响应示例**:
```json
{
  "successCount": 100,
  "errorCount": 2,
  "totalCount": 102,
  "errors": [
    "第5行: 资产编号已存在",
    "第8行: 资产名称不能为空"
  ]
}
```

#### 8. Excel导出
```
GET /api/assets/export/excel
```

#### 9. CSV导出
```
GET /api/assets/export/csv
```

#### 10. 获取统计数据
```
GET /api/assets/statistics
```

**响应示例**:
```json
{
  "totalCount": 150,
  "totalOriginalValue": 750000.00,
  "totalNetValue": 650000.00,
  "departmentStats": [
    ["技术部", 50],
    ["行政部", 30],
    ["财务部", 25]
  ],
  "statusStats": [
    ["在用", 120],
    ["闲置", 20],
    ["维修中", 8],
    ["报废", 2]
  ],
  "categoryStats": [
    ["电子设备", 80],
    ["办公设备", 40],
    ["办公家具", 30]
  ]
}
```

## 数据库设计

### 表结构设计

```sql
CREATE TABLE fixed_assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    new_asset_code VARCHAR(50) COMMENT '新资产编号',
    asset_code VARCHAR(50) UNIQUE NOT NULL COMMENT '资产编号',
    finance_card_code VARCHAR(50) COMMENT '财务卡片编号',
    asset_name VARCHAR(200) NOT NULL COMMENT '资产名称',
    asset_category VARCHAR(100) COMMENT '资产大类',
    classification_name VARCHAR(100) COMMENT '资产分类名称',
    classification_code VARCHAR(50) COMMENT '资产分类代码',
    original_value DECIMAL(12,2) COMMENT '原值',
    net_value DECIMAL(12,2) COMMENT '净值',
    accumulated_depreciation DECIMAL(12,2) COMMENT '累计折旧',
    depreciation_months INT COMMENT '已计提月份',
    useful_life INT COMMENT '使用年限',
    depreciation_status VARCHAR(20) COMMENT '计提状态',
    responsible_person VARCHAR(50) COMMENT '负责人',
    asset_status VARCHAR(20) DEFAULT '在用' COMMENT '资产状态',
    department VARCHAR(100) COMMENT '使用部门',
    user_name VARCHAR(50) COMMENT '使用人',
    location VARCHAR(200) COMMENT '存放地点',
    specification VARCHAR(200) COMMENT '规格型号',
    start_date DATE COMMENT '开始使用日期',
    old_asset_code VARCHAR(50) COMMENT '旧资产编码',
    acquisition_method VARCHAR(50) COMMENT '取得方式',
    procurement_form VARCHAR(50) COMMENT '组织采购形式',
    asset_purpose VARCHAR(100) COMMENT '资产用途',
    funding_source VARCHAR(100) COMMENT '资金来源',
    brand VARCHAR(100) COMMENT '品牌',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='固定资产表';
```

### 索引设计

```sql
-- 主键索引
PRIMARY KEY (id)

-- 唯一索引
UNIQUE KEY uk_asset_code (asset_code)

-- 普通索引
CREATE INDEX idx_asset_code ON fixed_assets(asset_code);
CREATE INDEX idx_department ON fixed_assets(department);
CREATE INDEX idx_asset_status ON fixed_assets(asset_status);
CREATE INDEX idx_asset_name ON fixed_assets(asset_name);
CREATE INDEX idx_created_at ON fixed_assets(created_at);
```

### 字段说明

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 主键ID | 自增主键 |
| asset_code | VARCHAR(50) | 资产编号 | 唯一，非空 |
| asset_name | VARCHAR(200) | 资产名称 | 非空 |
| asset_category | VARCHAR(100) | 资产类别 | 可选 |
| department | VARCHAR(100) | 使用部门 | 可选 |
| asset_status | VARCHAR(20) | 资产状态 | 默认'在用' |
| original_value | DECIMAL(12,2) | 原值 | 可选 |
| net_value | DECIMAL(12,2) | 净值 | 可选 |

## 部署指南

### 开发环境部署

#### 1. 后端部署

**环境要求**:
- JDK 8+
- Maven 3.6+
- MySQL 8.0+

**部署步骤**:
```bash
# 1. 克隆项目
cd optimized-backend

# 2. 创建数据库
mysql -u root -p
CREATE DATABASE asset_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 3. 执行SQL脚本
mysql -u root -p asset_management < create_optimized_database.sql

# 4. 配置数据库连接
# 编辑 src/main/resources/application.yml
# 修改数据库连接信息

# 5. 编译和运行
mvn clean package
java -jar target/fixed-asset-system-1.0.0.jar

# 或者使用Maven运行
mvn spring-boot:run
```

#### 2. 前端部署

**环境要求**:
- Node.js 16+
- npm 或 yarn

**部署步骤**:
```bash
# 1. 进入前端目录
cd optimized-frontend

# 2. 安装依赖
npm install

# 3. 配置API代理
# 编辑 vite.config.js
# 确保代理配置正确指向后端地址

# 4. 启动开发服务器
npm run dev

# 5. 生产环境构建
npm run build
```

### 生产环境部署

#### 1. 后端生产部署

```bash
# 1. 打包应用
mvn clean package -DskipTests

# 2. 配置生产环境
# 创建 application-prod.yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://production-db:3306/asset_management
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

# 3. 运行应用
java -jar \
  --spring.profiles.active=prod \
  --spring.config.location=classpath:/application-prod.yml \
  target/fixed-asset-system-1.0.0.jar
```

#### 2. 前端生产部署

```bash
# 1. 构建生产版本
npm run build

# 2. 配置Nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /path/to/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api {
        proxy_pass http://localhost:8080/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

#### 3. Docker部署

**后端Dockerfile**:
```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/fixed-asset-system-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

**前端Dockerfile**:
```dockerfile
FROM nginx:alpine
COPY dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

**docker-compose.yml**:
```yaml
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
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/asset_management

  frontend:
    build: ./optimized-frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

## 常见问题

### 1. 数据库连接问题

**问题**: 应用启动时数据库连接失败

**解决方案**:
1. 检查MySQL服务是否启动
2. 确认数据库连接配置正确
3. 检查防火墙设置
4. 验证数据库用户权限

```yaml
# application.yml 配置检查
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/asset_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: rootroot
```

### 2. 跨域问题

**问题**: 前端访问后端API时出现跨域错误

**解决方案**:
1. 确保后端配置了跨域支持
2. 检查前端代理配置
3. 生产环境使用Nginx反向代理

```java
// 后端跨域配置
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/assets")
public class AssetController {
    // ...
}
```

### 3. 文件上传大小限制

**问题**: Excel文件上传失败

**解决方案**:
1. 检查文件大小限制配置
2. 增加上传大小限制

```yaml
# application.yml
spring:
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB
```

### 4. 内存溢出问题

**问题**: 处理大量数据时出现内存溢出

**解决方案**:
1. 增加JVM内存
2. 优化数据查询（分页）
3. 使用流式处理

```bash
# 增加JVM内存
java -Xms512m -Xmx2048m -jar target/fixed-asset-system-1.0.0.jar
```

### 5. 前端构建问题

**问题**: npm install 或 npm run build 失败

**解决方案**:
1. 清除npm缓存：`npm cache clean --force`
2. 删除node_modules：`rm -rf node_modules`
3. 重新安装：`npm install`
4. 检查Node.js版本

### 6. 性能优化建议

1. **数据库优化**:
   - 添加合适的索引
   - 使用分页查询
   - 避免N+1查询问题

2. **前端优化**:
   - 启用Gzip压缩
   - 使用CDN
   - 代码分割和懒加载

3. **后端优化**:
   - 添加Redis缓存
   - 连接池优化
   - 异步处理耗时操作

## 学习资源推荐

### Vue 3 学习资源
1. [Vue 3 官方文档](https://v3.vuejs.org/)
2. [Vue 3 组合式API](https://v3.vuejs.org/guide/composition-api-introduction.html)
3. [Element Plus 组件库](https://element-plus.org/)

### Spring Boot 学习资源
1. [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
2. [Spring Data JPA 指南](https://spring.io/projects/spring-data-jpa)
3. [RESTful API 设计指南](https://restfulapi.net/)

### 数据库学习资源
1. [MySQL 官方文档](https://dev.mysql.com/doc/)
2. [JPA 学习指南](https://www.baeldung.com/jpa-hibernate-tutorial)
3. [SQL 优化最佳实践](https://use-the-index-luke.com/)

## 结语

本学习指南涵盖了固定资产管理系统的完整开发流程，包括前端Vue 3开发、后端Spring Boot开发、API设计、数据库设计、部署运维等各个方面。通过学习本指南，您将能够：

1. 理解前后端分离架构的设计思想
2. 掌握Vue 3 + Spring Boot的开发模式
3. 学会RESTful API的设计和实现
4. 了解企业级应用的开发和部署流程

建议按照指南的顺序逐步学习，结合实际项目代码进行练习，加深理解。