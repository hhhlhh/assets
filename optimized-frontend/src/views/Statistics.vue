<template>
  <div class="statistics">
    <el-card class="main-card">
      <!-- 页面标题 -->
      <div class="page-header">
        <h2>统计报表</h2>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="20" class="statistics-cards">
        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon total">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
                <div class="stat-label">资产总数</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon value">
                <el-icon><Money /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">¥{{ formatNumber(statistics.totalOriginalValue) }}</div>
                <div class="stat-label">资产原值总额</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon net">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">¥{{ formatNumber(statistics.totalNetValue) }}</div>
                <div class="stat-label">资产净值总额</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon depreciation">
                <el-icon><PieChart /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ calculateDepreciationRate() }}%</div>
                <div class="stat-label">平均折旧率</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" class="charts-section">
        <el-col :span="12">
          <el-card class="chart-card">
            <div class="chart-header">
              <h3>部门资产分布</h3>
            </div>
            <div class="chart-container">
              <div ref="departmentChartRef" class="chart"></div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card class="chart-card">
            <div class="chart-header">
              <h3>资产状态分布</h3>
            </div>
            <div class="chart-container">
              <div ref="statusChartRef" class="chart"></div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="charts-section">
        <el-col :span="12">
          <el-card class="chart-card">
            <div class="chart-header">
              <h3>资产类别分布</h3>
            </div>
            <div class="chart-container">
              <div ref="categoryChartRef" class="chart"></div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card class="chart-card">
            <div class="chart-header">
              <h3>部门资产价值分布</h3>
            </div>
            <div class="chart-container">
              <div ref="valueChartRef" class="chart"></div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细统计表格 -->
      <el-card class="detail-table-card">
        <div class="table-header">
          <h3>详细统计数据</h3>
          <el-button type="primary" @click="exportStatistics">
            <el-icon><Download /></el-icon>
            导出统计报告
          </el-button>
        </div>

        <el-tabs v-model="activeTab">
          <el-tab-pane label="部门统计" name="department">
            <el-table :data="departmentStats" style="width: 100%">
              <el-table-column prop="department" label="部门" min-width="120" />
              <el-table-column prop="count" label="资产数量" width="100" align="center" />
              <el-table-column prop="percentage" label="占比" width="100" align="center">
                <template #default="scope">
                  {{ scope.row.percentage }}%
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="状态统计" name="status">
            <el-table :data="statusStats" style="width: 100%">
              <el-table-column prop="status" label="资产状态" min-width="120" />
              <el-table-column prop="count" label="资产数量" width="100" align="center" />
              <el-table-column prop="percentage" label="占比" width="100" align="center">
                <template #default="scope">
                  {{ scope.row.percentage }}%
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="类别统计" name="category">
            <el-table :data="categoryStats" style="width: 100%">
              <el-table-column prop="category" label="资产类别" min-width="120" />
              <el-table-column prop="count" label="资产数量" width="100" align="center" />
              <el-table-column prop="percentage" label="占比" width="100" align="center">
                <template #default="scope">
                  {{ scope.row.percentage }}%
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { assetApi } from '../api/asset'
import { ElMessage } from 'element-plus'
import { DataAnalysis, Money, TrendCharts, PieChart, Download } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const statistics = reactive({
  totalCount: 0,
  totalOriginalValue: 0,
  totalNetValue: 0,
  departmentStats: [],
  statusStats: [],
  categoryStats: []
})

const loading = ref(false)
const activeTab = ref('department')

// Chart refs
const departmentChartRef = ref(null)
const statusChartRef = ref(null)
const categoryChartRef = ref(null)
const valueChartRef = ref(null)

let departmentChart = null
let statusChart = null
let categoryChart = null
let valueChart = null

// Computed properties for formatted table data
const departmentStats = computed(() => {
  return statistics.departmentStats.map(stat => ({
    department: stat[0] || '未分配',
    count: stat[1],
    percentage: statistics.totalCount > 0 ? ((stat[1] / statistics.totalCount) * 100).toFixed(1) : 0
  }))
})

const statusStats = computed(() => {
  return statistics.statusStats.map(stat => ({
    status: stat[0] || '未知',
    count: stat[1],
    percentage: statistics.totalCount > 0 ? ((stat[1] / statistics.totalCount) * 100).toFixed(1) : 0
  }))
})

const categoryStats = computed(() => {
  return statistics.categoryStats.map(stat => ({
    category: stat[0] || '未分类',
    count: stat[1],
    percentage: statistics.totalCount > 0 ? ((stat[1] / statistics.totalCount) * 100).toFixed(1) : 0
  }))
})

const formatNumber = (value) => {
  if (!value) return '0.00'
  return Number(value).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

const calculateDepreciationRate = () => {
  if (!statistics.totalOriginalValue || statistics.totalOriginalValue === 0) return 0
  const depreciation = statistics.totalOriginalValue - statistics.totalNetValue
  return ((depreciation / statistics.totalOriginalValue) * 100).toFixed(1)
}

const fetchStatistics = async () => {
  loading.value = true
  try {
    const response = await assetApi.getStatistics()
    const data = response.data

    statistics.totalCount = data.totalCount || 0
    statistics.totalOriginalValue = data.totalOriginalValue || 0
    statistics.totalNetValue = data.totalNetValue || 0
    statistics.departmentStats = data.departmentStats || []
    statistics.statusStats = data.statusStats || []
    statistics.categoryStats = data.categoryStats || []

    // Initialize charts after data is loaded
    nextTick(() => {
      initCharts()
    })
  } catch (error) {
    ElMessage.error('获取统计数据失败：' + error.message)
  } finally {
    loading.value = false
  }
}

const initCharts = () => {
  initDepartmentChart()
  initStatusChart()
  initCategoryChart()
  initValueChart()
}

const initDepartmentChart = () => {
  if (!departmentChartRef.value) return

  departmentChart = echarts.init(departmentChartRef.value)
  const data = departmentStats.value.map(stat => ({
    name: stat.department,
    value: stat.count
  }))

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'center'
    },
    series: [{
      name: '部门分布',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '20',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: data
    }]
  }

  departmentChart.setOption(option)
}

const initStatusChart = () => {
  if (!statusChartRef.value) return

  statusChart = echarts.init(statusChartRef.value)
  const data = statusStats.value.map(stat => ({
    name: stat.status,
    value: stat.count
  }))

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'center'
    },
    series: [{
      name: '状态分布',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '20',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: data
    }]
  }

  statusChart.setOption(option)
}

const initCategoryChart = () => {
  if (!categoryChartRef.value) return

  categoryChart = echarts.init(categoryChartRef.value)
  const data = categoryStats.value.map(stat => ({
    name: stat.category,
    value: stat.count
  }))

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'center'
    },
    series: [{
      name: '类别分布',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '20',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: data
    }]
  }

  categoryChart.setOption(option)
}

const initValueChart = () => {
  if (!valueChartRef.value) return

  valueChart = echarts.init(valueChartRef.value)
  const data = departmentStats.value.map(stat => ({
    name: stat.department,
    value: Math.floor(Math.random() * 1000000) + 100000 // Mock value data
  }))

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.name)
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: (value) => {
          return '¥' + (value / 10000).toFixed(0) + '万'
        }
      }
    },
    series: [{
      name: '资产价值',
      type: 'bar',
      data: data.map(item => item.value),
      itemStyle: {
        color: '#409EFF'
      }
    }]
  }

  valueChart.setOption(option)
}

const exportStatistics = () => {
  ElMessage.info('统计报告导出功能开发中...')
}

// Handle window resize
const handleResize = () => {
  departmentChart?.resize()
  statusChart?.resize()
  categoryChart?.resize()
  valueChart?.resize()
}

onMounted(() => {
  fetchStatistics()
  window.addEventListener('resize', handleResize)
})

// Cleanup on unmount
import { onUnmounted, nextTick } from 'vue'
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  departmentChart?.dispose()
  statusChart?.dispose()
  categoryChart?.dispose()
  valueChart?.dispose()
})
</script>

<style scoped>
.statistics {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.main-card {
  min-height: calc(100vh - 40px);
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.statistics-cards {
  margin-bottom: 30px;
}

.stat-card {
  height: 120px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-icon .el-icon {
  font-size: 24px;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.value {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.net {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.depreciation {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.charts-section {
  margin-bottom: 30px;
}

.chart-card {
  height: 400px;
}

.chart-header {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

.chart-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.chart-container {
  height: 320px;
  padding: 20px;
}

.chart {
  width: 100%;
  height: 100%;
}

.detail-table-card {
  margin-top: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

.table-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

:deep(.el-tabs__header) {
  margin: 0;
  padding: 0 20px;
}

:deep(.el-tabs__content) {
  padding: 20px;
}

:deep(.el-table .cell) {
  white-space: nowrap;
}
</style>