package com.fixedasset.system.controller;

import com.fixedasset.system.dto.MaintenanceRecordDto;
import com.fixedasset.system.entity.MaintenanceType;
import com.fixedasset.system.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    @GetMapping
    public ResponseEntity<Page<MaintenanceRecordDto>> getAllMaintenanceRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MaintenanceRecordDto> records = maintenanceRecordService.getMaintenanceRecordsWithPagination(pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordDto> getMaintenanceRecordById(@PathVariable Long id) {
        return maintenanceRecordService.getMaintenanceRecordById(id)
                .map(record -> ResponseEntity.ok(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecordDto> createMaintenanceRecord(@Valid @RequestBody MaintenanceRecordDto dto) {
        MaintenanceRecordDto createdRecord = maintenanceRecordService.createMaintenanceRecord(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecordDto> updateMaintenanceRecord(@PathVariable Long id, @Valid @RequestBody MaintenanceRecordDto dto) {
        try {
            MaintenanceRecordDto updatedRecord = maintenanceRecordService.updateMaintenanceRecord(id, dto);
            return ResponseEntity.ok(updatedRecord);
        } catch (com.fixedasset.system.exception.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable Long id) {
        try {
            maintenanceRecordService.deleteMaintenanceRecord(id);
            return ResponseEntity.noContent().build();
        } catch (com.fixedasset.system.exception.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<MaintenanceRecordDto>> getMaintenanceRecordsByAssetId(@PathVariable Long assetId) {
        List<MaintenanceRecordDto> records = maintenanceRecordService.getMaintenanceRecordsByAssetId(assetId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/asset/{assetId}/paginated")
    public ResponseEntity<Page<MaintenanceRecordDto>> getMaintenanceRecordsByAssetIdPaginated(
            @PathVariable Long assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MaintenanceRecordDto> records = maintenanceRecordService.getMaintenanceRecordsByAssetId(assetId, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MaintenanceRecordDto>> getMaintenanceRecordsByType(@PathVariable MaintenanceType type) {
        List<MaintenanceRecordDto> records = maintenanceRecordService.getMaintenanceRecordsByType(type);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MaintenanceRecordDto>> getMaintenanceRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MaintenanceRecordDto> records = maintenanceRecordService.getMaintenanceRecordsByDateRange(startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/cost/{assetId}")
    public ResponseEntity<java.util.Map<String, Object>> getTotalMaintenanceCost(@PathVariable Long assetId) {
        double totalCost = maintenanceRecordService.getTotalMaintenanceCostByAsset(assetId);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("assetId", assetId);
        result.put("totalCost", totalCost);
        return ResponseEntity.ok(result);
    }
}