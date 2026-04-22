<template>
  <el-form
    :model="formData"
    :rules="rules"
    ref="formRef"
    label-width="120px"
    class="asset-form"
  >
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="资产编号" prop="assetCode">
          <el-input v-model="formData.assetCode" placeholder="请输入资产编号" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="资产名称" prop="assetName">
          <el-input v-model="formData.assetName" placeholder="请输入资产名称" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="资产类别" prop="assetCategory">
          <el-select v-model="formData.assetCategory" placeholder="请选择资产类别" style="width: 100%">
            <el-option label="电子设备" value="电子设备" />
            <el-option label="办公设备" value="办公设备" />
            <el-option label="办公家具" value="办公家具" />
            <el-option label="其他设备" value="其他设备" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="使用部门" prop="department">
          <el-select v-model="formData.department" placeholder="请选择使用部门" style="width: 100%">
            <el-option label="技术部" value="技术部" />
            <el-option label="行政部" value="行政部" />
            <el-option label="财务部" value="财务部" />
            <el-option label="人事部" value="人事部" />
            <el-option label="市场部" value="市场部" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="使用人" prop="userName">
          <el-input v-model="formData.userName" placeholder="请输入使用人" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="存放地点" prop="location">
          <el-input v-model="formData.location" placeholder="请输入存放地点" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="资产状态" prop="assetStatus">
          <el-select v-model="formData.assetStatus" placeholder="请选择资产状态" style="width: 100%">
            <el-option label="在用" value="在用" />
            <el-option label="闲置" value="闲置" />
            <el-option label="维修中" value="维修中" />
            <el-option label="报废" value="报废" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="开始使用日期" prop="startDate">
          <el-date-picker
            v-model="formData.startDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="原值" prop="originalValue">
          <el-input-number
            v-model="formData.originalValue"
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
            v-model="formData.netValue"
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
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="formData.brand" placeholder="请输入品牌" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="规格型号" prop="specification">
          <el-input v-model="formData.specification" placeholder="请输入规格型号" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="负责人" prop="responsiblePerson">
          <el-input v-model="formData.responsiblePerson" placeholder="请输入负责人" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="财务卡片编号" prop="financeCardCode">
          <el-input v-model="formData.financeCardCode" placeholder="请输入财务卡片编号" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="累计折旧" prop="accumulatedDepreciation">
          <el-input-number
            v-model="formData.accumulatedDepreciation"
            :min="0"
            :precision="2"
            :step="0.01"
            placeholder="请输入累计折旧"
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="使用年限" prop="usefulLife">
          <el-input-number
            v-model="formData.usefulLife"
            :min="1"
            :max="50"
            placeholder="请输入使用年限（年）"
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="取得方式" prop="acquisitionMethod">
          <el-select v-model="formData.acquisitionMethod" placeholder="请选择取得方式" style="width: 100%">
            <el-option label="购买" value="购买" />
            <el-option label="捐赠" value="捐赠" />
            <el-option label="调拨" value="调拨" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="资金来源" prop="fundingSource">
          <el-input v-model="formData.fundingSource" placeholder="请输入资金来源" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="备注" prop="remarks">
      <el-input
        v-model="formData.remarks"
        type="textarea"
        rows="3"
        placeholder="请输入备注信息"
      />
    </el-form-item>

    <div class="form-actions">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        {{ editMode ? '更新' : '创建' }}
      </el-button>
    </div>
  </el-form>
</template>

<script setup>
import { ref, reactive, watch, defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  assetData: {
    type: Object,
    default: () => null
  },
  editMode: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['submit', 'cancel'])

const formRef = ref(null)
const submitting = ref(false)

const formData = reactive({
  assetCode: '',
  assetName: '',
  assetCategory: '',
  department: '',
  userName: '',
  location: '',
  assetStatus: '在用',
  originalValue: null,
  netValue: null,
  brand: '',
  specification: '',
  startDate: '',
  responsiblePerson: '',
  remarks: '',
  financeCardCode: '',
  accumulatedDepreciation: null,
  usefulLife: null,
  acquisitionMethod: '',
  fundingSource: ''
})

const rules = {
  assetCode: [
    { required: true, message: '请输入资产编号', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  assetName: [
    { required: true, message: '请输入资产名称', trigger: 'blur' },
    { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
  ],
  assetCategory: [
    { required: true, message: '请选择资产类别', trigger: 'change' }
  ],
  department: [
    { required: true, message: '请选择使用部门', trigger: 'change' }
  ],
  assetStatus: [
    { required: true, message: '请选择资产状态', trigger: 'change' }
  ],
  originalValue: [
    { type: 'number', min: 0, message: '原值必须大于等于0', trigger: 'blur' }
  ],
  netValue: [
    { type: 'number', min: 0, message: '净值必须大于等于0', trigger: 'blur' }
  ]
}

// Watch for prop changes to populate form in edit mode
watch(
  () => props.assetData,
  (newData) => {
    if (newData) {
      Object.assign(formData, newData)
    }
  },
  { immediate: true }
)

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitting.value = true

    // Emit submit event with form data
    emit('submit', { ...formData })
  } catch (error) {
    console.error('Validation failed:', error)
    ElMessage.error('请检查表单填写')
  } finally {
    submitting.value = false
  }
}

const handleCancel = () => {
  emit('cancel')
}

// Reset form when switching between add/edit modes
watch(
  () => props.editMode,
  (isEdit) => {
    if (!isEdit) {
      // Reset form for new asset creation
      Object.assign(formData, {
        assetCode: '',
        assetName: '',
        assetCategory: '',
        department: '',
        userName: '',
        location: '',
        assetStatus: '在用',
        originalValue: null,
        netValue: null,
        brand: '',
        specification: '',
        startDate: '',
        responsiblePerson: '',
        remarks: '',
        financeCardCode: '',
        accumulatedDepreciation: null,
        usefulLife: null,
        acquisitionMethod: '',
        fundingSource: ''
      })
    }
  }
)
</script>

<style scoped>
.asset-form {
  max-height: 70vh;
  overflow-y: auto;
  padding: 20px 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-input-number) {
  width: 100%;
}
</style>