<template>
  <el-config-provider>
    <el-container style="min-height: 100vh">
      <el-header style="background-color: #409EFF; color: white; padding: 0 20px;">
        <div class="header-content">
          <h1 style="margin: 0; line-height: 60px;">固定资产管理系统</h1>
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            background-color="#409EFF"
            text-color="#fff"
            active-text-color="#ffd04b"
            @select="handleMenuSelect"
          >
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/assets">资产管理</el-menu-item>
            <el-menu-item index="/statistics">统计报表</el-menu-item>
          </el-menu>
        </div>
      </el-header>

      <el-main style="padding: 20px;">
        <router-view />
      </el-main>

      <el-footer style="text-align: center; color: #666; padding: 20px;">
        固定资产管理系统 ©2024 Created by Claude Code
      </el-footer>
    </el-container>
  </el-config-provider>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

const handleMenuSelect = (index) => {
  router.push(index)
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  background-color: #f5f5f5;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.el-menu--horizontal {
  border-bottom: none;
}

.el-main {
  background-color: #f5f5f5;
}
</style>