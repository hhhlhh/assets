package com.assetmanagement.service;

import com.assetmanagement.dto.AssetDto;
import com.assetmanagement.dto.AssetSearchDto;
import com.assetmanagement.entity.Asset;
import com.assetmanagement.exception.DuplicateResourceException;
import com.assetmanagement.exception.ResourceNotFoundException;
import com.assetmanagement.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    /**
     * 保存资产
     */
    public Asset save(Asset asset) {
        // 检查资产编号是否已存在
        if (asset.getId() == null && assetRepository.existsByAssetCode(asset.getAssetCode())) {
            throw new DuplicateResourceException("Asset", "assetCode", asset.getAssetCode());
        }
        return assetRepository.save(asset);
    }

    /**
     * 根据ID查找资产
     */
    public Optional<Asset> findById(Long id) {
        return assetRepository.findById(id);
    }

    /**
     * 根据资产编号查找
     */
    public Optional<Asset> findByAssetCode(String assetCode) {
        return assetRepository.findByAssetCode(assetCode);
    }

    /**
     * 查找所有资产
     */
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    /**
     * 分页查找所有资产
     */
    public Page<Asset> findAll(Pageable pageable) {
        return assetRepository.findAll(pageable);
    }

    /**
     * 多条件搜索
     */
    public Page<Asset> searchAssets(AssetSearchDto searchDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(searchDto.getSortDirection()), searchDto.getSortBy());
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);

        return assetRepository.findBySearchCriteria(
                searchDto.getAssetName(),
                searchDto.getAssetCode(),
                searchDto.getDepartment(),
                searchDto.getAssetStatus(),
                searchDto.getAssetCategory(),
                searchDto.getUserName(),
                searchDto.getLocation(),
                pageable);
    }

    /**
     * 更新资产
     */
    public Asset update(Long id, Asset assetDetails) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", "id", id));

        // 如果修改了资产编号，检查是否重复
        if (!asset.getAssetCode().equals(assetDetails.getAssetCode()) &&
            assetRepository.existsByAssetCode(assetDetails.getAssetCode())) {
            throw new DuplicateResourceException("Asset", "assetCode", assetDetails.getAssetCode());
        }

        updateAssetFields(asset, assetDetails);
        return assetRepository.save(asset);
    }

    /**
     * 删除资产
     */
    public void deleteById(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asset", "id", id);
        }
        assetRepository.deleteById(id);
    }

    /**
     * 批量删除资产
     */
    public void deleteBatch(List<Long> ids) {
        List<Asset> assets = assetRepository.findAllById(ids);
        assetRepository.deleteAll(assets);
    }

    /**
     * 统计各部门资产数量
     */
    public List<Object[]> countByDepartment() {
        return assetRepository.countByDepartment();
    }

    /**
     * 统计各状态资产数量
     */
    public List<Object[]> countByAssetStatus() {
        return assetRepository.countByAssetStatus();
    }

    /**
     * 统计各分类资产数量
     */
    public List<Object[]> countByAssetCategory() {
        return assetRepository.countByAssetCategory();
    }

    /**
     * 统计资产总价值
     */
    public Object[] sumAssetValues() {
        return assetRepository.sumAssetValues();
    }

    /**
     * 更新资产字段
     */
    private void updateAssetFields(Asset existing, Asset details) {
        if (details.getAssetCode() != null) existing.setAssetCode(details.getAssetCode());
        if (details.getAssetName() != null) existing.setAssetName(details.getAssetName());
        if (details.getAssetCategory() != null) existing.setAssetCategory(details.getAssetCategory());
        if (details.getDepartment() != null) existing.setDepartment(details.getDepartment());
        if (details.getUserName() != null) existing.setUserName(details.getUserName());
        if (details.getAssetStatus() != null) existing.setAssetStatus(details.getAssetStatus());
        if (details.getLocation() != null) existing.setLocation(details.getLocation());
        if (details.getOriginalValue() != null) existing.setOriginalValue(details.getOriginalValue());
        if (details.getNetValue() != null) existing.setNetValue(details.getNetValue());
        if (details.getSpecification() != null) existing.setSpecification(details.getSpecification());
        if (details.getBrand() != null) existing.setBrand(details.getBrand());
        if (details.getStartDate() != null) existing.setStartDate(details.getStartDate());
        if (details.getResponsiblePerson() != null) existing.setResponsiblePerson(details.getResponsiblePerson());
        if (details.getRemarks() != null) existing.setRemarks(details.getRemarks());
    }
}