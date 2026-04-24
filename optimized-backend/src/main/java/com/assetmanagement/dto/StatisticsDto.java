package com.assetmanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsDto {
    public static class CategoryStatDto {
        private String category;
        private Long count;

        public CategoryStatDto(String category, Long count) {
            this.category = category;
            this.count = count;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public static class StatusStatDto {
        private String status;
        private Long count;

        public StatusStatDto(String status, Long count) {
            this.status = status;
            this.count = count;
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public static class DepartmentStatDto {
        private String department;
        private Long count;

        public DepartmentStatDto(String department, Long count) {
            this.department = department;
            this.count = count;
        }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    private List<CategoryStatDto> categoryStats;
    private List<StatusStatDto> statusStats;
    private List<DepartmentStatDto> departmentStats;
    private BigDecimal totalOriginalValue;
    private BigDecimal totalNetValue;
    private Long totalCount;

    public StatisticsDto() {}

    public StatisticsDto(List<CategoryStatDto> categoryStats,
                        List<StatusStatDto> statusStats,
                        List<DepartmentStatDto> departmentStats,
                        BigDecimal totalOriginalValue,
                        BigDecimal totalNetValue,
                        Long totalCount) {
        this.categoryStats = categoryStats;
        this.statusStats = statusStats;
        this.departmentStats = departmentStats;
        this.totalOriginalValue = totalOriginalValue;
        this.totalNetValue = totalNetValue;
        this.totalCount = totalCount;
    }

    public List<CategoryStatDto> getCategoryStats() { return categoryStats; }
    public void setCategoryStats(List<CategoryStatDto> categoryStats) { this.categoryStats = categoryStats; }
    public List<StatusStatDto> getStatusStats() { return statusStats; }
    public void setStatusStats(List<StatusStatDto> statusStats) { this.statusStats = statusStats; }
    public List<DepartmentStatDto> getDepartmentStats() { return departmentStats; }
    public void setDepartmentStats(List<DepartmentStatDto> departmentStats) { this.departmentStats = departmentStats; }
    public BigDecimal getTotalOriginalValue() { return totalOriginalValue; }
    public void setTotalOriginalValue(BigDecimal totalOriginalValue) { this.totalOriginalValue = totalOriginalValue; }
    public BigDecimal getTotalNetValue() { return totalNetValue; }
    public void setTotalNetValue(BigDecimal totalNetValue) { this.totalNetValue = totalNetValue; }
    public Long getTotalCount() { return totalCount; }
    public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
}