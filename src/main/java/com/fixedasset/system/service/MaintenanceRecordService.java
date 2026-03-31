package com.fixedasset.system.service;

import com.fixedasset.system.dto.MaintenanceRecordDto;
import com.fixedasset.system.entity.Asset;
import com.fixedasset.system.entity.MaintenanceRecord;
import com.fixedasset.system.entity.MaintenanceType;
import com.fixedasset.system.exception.ResourceNotFoundException;
import com.fixedasset.system.repository.AssetRepository;
import com.fixedasset.system.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaintenanceRecordService {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Autowired
    private AssetRepository assetRepository;

    public List<MaintenanceRecordDto> getAllMaintenanceRecords() {
        return maintenanceRecordRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Page<MaintenanceRecordDto> getMaintenanceRecordsWithPagination(Pageable pageable) {
        return maintenanceRecordRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<MaintenanceRecordDto> getMaintenanceRecordById(Long id) {
        return maintenanceRecordRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<MaintenanceRecordDto> getMaintenanceRecordsByAssetId(Long assetId) {
        return maintenanceRecordRepository.findByAssetId(assetId).stream()
                .map(this::convertToDto)
                .toList();
    }

    public Page<MaintenanceRecordDto> getMaintenanceRecordsByAssetId(Long assetId, Pageable pageable) {
        return maintenanceRecordRepository.findByAssetId(assetId, pageable).map(this::convertToDto);
    }

    public MaintenanceRecordDto createMaintenanceRecord(MaintenanceRecordDto dto) {
        // 验证资产是否存在
        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("资产不存在，ID: " + dto.getAssetId()));

        MaintenanceRecord record = convertToEntity(dto, asset);
        MaintenanceRecord savedRecord = maintenanceRecordRepository.save(record);
        return convertToDto(savedRecord);
    }

    public MaintenanceRecordDto updateMaintenanceRecord(Long id, MaintenanceRecordDto dto) {
        MaintenanceRecord existingRecord = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("维护记录不存在，ID: " + id));

        // 验证资产是否存在
        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("资产不存在，ID: " + dto.getAssetId()));

        updateEntityFromDto(existingRecord, dto, asset);
        MaintenanceRecord updatedRecord = maintenanceRecordRepository.save(existingRecord);
        return convertToDto(updatedRecord);
    }

    public void deleteMaintenanceRecord(Long id) {
        if (!maintenanceRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("维护记录不存在，ID: " + id);
        }
        maintenanceRecordRepository.deleteById(id);
    }

    public List<MaintenanceRecordDto> getMaintenanceRecordsByType(MaintenanceType type) {
        return maintenanceRecordRepository.findByType(type).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<MaintenanceRecordDto> getMaintenanceRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return maintenanceRecordRepository.findByMaintenanceDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .toList();
    }

    public double getTotalMaintenanceCostByAsset(Long assetId) {
        Double totalCost = maintenanceRecordRepository.getTotalMaintenanceCostByAsset(assetId);
        return totalCost != null ? totalCost : 0.0;
    }

    private MaintenanceRecordDto convertToDto(MaintenanceRecord record) {
        MaintenanceRecordDto dto = new MaintenanceRecordDto();
        dto.setId(record.getId());
        dto.setAssetId(record.getAsset().getId());
        dto.setMaintenanceDate(record.getMaintenanceDate());
        dto.setType(record.getType());
        dto.setDescription(record.getDescription());
        dto.setCost(record.getCost());
        dto.setTechnician(record.getTechnician());
        dto.setLocation(record.getLocation());
        return dto;
    }

    private MaintenanceRecord convertToEntity(MaintenanceRecordDto dto, Asset asset) {
        MaintenanceRecord record = new MaintenanceRecord();
        updateEntityFromDto(record, dto, asset);
        return record;
    }

    private void updateEntityFromDto(MaintenanceRecord record, MaintenanceRecordDto dto, Asset asset) {
        record.setAsset(asset);
        record.setMaintenanceDate(dto.getMaintenanceDate());
        record.setType(dto.getType());
        record.setDescription(dto.getDescription());
        record.setCost(dto.getCost());
        record.setTechnician(dto.getTechnician());
        record.setLocation(dto.getLocation());
    }
}