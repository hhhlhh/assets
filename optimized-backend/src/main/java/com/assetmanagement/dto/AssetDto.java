package com.assetmanagement.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

    private Long id;
    private String newAssetCode;
    private String assetCode;
    private String financeCardCode;
    private String assetName;
    private String assetCategory;
    private String classificationName;
    private String classificationCode;
    private BigDecimal originalValue;
    private BigDecimal netValue;
    private BigDecimal accumulatedDepreciation;
    private Integer depreciationMonths;
    private Integer usefulLife;
    private String depreciationStatus;
    private String responsiblePerson;
    private String assetStatus;
    private String department;
    private String userName;
    private String location;
    private String specification;
    private Date startDate;
    private String oldAssetCode;
    private String acquisitionMethod;
    private String procurementForm;
    private String assetPurpose;
    private String fundingSource;
    private String brand;
    private String remarks;
    private Date createdAt;
    private Date updatedAt;
}