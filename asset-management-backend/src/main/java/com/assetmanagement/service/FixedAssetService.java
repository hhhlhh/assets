package com.assetmanagement.service;

import com.assetmanagement.entity.FixedAsset;
import com.assetmanagement.repository.FixedAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FixedAssetService {

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    /**
     * 保存资产
     */
    public FixedAsset save(FixedAsset fixedAsset) {
        return fixedAssetRepository.save(fixedAsset);
    }

    /**
     * 根据ID查找资产
     */
    public Optional<FixedAsset> findById(Long id) {
        return fixedAssetRepository.findById(id);
    }

    /**
     * 根据资产编号查找
     */
    public Optional<FixedAsset> findByAssetCode(String assetCode) {
        return fixedAssetRepository.findByAssetCode(assetCode);
    }

    /**
     * 查找所有资产
     */
    public List<FixedAsset> findAll() {
        return fixedAssetRepository.findAll();
    }

    /**
     * 分页查找所有资产
     */
    public Page<FixedAsset> findAll(Pageable pageable) {
        return fixedAssetRepository.findAll(pageable);
    }

    /**
     * 根据部门查找资产
     */
    public List<FixedAsset> findByDepartment(String department) {
        return fixedAssetRepository.findByDepartment(department);
    }

    /**
     * 根据资产状态查找资产
     */
    public List<FixedAsset> findByAssetStatus(String assetStatus) {
        return fixedAssetRepository.findByAssetStatus(assetStatus);
    }

    /**
     * 根据资产名称模糊查询
     */
    public List<FixedAsset> findByAssetNameContaining(String assetName) {
        return fixedAssetRepository.findByAssetNameContaining(assetName);
    }

    /**
     * 组合条件查询
     */
    public Page<FixedAsset> findByCriteria(String department, String assetStatus, String assetName, Pageable pageable) {
        return fixedAssetRepository.findByCriteria(department, assetStatus, assetName, pageable);
    }

    /**
     * 更新资产
     */
    public FixedAsset update(Long id, FixedAsset fixedAssetDetails) {
        FixedAsset fixedAsset = fixedAssetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("资产不存在，ID: " + id));

        // 更新字段
        if (fixedAssetDetails.getAssetName() != null) {
            fixedAsset.setAssetName(fixedAssetDetails.getAssetName());
        }
        if (fixedAssetDetails.getAssetCode() != null) {
            fixedAsset.setAssetCode(fixedAssetDetails.getAssetCode());
        }
        if (fixedAssetDetails.getDepartment() != null) {
            fixedAsset.setDepartment(fixedAssetDetails.getDepartment());
        }
        if (fixedAssetDetails.getAssetStatus() != null) {
            fixedAsset.setAssetStatus(fixedAssetDetails.getAssetStatus());
        }
        if (fixedAssetDetails.getUserName() != null) {
            fixedAsset.setUserName(fixedAssetDetails.getUserName());
        }
        if (fixedAssetDetails.getLocation() != null) {
            fixedAsset.setLocation(fixedAssetDetails.getLocation());
        }
        if (fixedAssetDetails.getOriginalValue() != null) {
            fixedAsset.setOriginalValue(fixedAssetDetails.getOriginalValue());
        }
        if (fixedAssetDetails.getNetValue() != null) {
            fixedAsset.setNetValue(fixedAssetDetails.getNetValue());
        }

        return fixedAssetRepository.save(fixedAsset);
    }

    /**
     * 删除资产
     */
    public void deleteById(Long id) {
        fixedAssetRepository.deleteById(id);
    }

    /**
     * 批量删除资产
     */
    public void deleteBatch(List<Long> ids) {
        fixedAssetRepository.deleteByIdIn(ids);
    }

    /**
     * 统计各部门资产数量
     */
    public List<Object[]> countByDepartment() {
        return fixedAssetRepository.countByDepartment();
    }

    /**
     * 统计各状态资产数量
     */
    public List<Object[]> countByAssetStatus() {
        return fixedAssetRepository.countByAssetStatus();
    }

    /**
     * 统计资产总价值
     */
    public Object[] sumAssetValues() {
        return fixedAssetRepository.sumAssetValues();
    }
}