package com.fixedasset.system.repository;

import com.fixedasset.system.entity.Asset;
import com.fixedasset.system.entity.AssetStatus;
import com.fixedasset.system.entity.AssetType;
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
    Optional<Asset> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);

    List<Asset> findByType(AssetType type);
    List<Asset> findByStatus(AssetStatus status);
    List<Asset> findByAssignedTo(String assignedTo);
    List<Asset> findByDepartment(String department);

    @Query("SELECT a FROM Asset a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.model) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Asset> searchAssets(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT a FROM Asset a WHERE a.type = :type AND a.status = :status")
    List<Asset> findByTypeAndStatus(@Param("type") AssetType type, @Param("status") AssetStatus status);

    @Query("SELECT COUNT(a) FROM Asset a")
    long countAllAssets();

    @Query("SELECT COUNT(a) FROM Asset a WHERE a.type = :type")
    long countByType(@Param("type") AssetType type);

    @Query("SELECT COUNT(a) FROM Asset a WHERE a.status = :status")
    long countByStatus(@Param("status") AssetStatus status);
}