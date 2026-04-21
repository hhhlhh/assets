package com.assetmanagement.controller;

import com.assetmanagement.dto.AssetSearchDto;
import com.assetmanagement.entity.Asset;
import com.assetmanagement.service.AssetService;
import com.assetmanagement.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 获取资产列表（分页+搜索）
     */
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAssets(@RequestBody AssetSearchDto searchDto) {
        try {
            Page<Asset> assetPage = assetService.searchAssets(searchDto);

            Map<String, Object> response = new HashMap<>();
            response.put("assets", assetPage.getContent());
            response.put("currentPage", assetPage.getNumber());
            response.put("totalItems", assetPage.getTotalElements());
            response.put("totalPages", assetPage.getTotalPages());
            response.put("pageSize", assetPage.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 获取所有资产（简单列表）
     */
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        try {
            List<Asset> assets = assetService.findAll();
            return ResponseEntity.ok(assets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 根据ID获取资产
     */
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return assetService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据资产编号获取资产
     */
    @GetMapping("/code/{assetCode}")
    public ResponseEntity<Asset> getAssetByCode(@PathVariable String assetCode) {
        return assetService.findByAssetCode(assetCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建新资产
     */
    @PostMapping
    public ResponseEntity<?> createAsset(@Valid @RequestBody Asset asset) {
        try {
            Asset savedAsset = assetService.save(asset);
            return ResponseEntity.ok(savedAsset);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新资产
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(@PathVariable Long id, @Valid @RequestBody Asset assetDetails) {
        try {
            Asset updatedAsset = assetService.update(id, assetDetails);
            return ResponseEntity.ok(updatedAsset);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 删除资产
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAsset(@PathVariable Long id) {
        try {
            assetService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "资产删除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 批量删除资产
     */
    @PostMapping("/batch-delete")
    public ResponseEntity<?> batchDeleteAssets(@RequestBody List<Long> ids) {
        try {
            assetService.deleteBatch(ids);
            return ResponseEntity.ok(Map.of("message", "批量删除成功", "count", ids.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 导出Excel
     */
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) {
        try {
            List<Asset> assets = assetService.findAll();
            excelExportService.exportToExcel(assets, response);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败: " + e.getMessage());
        }
    }

    /**
     * 导出CSV
     */
    @GetMapping("/export/csv")
    public void exportToCsv(HttpServletResponse response) {
        try {
            List<Asset> assets = assetService.findAll();
            excelExportService.exportToCsv(assets, response);
        } catch (IOException e) {
            throw new RuntimeException("导出CSV失败: " + e.getMessage());
        }
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();

            // 部门统计
            List<Object[]> departmentStats = assetService.countByDepartment();
            statistics.put("departmentStats", departmentStats);

            // 状态统计
            List<Object[]> statusStats = assetService.countByAssetStatus();
            statistics.put("statusStats", statusStats);

            // 分类统计
            List<Object[]> categoryStats = assetService.countByAssetCategory();
            statistics.put("categoryStats", categoryStats);

            // 价值统计
            Object[] valueStats = assetService.sumAssetValues();
            statistics.put("totalOriginalValue", valueStats[0] != null ? valueStats[0] : 0);
            statistics.put("totalNetValue", valueStats[1] != null ? valueStats[1] : 0);

            // 总数统计
            long totalCount = assetService.findAll().size();
            statistics.put("totalCount", totalCount);

            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}