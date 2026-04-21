package com.assetmanagement.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetSearchDto {

    private String assetName;
    private String assetCode;
    private String department;
    private String assetStatus;
    private String assetCategory;
    private String userName;
    private String location;

    // 分页参数
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";
}