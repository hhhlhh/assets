import { defineStore } from 'pinia'
import axios from 'axios'

const API_BASE_URL = '/api'

export const useAssetStore = defineStore('asset', {
  state: () => ({
    assets: [],
    currentAsset: null,
    loading: false,
    error: null,
    totalItems: 0,
    currentPage: 0,
    totalPages: 0
  }),

  getters: {
    assetCount: (state) => state.assets.length,
    isLoading: (state) => state.loading
  },

  actions: {
    async fetchAssets(page = 0, size = 10, sortBy = 'id') {
      this.loading = true
      this.error = null

      try {
        const response = await axios.get(`${API_BASE_URL}/assets`, {
          params: { page, size, sortBy }
        })

        this.assets = response.data.assets
        this.totalItems = response.data.totalItems
        this.currentPage = response.data.currentPage
        this.totalPages = response.data.totalPages

        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async searchAssets(assetName, department, assetStatus, page = 0, size = 10) {
      this.loading = true
      this.error = null

      try {
        const response = await axios.get(`${API_BASE_URL}/assets/search`, {
          params: { assetName, department, assetStatus, page, size }
        })

        this.assets = response.data.assets
        this.totalItems = response.data.totalItems
        this.currentPage = response.data.currentPage
        this.totalPages = response.data.totalPages

        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async createAsset(assetData) {
      this.loading = true
      this.error = null

      try {
        const response = await axios.post(`${API_BASE_URL}/assets`, assetData)
        this.currentAsset = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.error || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateAsset(id, assetData) {
      this.loading = true
      this.error = null

      try {
        const response = await axios.put(`${API_BASE_URL}/assets/${id}`, assetData)
        this.currentAsset = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.error || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteAsset(id) {
      this.loading = true
      this.error = null

      try {
        await axios.delete(`${API_BASE_URL}/assets/${id}`)
        this.assets = this.assets.filter(asset => asset.id !== id)
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async getAssetById(id) {
      this.loading = true
      this.error = null

      try {
        const response = await axios.get(`${API_BASE_URL}/assets/${id}`)
        this.currentAsset = response.data
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async getStatistics() {
      this.loading = true
      this.error = null

      try {
        const response = await axios.get(`${API_BASE_URL}/assets/statistics`)
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})