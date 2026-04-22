<template>
  <el-config-provider :locale="zhCn">
    <div id="app">
      <el-container style="min-height: 100vh;">
        <el-header class="app-header">
          <div class="header-content">
            <div class="logo">
              <el-icon><OfficeBuilding /></el-icon>
              <span class="title">固定资产管理系统</span>
            </div>
            <div class="nav-menu">
              <el-menu
                :default-active="activeMenu"
                mode="horizontal"
                background-color="#409EFF"
                text-color="#fff"
                active-text-color="#ffd04b"
                @select="handleMenuSelect"
              >
                <el-menu-item index="assets">
                  <el-icon><Menu /></el-icon>
                  资产管理
                </el-menu-item>
                <el-menu-item index="statistics">
                  <el-icon><DataAnalysis /></el-icon>
                  统计报表
                </el-menu-item>
              </el-menu>
            </div>
          </div>
        </el-header>

        <el-main class="app-main">
          <router-view />
        </el-main>

        <el-footer class="app-footer">
          <div class="footer-content">
            <p>&copy; 2024 固定资产管理系统. All rights reserved.</p>
          </div>
        </el-footer>
      </el-container>
    </div>
  </el-config-provider>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { OfficeBuilding, Menu, DataAnalysis } from '@element-plus/icons-vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => {
  const path = route.path
  if (path.includes('/statistics')) return 'statistics'
  return 'assets'
})

const handleMenuSelect = (index) => {
  if (index === 'assets') {
    router.push('/')
  } else if (index === 'statistics') {
    router.push('/statistics')
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  background-color: #f5f5f5;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.app-header {
  background-color: #409EFF;
  color: white;
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo .el-icon {
  font-size: 24px;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.nav-menu {
  height: 60px;
}

.nav-menu .el-menu {
  height: 60px;
  border-bottom: none;
}

.nav-menu .el-menu-item {
  height: 60px;
  line-height: 60px;
  font-size: 16px;
}

.nav-menu .el-menu-item .el-icon {
  margin-right: 5px;
}

.app-main {
  padding: 0;
  background-color: #f5f5f5;
  min-height: calc(100vh - 120px);
}

.app-footer {
  background-color: #303133;
  color: #909399;
  padding: 0;
  height: 60px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer-content p {
  margin: 0;
  font-size: 14px;
}

/* Responsive design */
@media (max-width: 768px) {
  .header-content {
    padding: 0 10px;
  }

  .title {
    font-size: 16px;
  }

  .nav-menu .el-menu-item {
    font-size: 14px;
    padding: 0 15px;
  }

  .footer-content {
    padding: 0 10px;
  }

  .footer-content p {
    font-size: 12px;
  }
}

/* Animation */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>