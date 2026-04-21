<template>
  <div class="add-asset">
    <el-card>
      <div class="header">
        <h2>新增固定资产</h2>
        <el-button @click="goBack">返回列表</el-button>
      </div>

      <el-form
        ref="formRef"
        :model="assetForm"
        :rules="rules"
        label-width="120px"
        class="asset-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产编号" prop="assetCode">
              <el-input v-model="assetForm.assetCode" placeholder="请输入资产编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资产名称" prop="assetName">
              <el-input v-model="assetForm.assetName" placeholder="请输入资产名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产类别" prop="assetCategory">
              <el-select v-model="assetForm.assetCategory" placeholder="请选择资产类别" style="width: 100%">
                <el-option label="电子设备" value="电子设备" />
                <el-option label="办公设备" value="办公设备" />
                <el-option label="办公家具" value="办公家具" />
                <el-option label="运输设备" value="运输设备" />
                <el-option label="其他设备" value="其他设备" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格型号" prop="specification">
              <el-input v-model="assetForm.specification" placeholder="请输入规格型号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="使用部门" prop="department">
              <el-select v-model="assetForm.department" placeholder="请选择使用部门" style="width: 100%">
                <el-option label="技术部" value="技术部" />
                <el-option label="行政部" value="行政部" />
                <el-option label="财务部" value="财务部" />
                <el-option label="人事部" value="人事部" />
                <el-option label="市场部" value="市场部" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用人" prop="userName">
              <el-input v-model="assetForm.userName" placeholder="请输入使用人" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="存放地点" prop="location">
              <el-input v-model="assetForm.location" placeholder="请输入存放地点" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资产状态" prop="assetStatus">
              <el-select v-model="assetForm.assetStatus" placeholder="请选择资产状态" style="width: 100%">
                <el-option label="在用" value="在用" />
                <el-option label="闲置" value="闲置" />
                <el-option label="维修中" value="维修中" />
                <el-option label="报废" value="报废" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="原值" prop="originalValue">
              <el-input-number
                v-model="assetForm.originalValue"
                :min="0"
                :precision="2"
                :step="0.01"
                placeholder="请输入原值"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="净值" prop="netValue">
              <el-input-number
                v-model="assetForm.netValue"
                :min="0"
                :precision="2"
                :step="0.01"
                placeholder="请输入净值"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始使用日期" prop="startDate">
              <el-date-picker
                v-model="assetForm.startDate"
                type="date"
                placeholder="请选择开始使用日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input v-model="assetForm.brand" placeholder="请输入品牌" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="assetForm.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            保存
          </el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAssetStore } from '../stores/asset'
import { ElMessage } from 'element-plus'

const router = useRouter()
const assetStore = useAssetStore()
const formRef = ref()
const loading = ref(false)

const assetForm = reactive({
  assetCode: '',
  assetName: '',
  assetCategory: '',
  specification: '',
  department: '',
  userName: '',
  location: '',
  assetStatus: '在用',
  originalValue: null,
  netValue: null,
  startDate: null,
  brand: '',
  remarks: ''
})

const rules = {
  assetCode: [
    { required: true, message: '请输入资产编号', trigger: 'blur' }
  ],
  assetName: [
    { required: true, message: '请输入资产名称', trigger: 'blur' }
  ],
  assetCategory: [
    { required: true, message: '请选择资产类别', trigger: 'change' }
  ],
  department: [
    { required: true, message: '请选择使用部门', trigger: 'change' }
  ],
  assetStatus: [
    { required: true, message: '请选择资产状态', trigger: 'change' }
  ]
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const formData = { ...assetForm }

        // 处理日期格式
        if (formData.startDate) {
          formData.startDate = new Date(formData.startDate).toISOString().split('T')[0]
        }

        await assetStore.createAsset(formData)
        ElMessage.success('资产创建成功')
        router.push('/assets')
      } catch (error) {
        ElMessage.error('创建失败：' + error.message)
      } finally {
        loading.value = false
      }
    }
  })
}

const goBack = () => {
  router.push('/assets')
}
</script>

<style scoped>
.add-asset {
  max-width: 1000px;
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

.asset-form {
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>