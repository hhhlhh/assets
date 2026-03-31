package com.fixedasset.system.controller;

import com.fixedasset.system.dto.AssetDto;
import com.fixedasset.system.entity.AssetStatus;
import com.fixedasset.system.entity.AssetType;
import com.fixedasset.system.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping
    public ResponseEntity<Page<AssetDto>> getAllAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetDto> assets = assetService.getAssetsWithPagination(pageable);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetDto> getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id)
                .map(asset -> ResponseEntity.ok(asset))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AssetDto> createAsset(@Valid @RequestBody AssetDto assetDto) {
        AssetDto createdAsset = assetService.createAsset(assetDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetDto> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetDto assetDto) {
        try {
            AssetDto updatedAsset = assetService.updateAsset(id, assetDto);
            return ResponseEntity.ok(updatedAsset);
        } catch (com.fixedasset.system.exception.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        try {
            assetService.deleteAsset(id);
            return ResponseEntity.noContent().build();
        } catch (com.fixedasset.system.exception.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AssetDto>> searchAssets(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetDto> assets = assetService.searchAssets(keyword, pageable);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<java.util.List<AssetDto>> getAssetsByType(@PathVariable AssetType type) {
        java.util.List<AssetDto> assets = assetService.getAssetsByType(type);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<java.util.List<AssetDto>> getAssetsByStatus(@PathVariable AssetStatus status) {
        java.util.List<AssetDto> assets = assetService.getAssetsByStatus(status);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/stats/total")
    public ResponseEntity<java.util.Map<String, Object>> getTotalAssetCount() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("total", assetService.getTotalAssetCount());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/by-type")
    public ResponseEntity<java.util.Map<String, Long>> getAssetCountByType() {
        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        for (AssetType type : AssetType.values()) {
            stats.put(type.name(), assetService.getAssetCountByType(type));
        }
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<java.util.Map<String, Long>> getAssetCountByStatus() {
        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        for (AssetStatus status : AssetStatus.values()) {
            stats.put(status.name(), assetService.getAssetCountByStatus(status));
        }
        return ResponseEntity.ok(stats);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateAssetStatus(@PathVariable Long id, @RequestBody com.fixedasset.system.entity.AssetStatus newStatus) {
        try {
            assetService.updateAssetStatus(id, newStatus);
            return ResponseEntity.ok().build();
        } catch (com.fixedasset.system.exception.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}