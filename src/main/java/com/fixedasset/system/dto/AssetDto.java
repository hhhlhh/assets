package com.fixedasset.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {
    private Long id;

    @NotBlank(message = "资产名称不能为空")
    private String name;

    @NotNull(message = "资产类型不能为空")
    private com.fixedasset.system.entity.AssetType type;

    private String model;

    private String serialNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    private BigDecimal price;

    private com.fixedasset.system.entity.AssetStatus status;

    private String location;
    private String description;
    private String assignedTo;
    private String department;
}