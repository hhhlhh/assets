package com.assetmanagement.repository;

import com.assetmanagement.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * 根据资产编号查找
     */
    Optional<Asset> findByAssetCode(String assetCode);

    /**
     * 根据部门查找
     */
    List<Asset> findByDepartment(String department);

    /**
     * 根据资产状态查找
     */
    List<Asset> findByAssetStatus(String assetStatus);

    /**
     * 根据部门分页查找
     */
    Page<Asset> findByDepartment(String department, Pageable pageable);

    /**
     * 根据资产状态分页查找
     */
    Page<Asset> findByAssetStatus(String assetStatus, Pageable pageable);

    /**
     * 根据资产名称模糊查询
     */
    List<Asset> findByAssetNameContaining(String assetName);

    /**
     * 根据资产名称模糊查询（分页）
     */
    Page<Asset> findByAssetNameContaining(String assetName, Pageable pageable);

    /**
     * 多条件组合查询
     */
    @Query("SELECT a FROM Asset a WHERE " +
           "(:assetName IS NULL OR LOWER(a.assetName) LIKE LOWER(CONCAT('%', :assetName, '%'))) AND " +
           "(:assetCode IS NULL OR a.assetCode LIKE %:assetCode%) AND " +
           "(:department IS NULL OR a.department = :department) AND " +
           "(:assetStatus IS NULL OR a.assetStatus = :assetStatus) AND " +
           "(:assetCategory IS NULL OR a.assetCategory = :assetCategory) AND " +
           "(:userName IS NULL OR a.userName LIKE %:userName%) AND " +
           "(:location IS NULL OR a.location LIKE %:location%)")
    Page<Asset> findBySearchCriteria(
            @Param("assetName") String assetName,
            @Param("assetCode") String assetCode,
            @Param("department") String department,
            @Param("assetStatus") String assetStatus,
            @Param("assetCategory") String assetCategory,
            @Param("userName") String userName,
            @Param("location") String location,
            Pageable pageable);

    /**
     * 统计各部门资产数量
     */
    @Query("SELECT a.department, COUNT(a) FROM Asset a GROUP BY a.department")
    List<Object[]> countByDepartment();

    /**
     * 统计各状态资产数量
     */
    @Query("SELECT a.assetStatus, COUNT(a) FROM Asset a GROUP BY a.assetStatus")
    List<Object[]> countByAssetStatus();

    /**
     * 统计各分类资产数量
     */
    @Query("SELECT a.assetCategory, COUNT(a) FROM Asset a GROUP BY a.assetCategory")
    List<Object[]> countByAssetCategory();

    /**
     * 统计资产总价值
     */
    @Query("SELECT SUM(a.originalValue), SUM(a.netValue) FROM Asset a")
    Object[] sumAssetValues();

    /**
     * 检查资产编号是否存在
     */
    boolean existsByAssetCode(String assetCode);
}