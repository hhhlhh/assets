package com.assetmanagement.controller;

import com.assetmanagement.entity.FixedAsset;
import com.assetmanagement.service.FixedAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/assets")
@CrossOrigin(origins = "*")
public class FixedAssetController {

    @Autowired
    private FixedAssetService fixedAssetService;

    /**
     * 获取所有资产（分页）
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<FixedAsset> assetPage = fixedAssetService.findAll(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("assets", assetPage.getContent());
            response.put("currentPage", assetPage.getNumber());
            response.put("totalItems", assetPage.getTotalElements());
            response.put("totalPages", assetPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 根据ID获取资产
     */
    @GetMapping("/{id}")
    public ResponseEntity<FixedAsset> getAssetById(@PathVariable Long id) {
        Optional<FixedAsset> asset = fixedAssetService.findById(id);
        return asset.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据资产编号获取资产
     */
    @GetMapping("/code/{assetCode}")
    public ResponseEntity<FixedAsset> getAssetByCode(@PathVariable String assetCode) {
        Optional<FixedAsset> asset = fixedAssetService.findByAssetCode(assetCode);
        return asset.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建新资产
     */
    @PostMapping
    public ResponseEntity<?> createAsset(@Valid @RequestBody FixedAsset fixedAsset) {
        try {
            // 检查资产编号是否已存在
            if (fixedAsset.getAssetCode() != null &&
                fixedAssetService.findByAssetCode(fixedAsset.getAssetCode()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "资产编号已存在"));
            }

            FixedAsset savedAsset = fixedAssetService.save(fixedAsset);
            return ResponseEntity.ok(savedAsset);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新资产
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(@PathVariable Long id, @Valid @RequestBody FixedAsset fixedAssetDetails) {
        try {
            FixedAsset updatedAsset = fixedAssetService.update(id, fixedAssetDetails);
            return ResponseEntity.ok(updatedAsset);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 删除资产
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAsset(@PathVariable Long id) {
        try {
            fixedAssetService.deleteById(id);
            return ResponseEntity.ok().body(Map.of("message", "资产删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 搜索资产
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAssets(
            @RequestParam(required = false) String assetName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String assetStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
            Page<FixedAsset> assetPage = fixedAssetService.findByCriteria(
                    department, assetStatus, assetName, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("assets", assetPage.getContent());
            response.put("currentPage", assetPage.getNumber());
            response.put("totalItems", assetPage.getTotalElements());
            response.put("totalPages", assetPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            List<Object[]> departmentStats = fixedAssetService.countByDepartment();
            List<Object[]> statusStats = fixedAssetService.countByAssetStatus();
            Object[] valueStats = fixedAssetService.sumAssetValues();

            Map<String, Object> response = new HashMap<>();
            response.put("departmentStats", departmentStats);
            response.put("statusStats", statusStats);
            response.put("totalOriginalValue", valueStats[0]);
            response.put("totalNetValue", valueStats[1]);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}