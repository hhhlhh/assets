package com.fixedasset.system.repository;

import com.fixedasset.system.entity.MaintenanceRecord;
import com.fixedasset.system.entity.MaintenanceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByAssetId(Long assetId);
    Page<MaintenanceRecord> findByAssetId(Long assetId, Pageable pageable);

    List<MaintenanceRecord> findByType(MaintenanceType type);
    List<MaintenanceRecord> findByTechnician(String technician);

    @Query("SELECT m FROM MaintenanceRecord m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate")
    List<MaintenanceRecord> findByMaintenanceDateBetween(@Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM MaintenanceRecord m WHERE m.asset.id = :assetId AND m.maintenanceDate BETWEEN :startDate AND :endDate")
    List<MaintenanceRecord> findByAssetAndDateRange(@Param("assetId") Long assetId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(m.cost) FROM MaintenanceRecord m WHERE m.asset.id = :assetId")
    Double getTotalMaintenanceCostByAsset(@Param("assetId") Long assetId);
}