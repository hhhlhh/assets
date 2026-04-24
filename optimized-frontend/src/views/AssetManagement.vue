<template>
  <div class="asset-management">
    <el-card class="main-card">
      <!-- 页面标题和操作按钮 -->
      <div class="page-header">
        <div class="left">
          <h2>固定资产管理</h2>
        </div>
        <div class="right">
          <el-button-group>
            <el-button type="success" @click="handleExportExcel">
              <el-icon><Download /></el-icon>
              导出Excel
            </el-button>
            <el-button type="info" @click="handleExportCsv">
              <el-icon><Download /></el-icon>
              导出CSV
            </el-button>
          </el-button-group>
          <el-upload
            class="import-upload"
            :action="''"
            :show-file-list="false"
            :before-upload="handleBeforeUpload"
            :on-success="handleImportSuccess"
            :on-error="handleImportError"
            accept=".xlsx,.xls"
            style="margin-left: 10px; display: inline-block;"
          >
            <el-button type="warning">
              <el-icon><Upload /></el-icon>
              导入Excel
            </el-button>
          </el-upload>
          <el-button type="primary" @click="showAddDialog = true" style="margin-left: 10px;">
            <el-icon><Plus /></el-icon>
            新增资产
          </el-button>
        </div>
      </div>

      <!-- 搜索表单 -->
      <el-card class="search-card">
        <div class="search-header">
          <span>搜索条件</span>
          <el-button link @click="toggleSearchCollapse">
            <el-icon><component :is="searchCollapsed ? 'ArrowDown' : 'ArrowUp'" /></el-icon>
          </el-button>
        </div>
        <el-collapse-transition>
          <div v-show="!searchCollapsed">
            <el-form :model="searchForm" :inline="true" class="search-form">
              <el-row :gutter="20">
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item label="资产名称">
                    <el-input v-model="searchForm.assetName" placeholder="请输入资产名称" clearable />
                  </el-form-item>
                </el-col>
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item label="资产编号">
                    <el-input v-model="searchForm.assetCode" placeholder="请输入资产编号" clearable />
                  </el-form-item>
                </el-col>
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item label="使用部门">
                    <el-select v-model="searchForm.department" placeholder="请选择部门" clearable>
                      <el-option label="技术部" value="技术部" />
                      <el-option label="行政部" value="行政部" />
                      <el-option label="财务部" value="财务部" />
                      <el-option label="人事部" value="人事部" />
                      <el-option label="市场部" value="市场部" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item label="资产状态">
                    <el-select v-model="searchForm.assetStatus" placeholder="请选择状态" clearable>
                      <el-option label="在用" value="在用" />
                      <el-option label="闲置" value="闲置" />
                      <el-option label="维修中" value="维修中" />
                      <el-option label="报废" value="报废" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item label="资产类别">
                    <el-select v-model="searchForm.assetCategory" placeholder="请选择类别" clearable>
                      <el-option label="电子设备" value="电子设备" />
                      <el-option label="办公设备" value="办公设备" />
                      <el-option label="办公家具" value="办公家具" />
                      <el-option label="其他设备" value="其他设备" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item label="使用人">
                    <el-input v-model="searchForm.userName" placeholder="请输入使用人" clearable />
                  </el-form-item>
                </el-col>
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item label="存放地点">
                    <el-input v-model="searchForm.location" placeholder="请输入存放地点" clearable />
                  </el-form-item>
                </el-col>
                <el-col :span="24" :sm="12" :md="8" :lg="6">
                  <el-form-item>
                    <el-button type="primary" @click="handleSearch">搜索</el-button>
                    <el-button @click="resetSearch">重置</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-collapse-transition>
      </el-card>

      <!-- 资产表格 -->
      <el-table
        v-loading="loading"
        :data="assets"
        style="width: 100%"
        border
        stripe
        height="400"
      >
        <el-table-column prop="assetCode" label="资产编号" width="120" fixed />
        <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="assetCategory" label="资产类别" width="100" />
        <el-table-column prop="department" label="使用部门" width="100" />
        <el-table-column prop="userName" label="使用人" width="100" />
        <el-table-column prop="location" label="存放地点" width="120" show-overflow-tooltip />
        <el-table-column prop="originalValue" label="原值" width="100" align="right">
          <template #default="scope">
            ¥{{ scope.row.originalValue?.toLocaleString() || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="netValue" label="净值" width="100" align="right">
          <template #default="scope">
            ¥{{ scope.row.netValue?.toLocaleString() || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="assetStatus" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.assetStatus)" size="small">
              {{ scope.row.assetStatus || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="brand" label="品牌" width="100" />
        <el-table-column prop="specification" label="规格型号" width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="scope">
            <el-button
              size="small"
              @click="handleEdit(scope.row)"
              type="primary"
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
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editMode ? '编辑资产' : '新增资产'"
      width="800px"
    >
      <AssetForm
        v-if="showAddDialog"
        :asset-data="currentAsset"
        :edit-mode="editMode"
        @submit="handleFormSubmit"
        @cancel="showAddDialog = false"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, h, computed } from 'vue'
import { assetApi } from '../api/asset'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Upload, ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import AssetForm from '../components/AssetForm.vue'

// 数据定义
const assets = ref([])
const loading = ref(false)
const showAddDialog = ref(false)
const editMode = ref(false)
const currentAsset = ref(null)
const searchCollapsed = ref(false)

const searchForm = reactive({
  assetName: '',
  assetCode: '',
  department: '',
  assetStatus: '',
  assetCategory: '',
  userName: '',
  location: '',
  page: 0,
  size: 10,
  sortBy: 'id',
  sortDirection: 'asc'
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  totalPages: 0
})

// 方法定义
const fetchAssets = async () => {
  loading.value = true
  try {
    // 检查是否有搜索条件
    const hasSearchCriteria = Object.values(searchForm).some(val => 
      val !== '' && val !== null && val !== undefined && val !== 0 && val !== 10
    ) && searchForm.sortBy !== 'id' // 排除默认排序项

    if (!hasSearchCriteria && pagination.currentPage === 1) {
      // 没有搜索条件且在第一页，尝试获取全量数据
      const response = await assetApi.getAll()
      assets.value = response.data
      pagination.total = response.data.length
      pagination.totalPages = 1
      pagination.pageSize = response.data.length
    } else {
      // 有搜索条件或在非第一页，使用分页搜索
      const params = {
        ...searchForm,
        page: pagination.currentPage - 1,
        size: pagination.pageSize
      }
      const response = await assetApi.search(params)
      assets.value = response.data.assets
      pagination.total = response.data.totalItems
      pagination.totalPages = response.data.totalPages
    }
  } catch (error) {
    ElMessage.error('获取资产列表失败：' + error.message)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  fetchAssets()
}

const toggleSearchCollapse = () => {
  searchCollapsed.value = !searchCollapsed.value
}

const resetSearch = () => {
  Object.assign(searchForm, {
    assetName: '',
    assetCode: '',
    department: '',
    assetStatus: '',
    assetCategory: '',
    userName: '',
    location: ''
  })
  pagination.currentPage = 1
  fetchAssets()
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  fetchAssets()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  fetchAssets()
}

const handleEdit = (asset) => {
  currentAsset.value = { ...asset }
  editMode.value = true
  showAddDialog.value = true
}

const handleDelete = (asset) => {
  ElMessageBox.confirm(
    `确定要删除资产 "${asset.assetName}" 吗？删除后无法恢复。`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await assetApi.delete(asset.id)
      ElMessage.success('删除成功')
      fetchAssets()
    } catch (error) {
      ElMessage.error('删除失败：' + error.message)
    }
  }).catch(() => {})
}

const handleFormSubmit = async (formData) => {
  try {
    if (editMode.value) {
      await assetApi.update(currentAsset.value.id, formData)
      ElMessage.success('更新成功')
    } else {
      await assetApi.create(formData)
      ElMessage.success('创建成功')
    }
    showAddDialog.value = false
    fetchAssets()
  } catch (error) {
    ElMessage.error('操作失败：' + error.message)
  }
}

const handleExportExcel = async () => {
  try {
    const response = await assetApi.exportExcel()
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `固定资产台账_${new Date().toLocaleDateString()}.xlsx`)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    ElMessage.success('导出Excel成功')
  } catch (error) {
    ElMessage.error('导出Excel失败：' + error.message)
  }
}

const handleExportCsv = async () => {
  try {
    const response = await assetApi.exportCsv()
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `固定资产台账_${new Date().toLocaleDateString()}.csv`)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    ElMessage.success('导出CSV成功')
  } catch (error) {
    ElMessage.error('导出CSV失败：' + error.message)
  }
}

const handleBeforeUpload = async (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                  file.type === 'application/vnd.ms-excel'

  if (!isExcel) {
    ElMessage.error('只能上传Excel文件!')
    return false
  }

  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过10MB!')
    return false
  }

  try {
    loading.value = true
    const response = await assetApi.importExcel(file)

    if (response.data.errorCount === 0) {
      ElMessage.success(`导入成功！共导入 ${response.data.successCount} 条记录`)
    } else {
      ElMessage.warning(`导入完成！成功: ${response.data.successCount} 条，失败: ${response.data.errorCount} 条`)

      // 显示错误详情
      if (response.data.errors && response.data.errors.length > 0) {
        const errorDialog = {
          title: '导入错误详情',
          message: h('div', { style: 'max-height: 300px; overflow-y: auto;' },
            response.data.errors.map(error =>
              h('p', { style: 'color: #f56c6c; margin: 5px 0;' }, error)
            )
          ),
          type: 'warning'
        }

        // 使用简单的消息提示错误
        console.warn('导入错误详情:', response.data.errors)
      }
    }

    fetchAssets()
    return false // 阻止默认上传行为
  } catch (error) {
    ElMessage.error('导入失败：' + (error.response?.data?.error || error.message))
    return false
  } finally {
    loading.value = false
  }
}

const handleImportSuccess = (response) => {
  // 这个函数不会被调用，因为我们在before-upload中处理了
}

const handleImportError = (error) => {
  // 这个函数不会被调用，因为我们在before-upload中处理了
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

// 生命周期
onMounted(() => {
  fetchAssets()
})
</script>

<style scoped>
.asset-management {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.main-card {
  min-height: calc(100vh - 40px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.search-card {
  margin-bottom: 20px;
  background-color: #f8f9fa;
}

.search-form {
  margin: 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table .cell) {
  white-space: nowrap;
}

/* Responsive improvements */
.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  margin-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
  font-weight: 500;
}

.search-header span {
  color: #303133;
}

.import-upload {
  display: inline-block;
}

/* Mobile responsive styles */
@media (max-width: 768px) {
  .asset-management {
    padding: 10px;
  }

  .page-header {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
  }

  .page-header .right {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }

  .page-header .right .el-button {
    flex: 1;
    min-width: 100px;
  }

  .search-form .el-row {
    margin-bottom: 10px;
  }

  .search-form .el-col {
    margin-bottom: 10px;
  }

  .pagination-container {
    justify-content: center;
  }

  :deep(.el-dialog) {
    width: 95% !important;
    margin: 5vh auto !important;
  }

  :deep(.el-table) {
    font-size: 12px;
  }

  :deep(.el-table th),
  :deep(.el-table td) {
    padding: 8px 4px;
  }
}

@media (max-width: 480px) {
  .page-header h2 {
    font-size: 18px;
  }

  .search-card {
    padding: 10px;
  }

  .main-card {
    padding: 10px;
  }

  :deep(.el-dialog__body) {
    padding: 15px;
  }
}
</style>