# 固定资产管理系统 - 快速入门指南

## 🚀 5分钟快速开始

### 步骤1：环境准备

```bash
# 检查环境
node --version  # 需要 v16+
java --version  # 需要 JDK 8+
mysql --version # 需要 MySQL 8.0+
mvn --version   # 需要 Maven 3.6+
```

### 步骤2：数据库设置

```bash
# 1. 登录MySQL
mysql -u root -p

# 2. 创建数据库
CREATE DATABASE asset_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 3. 导入数据
mysql -u root -p asset_management < create_optimized_database.sql
```

### 步骤3：启动后端服务

```bash
cd optimized-backend

# 编译项目
mvn clean package

# 启动应用
java -jar target/fixed-asset-system-1.0.0.jar

# 应用将在 http://localhost:8080 启动
```

### 步骤4：启动前端服务

```bash
cd optimized-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 应用将在 http://localhost:3000 启动
```

### 步骤5：访问系统

1. 打开浏览器访问 `http://localhost:3000`
2. 您应该看到固定资产管理系统的主界面
3. 尝试添加、编辑、删除资产
4. 查看统计报表页面

## 🎯 核心功能演示

### 1. 添加新资产

1. 点击"新增资产"按钮
2. 填写资产信息：
   - 资产编号：FA001
   - 资产名称：联想台式电脑
   - 资产类别：电子设备
   - 使用部门：技术部
   - 原值：5000.00
3. 点击"创建"按钮

### 2. 搜索资产

1. 在搜索框中输入关键词
2. 选择筛选条件（部门、状态等）
3. 点击"搜索"按钮
4. 查看分页结果

### 3. 导入Excel数据

1. 准备Excel文件（格式见下方）
2. 点击"导入Excel"按钮
3. 选择文件并上传
4. 查看导入结果

### 4. 查看统计报表

1. 点击顶部导航的"统计报表"
2. 查看各类图表和数据
3. 分析资产分布情况

## 📋 Excel导入格式

### 必需列（第一行标题）：
| 资产编号 | 资产名称 | 资产类别 | 使用部门 | 使用人 | 存放地点 | 资产状态 | 原值 | 净值 | 品牌 | 规格型号 | 开始使用日期 | 负责人 | 备注 |

### 示例数据：
| FA001 | 联想台式电脑 | 电子设备 | 技术部 | 张三 | 办公室A-101 | 在用 | 5000.00 | 4500.00 | 联想 | ThinkCentre M720 | 2023-01-15 | 张三 | 新购设备 |

## 🔧 常用开发命令

### 前端开发

```bash
# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview

# 代码格式化
npm run format

# 代码检查
npm run lint
```

### 后端开发

```bash
# 编译项目
mvn clean compile

# 打包应用
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests

# 运行应用
mvn spring-boot:run

# 运行特定测试
mvn test -Dtest=AssetServiceTest
```

## 📁 项目结构概览

### 前端结构
```
optimized-frontend/
├── src/
│   ├── api/           # API接口
│   │   └── asset.js  # 资产API
│   ├── assets/        # 静态资源
│   ├── components/    # 公共组件
│   ├── views/         # 页面组件
│   │   ├── AssetManagement.vue  # 资产管理页面
│   │   └── Statistics.vue       # 统计报表页面
│   ├── router/        # 路由配置
│   │   └── index.js  # 路由定义
│   ├── App.vue        # 根组件
│   ├── main.js        # 入口文件
│   └── style.css      # 全局样式
├── package.json      # npm配置
└── vite.config.js    # Vite配置
```

### 后端结构
```
optimized-backend/
├── src/main/java/com/assetmanagement/
│   ├── AssetManagementApplication.java  # 主应用类
│   ├── config/                         # 配置类
│   ├── controller/                     # 控制器层
│   │   └── AssetController.java        # 资产控制器
│   ├── dto/                           # 数据传输对象
│   ├── entity/                        # 实体层
│   │   └── Asset.java                 # 资产实体
│   ├── exception/                     # 异常处理
│   ├── repository/                    # 数据访问层
│   │   └── AssetRepository.java     # 资产仓库
│   ├── service/                       # 服务层
│   │   ├── AssetService.java         # 资产服务
│   │   ├── ExcelExportService.java   # Excel导出服务
│   │   └── ExcelImportService.java   # Excel导入服务
│   └── util/                          # 工具类
├── src/main/resources/
│   ├── application.yml                # 配置文件
│   └── data.sql                       # 初始化数据（可选）
└── pom.xml                            # Maven配置
```

## 🌐 API快速参考

### 常用接口

```bash
# 获取所有资产（简单列表）
GET http://localhost:8080/api/assets

# 搜索资产（分页+多条件）
POST http://localhost:8080/api/assets/search

# 根据ID获取资产
GET http://localhost:8080/api/assets/{id}

# 根据资产编号获取资产
GET http://localhost:8080/api/assets/code/{assetCode}

# 创建资产
POST http://localhost:8080/api/assets

# 更新资产
PUT http://localhost:8080/api/assets/{id}

# 删除资产
DELETE http://localhost:8080/api/assets/{id}

# 批量删除资产
POST http://localhost:8080/api/assets/batch-delete

# 导出Excel
GET http://localhost:8080/api/assets/export/excel

# 导出CSV
GET http://localhost:8080/api/assets/export/csv

# 导入Excel
POST http://localhost:8080/api/assets/import/excel

# 获取统计数据
GET http://localhost:8080/api/assets/statistics
```

### 使用curl测试API

```bash
# 获取资产列表
curl -X GET http://localhost:8080/api/assets

# 创建新资产
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
    "startDate": "2023-01-15",
    "brand": "联想",
    "specification": "ThinkCentre M720"
  }'

# 搜索资产
curl -X POST http://localhost:8080/api/assets/search \
  -H "Content-Type: application/json" \
  -d '{
    "assetName": "电脑",
    "department": "技术部",
    "assetStatus": "在用",
    "page": 0,
    "size": 10
  }'

# 批量删除资产
curl -X POST http://localhost:8080/api/assets/batch-delete \
  -H "Content-Type: application/json" \
  -d '[1, 2, 3]'
```

## 🎨 前端开发要点

### 1. 组件开发

```vue
<!-- 创建新组件 -->
<template>
  <div class="my-component">
    <h2>{{ title }}</h2>
    <el-button @click="handleClick">点击我</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const title = ref('我的组件')
const handleClick = () => {
  console.log('按钮被点击了')
}
</script>

<style scoped>
.my-component {
  padding: 20px;
  border: 1px solid #eee;
}
</style>
```

### 2. API调用

```javascript
// src/api/asset.js
export const assetApi = {
  // 获取资产列表
  getAssets: () => {
    return axios.get('/api/assets')
  },

  // 创建资产
  createAsset: (data) => {
    return axios.post('/api/assets', data)
  }
}

// 在组件中使用
const createNewAsset = async () => {
  try {
    const response = await assetApi.createAsset(assetData)
    console.log('创建成功:', response.data)
  } catch (error) {
    console.error('创建失败:', error)
  }
}
```

### 3. 路由配置

```javascript
// src/router/index.js
const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/assets',
    name: 'Assets',
    component: () => import('../views/AssetManagement.vue')
  }
]
```

## 🔧 后端开发要点

### 1. 创建新实体

```java
@Entity
@Table(name = "my_table")
@Data
public class MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

### 2. 创建Repository

```java
@Repository
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {

    // 自定义查询方法
    List<MyEntity> findByNameContaining(String name);

    // 自定义JPQL查询
    @Query("SELECT e FROM MyEntity e WHERE e.name LIKE %:keyword%")
    List<MyEntity> searchByKeyword(@Param("keyword") String keyword);
}
```

### 3. 创建Service

```java
@Service
@Transactional
public class MyEntityService {

    @Autowired
    private MyEntityRepository repository;

    public MyEntity create(MyEntity entity) {
        return repository.save(entity);
    }

    public List<MyEntity> findAll() {
        return repository.findAll();
    }

    public Optional<MyEntity> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
```

### 4. 创建Controller

```java
@RestController
@RequestMapping("/api/my-entities")
@CrossOrigin(origins = "*")
public class MyEntityController {

    @Autowired
    private MyEntityService service;

    @GetMapping
    public ResponseEntity<List<MyEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<MyEntity> create(@Valid @RequestBody MyEntity entity) {
        MyEntity saved = service.create(entity);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyEntity> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
```

## 🐛 常见问题快速解决

### 前端问题

**Q: npm install 失败**
A: 
```bash
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

**Q: 跨域错误**
A: 检查 `vite.config.js` 中的代理配置：
```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

### 后端问题

**Q: 数据库连接失败**
A: 检查 `application.yml` 中的数据库配置

**Q: 端口被占用**
A: 
```bash
# 查找占用端口的进程
lsof -i :8080
# 或
netstat -tlnp | grep 8080

# 修改端口
# 在 application.yml 中修改 server.port
```

### 数据库问题

**Q: 忘记MySQL密码**
A: 
```bash
# 停止MySQL服务
sudo service mysql stop

# 安全模式启动
sudo mysqld_safe --skip-grant-tables &

# 重置密码
mysql -u root
UPDATE mysql.user SET authentication_string=PASSWORD('new_password') WHERE User='root';
FLUSH PRIVILEGES;
```

## 📚 进阶学习

### 学习路径建议

1. **基础阶段**（1-2天）
   - 阅读本快速入门指南
   - 运行示例项目
   - 修改简单功能

2. **进阶阶段**（1周）
   - 学习Vue 3组合式API
   - 理解Spring Boot自动配置
   - 掌握JPA数据访问

3. **高级阶段**（2-4周）
   - 学习微服务架构
   - 掌握Docker容器化
   - 了解性能优化

### 推荐学习资源

- [Vue 3 官方文档](https://v3.vuejs.org/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MySQL 官方文档](https://dev.mysql.com/doc/)
- [Element Plus 组件库](https://element-plus.org/)

## 🎯 下一步

1. ✅ 按照本指南启动项目
2. 📖 阅读详细的学习指南（LEARNING_GUIDE.md）
3. 🔧 尝试修改和扩展功能
4. 🚀 部署到生产环境

祝您学习愉快！如有问题，请参考常见问题部分或查看项目文档。