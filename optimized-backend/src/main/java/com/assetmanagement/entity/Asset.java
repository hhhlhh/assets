package com.assetmanagement.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "fixed_assets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "new_asset_code", length = 50)
    private String newAssetCode;

    @Column(name = "asset_code", length = 50, unique = true, nullable = false)
    @NotBlank(message = "资产编号不能为空")
    private String assetCode;

    @Column(name = "finance_card_code", length = 50)
    private String financeCardCode;

    @Column(name = "asset_name", length = 200, nullable = false)
    @NotBlank(message = "资产名称不能为空")
    private String assetName;

    @Column(name = "asset_category", length = 100)
    private String assetCategory;

    @Column(name = "classification_name", length = 100)
    private String classificationName;

    @Column(name = "classification_code", length = 50)
    private String classificationCode;

    @Column(name = "original_value", precision = 12, scale = 2)
    private BigDecimal originalValue;

    @Column(name = "net_value", precision = 12, scale = 2)
    private BigDecimal netValue;

    @Column(name = "accumulated_depreciation", precision = 12, scale = 2)
    private BigDecimal accumulatedDepreciation;

    @Column(name = "depreciation_months")
    private Integer depreciationMonths;

    @Column(name = "useful_life")
    private Integer usefulLife;

    @Column(name = "depreciation_status", length = 20)
    private String depreciationStatus;

    @Column(name = "responsible_person", length = 50)
    private String responsiblePerson;

    @Column(name = "asset_status", length = 20)
    private String assetStatus;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "user_name", length = 50)
    private String userName;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "specification", length = 200)
    private String specification;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "old_asset_code", length = 50)
    private String oldAssetCode;

    @Column(name = "acquisition_method", length = 50)
    private String acquisitionMethod;

    @Column(name = "procurement_form", length = 50)
    private String procurementForm;

    @Column(name = "asset_purpose", length = 100)
    private String assetPurpose;

    @Column(name = "funding_source", length = 100)
    private String fundingSource;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        if (assetStatus == null) {
            assetStatus = "在用";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}