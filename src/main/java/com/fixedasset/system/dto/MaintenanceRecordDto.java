package com.fixedasset.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecordDto {
    private Long id;

    private Long assetId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime maintenanceDate;

    private com.fixedasset.system.entity.MaintenanceType type;

    private String description;

    private BigDecimal cost;

    private String technician;
    private String location;
}