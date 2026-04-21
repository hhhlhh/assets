package com.assetmanagement.repository;

import com.assetmanagement.entity.FixedAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, Long> {

    /**
     * 根据资产编号查找
     */
    Optional<FixedAsset> findByAssetCode(String assetCode);

    /**
     * 根据部门查找
     */
    List<FixedAsset> findByDepartment(String department);

    /**
     * 根据资产状态查找
     */
    List<FixedAsset> findByAssetStatus(String assetStatus);

    /**
     * 根据部门分页查找
     */
    Page<FixedAsset> findByDepartment(String department, Pageable pageable);

    /**
     * 根据资产状态分页查找
     */
    Page<FixedAsset> findByAssetStatus(String assetStatus, Pageable pageable);

    /**
     * 根据资产名称模糊查询
     */
    List<FixedAsset> findByAssetNameContaining(String assetName);

    /**
     * 根据资产名称模糊查询（分页）
     */
    Page<FixedAsset> findByAssetNameContaining(String assetName, Pageable pageable);

    /**
     * 组合条件查询
     */
    @Query("SELECT f FROM FixedAsset f WHERE " +
           "(:department IS NULL OR f.department = :department) AND " +
           "(:assetStatus IS NULL OR f.assetStatus = :assetStatus) AND " +
           "(:assetName IS NULL OR f.assetName LIKE %:assetName%)")
    Page<FixedAsset> findByCriteria(
            @Param("department") String department,
            @Param("assetStatus") String assetStatus,
            @Param("assetName") String assetName,
            Pageable pageable);

    /**
     * 统计各部门资产数量
     */
    @Query("SELECT f.department, COUNT(f) FROM FixedAsset f GROUP BY f.department")
    List<Object[]> countByDepartment();

    /**
     * 统计各状态资产数量
     */
    @Query("SELECT f.assetStatus, COUNT(f) FROM FixedAsset f GROUP BY f.assetStatus")
    List<Object[]> countByAssetStatus();

    /**
     * 统计资产总价值
     */
    @Query("SELECT SUM(f.originalValue), SUM(f.netValue) FROM FixedAsset f")
    Object[] sumAssetValues();
}