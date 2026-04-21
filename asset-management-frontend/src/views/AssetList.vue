<template>
  <div class="asset-list">
    <el-card>
      <div class="header">
        <div class="left">
          <h2>固定资产管理</h2>
        </div>
        <div class="right">
          <el-button type="primary" @click="goToAddAsset">
            <el-icon><Plus /></el-icon>
            新增资产
          </el-button>
        </div>
      </div>

      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="资产名称">
          <el-input v-model="searchForm.assetName" placeholder="请输入资产名称" clearable />
        </el-form-item>
        <el-form-item label="使用部门">
          <el-select v-model="searchForm.department" placeholder="请选择部门" clearable>
            <el-option label="技术部" value="技术部" />
            <el-option label="行政部" value="行政部" />
            <el-option label="财务部" value="财务部" />
            <el-option label="人事部" value="人事部" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产状态">
          <el-select v-model="searchForm.assetStatus" placeholder="请选择状态" clearable>
            <el-option label="在用" value="在用" />
            <el-option label="闲置" value="闲置" />
            <el-option label="维修中" value="维修中" />
            <el-option label="报废" value="报废" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 资产表格 -->
      <el-table
        v-loading="loading"
        :data="assets"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column prop="assetCode" label="资产编号" width="120" />
        <el-table-column prop="assetName" label="资产名称" min-width="150" />
        <el-table-column prop="assetCategory" label="资产类别" width="100" />
        <el-table-column prop="department" label="使用部门" width="100" />
        <el-table-column prop="userName" label="使用人" width="100" />
        <el-table-column prop="location" label="存放地点" width="120" />
        <el-table-column prop="originalValue" label="原值" width="100">
          <template #default="scope">
            ¥{{ scope.row.originalValue?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="assetStatus" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.assetStatus)">
              {{ scope.row.assetStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button
              size="small"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalItems"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAssetStore } from '../stores/asset'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()
const assetStore = useAssetStore()

const assets = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)

const searchForm = reactive({
  assetName: '',
  department: '',
  assetStatus: ''
})

const fetchAssets = async () => {
  loading.value = true
  try {
    const response = await assetStore.searchAssets(
      searchForm.assetName,
      searchForm.department,
      searchForm.assetStatus,
      currentPage.value - 1,
      pageSize.value
    )
    assets.value = response.assets
    totalItems.value = response.totalItems
  } catch (error) {
    ElMessage.error('获取资产列表失败：' + error.message)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchAssets()
}

const resetSearch = () => {
  searchForm.assetName = ''
  searchForm.department = ''
  searchForm.assetStatus = ''
  currentPage.value = 1
  fetchAssets()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchAssets()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchAssets()
}

const goToAddAsset = () => {
  router.push('/assets/add')
}

const handleEdit = (asset) => {
  router.push(`/assets/edit/${asset.id}`)
}

const handleDelete = (asset) => {
  ElMessageBox.confirm(
    `确定要删除资产 "${asset.assetName}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await assetStore.deleteAsset(asset.id)
      ElMessage.success('删除成功')
      fetchAssets()
    } catch (error) {
      ElMessage.error('删除失败：' + error.message)
    }
  }).catch(() => {})
}

const getStatusType = (status) => {
  const types = {
    '在用': 'success',
    '闲置': 'warning',
    '维修中': 'info',
    '报废': 'danger'
  }
  return types[status] || 'info'
}

onMounted(() => {
  fetchAssets()
})
</script>

<style scoped>
.asset-list {
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
  color: #303133;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>