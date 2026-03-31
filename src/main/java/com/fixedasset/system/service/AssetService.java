package com.fixedasset.system.service;

import com.fixedasset.system.dto.AssetDto;
import com.fixedasset.system.entity.Asset;
import com.fixedasset.system.entity.AssetStatus;
import com.fixedasset.system.entity.AssetType;
import com.fixedasset.system.exception.DuplicateResourceException;
import com.fixedasset.system.exception.ResourceNotFoundException;
import com.fixedasset.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.List;

@Service
@Transactional
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<AssetDto> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Page<AssetDto> getAssetsWithPagination(Pageable pageable) {
        return assetRepository.findAll(pageable).map(this::convertToDto);
    }

    public Optional<AssetDto> getAssetById(Long id) {
        return assetRepository.findById(id)
                .map(this::convertToDto);
    }

    public AssetDto createAsset(AssetDto assetDto) {
        if (assetRepository.existsBySerialNumber(assetDto.getSerialNumber())) {
            throw new DuplicateResourceException("序列号已存在: " + assetDto.getSerialNumber());
        }

        Asset asset = convertToEntity(assetDto);
        Asset savedAsset = assetRepository.save(asset);
        return convertToDto(savedAsset);
    }

    public AssetDto updateAsset(Long id, AssetDto assetDto) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("资产不存在，ID: " + id));

        // 检查序列号的唯一性（排除当前资产）
        if (!existingAsset.getSerialNumber().equals(assetDto.getSerialNumber()) &&
                assetRepository.existsBySerialNumber(assetDto.getSerialNumber())) {
            throw new DuplicateResourceException("序列号已存在: " + assetDto.getSerialNumber());
        }

        updateEntityFromDto(existingAsset, assetDto);
        Asset updatedAsset = assetRepository.save(existingAsset);
        return convertToDto(updatedAsset);
    }

    public void deleteAsset(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new ResourceNotFoundException("资产不存在，ID: " + id);
        }
        assetRepository.deleteById(id);
    }

    public Page<AssetDto> searchAssets(String keyword, Pageable pageable) {
        return assetRepository.searchAssets(keyword, pageable).map(this::convertToDto);
    }

    public List<AssetDto> getAssetsByType(AssetType type) {
        return assetRepository.findByType(type).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<AssetDto> getAssetsByStatus(AssetStatus status) {
        return assetRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .toList();
    }

    public long getTotalAssetCount() {
        return assetRepository.countAllAssets();
    }

    public long getAssetCountByType(AssetType type) {
        return assetRepository.countByType(type);
    }

    public long getAssetCountByStatus(AssetStatus status) {
        return assetRepository.countByStatus(status);
    }

    public void updateAssetStatus(Long id, AssetStatus newStatus) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("资产不存在，ID: " + id));
        asset.setStatus(newStatus);
        assetRepository.save(asset);
    }

    private AssetDto convertToDto(Asset asset) {
        AssetDto dto = new AssetDto();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setType(asset.getType());
        dto.setModel(asset.getModel());
        dto.setSerialNumber(asset.getSerialNumber());
        dto.setPurchaseDate(asset.getPurchaseDate());
        dto.setPrice(asset.getPrice());
        dto.setStatus(asset.getStatus());
        dto.setLocation(asset.getLocation());
        dto.setDescription(asset.getDescription());
        dto.setAssignedTo(asset.getAssignedTo());
        dto.setDepartment(asset.getDepartment());
        return dto;
    }

    private Asset convertToEntity(AssetDto dto) {
        Asset asset = new Asset();
        updateEntityFromDto(asset, dto);
        return asset;
    }

    private void updateEntityFromDto(Asset asset, AssetDto dto) {
        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setModel(dto.getModel());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setPurchaseDate(dto.getPurchaseDate());
        asset.setPrice(dto.getPrice());
        asset.setStatus(dto.getStatus());
        asset.setLocation(dto.getLocation());
        asset.setDescription(dto.getDescription());
        asset.setAssignedTo(dto.getAssignedTo());
        asset.setDepartment(dto.getDepartment());
    }
}