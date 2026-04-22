import axios from 'axios'

const API_BASE_URL = '/api'

// 资产相关API
export const assetApi = {
  // 搜索资产
  search: (searchParams) => {
    return axios.post(`${API_BASE_URL}/assets/search`, searchParams)
  },

  // 获取所有资产
  getAll: () => {
    return axios.get(`${API_BASE_URL}/assets`)
  },

  // 根据ID获取资产
  getById: (id) => {
    return axios.get(`${API_BASE_URL}/assets/${id}`)
  },

  // 根据资产编号获取资产
  getByCode: (assetCode) => {
    return axios.get(`${API_BASE_URL}/assets/code/${assetCode}`)
  },

  // 创建资产
  create: (assetData) => {
    return axios.post(`${API_BASE_URL}/assets`, assetData)
  },

  // 更新资产
  update: (id, assetData) => {
    return axios.put(`${API_BASE_URL}/assets/${id}`, assetData)
  },

  // 删除资产
  delete: (id) => {
    return axios.delete(`${API_BASE_URL}/assets/${id}`)
  },

  // 批量删除
  batchDelete: (ids) => {
    return axios.post(`${API_BASE_URL}/assets/batch-delete`, ids)
  },

  // 导出Excel
  exportExcel: () => {
    return axios.get(`${API_BASE_URL}/assets/export/excel`, {
      responseType: 'blob'
    })
  },

  // 导出CSV
  exportCsv: () => {
    return axios.get(`${API_BASE_URL}/assets/export/csv`, {
      responseType: 'blob'
    })
  },

  // 获取统计数据
  getStatistics: () => {
    return axios.get(`${API_BASE_URL}/assets/statistics`)
  },

  // 导入Excel
  importExcel: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return axios.post(`${API_BASE_URL}/assets/import/excel`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}