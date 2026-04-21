package com.assetmanagement.service;

import com.assetmanagement.entity.Asset;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {

    @Autowired
    private AssetService assetService;

    private static final String[] HEADERS = {
        "新资产编号", "资产编号", "财务卡片编号", "资产名称", "资产大类",
        "资产分类名称", "资产分类代码", "原值", "净值", "累计折旧",
        "已计提月份", "使用年限", "计提状态", "负责人", "资产状态",
        "使用部门", "使用人", "存放地点", "规格型号", "开始使用日期",
        "旧资产编码", "取得方式", "组织采购形式", "资产用途", "资金来源",
        "品牌", "备注"
    };

    /**
     * 导出资产数据到Excel
     */
    public void exportToExcel(List<Asset> assets, HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("固定资产台账");

            // 创建标题行
            createHeaderRow(sheet, workbook);

            // 创建数据行
            int rowNum = 1;
            for (Asset asset : assets) {
                Row row = sheet.createRow(rowNum++);
                populateAssetRow(row, asset);
            }

            // 自动调整列宽
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=assets_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx");

            // 写入响应流
            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
            }
        }
    }

    /**
     * 导出为CSV格式
     */
    public void exportToCsv(List<Asset> assets, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=assets_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv");

        try (OutputStream outputStream = response.getOutputStream()) {
            // 写入BOM，解决Excel中文乱码问题
            outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            // 写入标题行
            StringBuilder headerLine = new StringBuilder();
            for (int i = 0; i < HEADERS.length; i++) {
                if (i > 0) headerLine.append(",");
                headerLine.append("\"").append(HEADERS[i]).append("\"");
            }
            headerLine.append("\n");
            outputStream.write(headerLine.toString().getBytes("UTF-8"));

            // 写入数据行
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (Asset asset : assets) {
                StringBuilder dataLine = new StringBuilder();
                appendCsvCell(dataLine, asset.getNewAssetCode());
                appendCsvCell(dataLine, asset.getAssetCode());
                appendCsvCell(dataLine, asset.getFinanceCardCode());
                appendCsvCell(dataLine, asset.getAssetName());
                appendCsvCell(dataLine, asset.getAssetCategory());
                appendCsvCell(dataLine, asset.getClassificationName());
                appendCsvCell(dataLine, asset.getClassificationCode());
                appendCsvCell(dataLine, asset.getOriginalValue());
                appendCsvCell(dataLine, asset.getNetValue());
                appendCsvCell(dataLine, asset.getAccumulatedDepreciation());
                appendCsvCell(dataLine, asset.getDepreciationMonths());
                appendCsvCell(dataLine, asset.getUsefulLife());
                appendCsvCell(dataLine, asset.getDepreciationStatus());
                appendCsvCell(dataLine, asset.getResponsiblePerson());
                appendCsvCell(dataLine, asset.getAssetStatus());
                appendCsvCell(dataLine, asset.getDepartment());
                appendCsvCell(dataLine, asset.getUserName());
                appendCsvCell(dataLine, asset.getLocation());
                appendCsvCell(dataLine, asset.getSpecification());
                appendCsvCell(dataLine, asset.getStartDate() != null ?
                    dateFormat.format(asset.getStartDate()) : null);
                appendCsvCell(dataLine, asset.getOldAssetCode());
                appendCsvCell(dataLine, asset.getAcquisitionMethod());
                appendCsvCell(dataLine, asset.getProcurementForm());
                appendCsvCell(dataLine, asset.getAssetPurpose());
                appendCsvCell(dataLine, asset.getFundingSource());
                appendCsvCell(dataLine, asset.getBrand());
                appendCsvCell(dataLine, asset.getRemarks());
                dataLine.append("\n");
                outputStream.write(dataLine.toString().getBytes("UTF-8"));
            }
        }
    }

    /**
     * 创建Excel标题行
     */
    private void createHeaderRow(Sheet sheet, Workbook workbook) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * 填充资产数据行
     */
    private void populateAssetRow(Row row, Asset asset) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int cellNum = 0;

        row.createCell(cellNum++).setCellValue(asset.getNewAssetCode() != null ? asset.getNewAssetCode() : "");
        row.createCell(cellNum++).setCellValue(asset.getAssetCode() != null ? asset.getAssetCode() : "");
        row.createCell(cellNum++).setCellValue(asset.getFinanceCardCode() != null ? asset.getFinanceCardCode() : "");
        row.createCell(cellNum++).setCellValue(asset.getAssetName() != null ? asset.getAssetName() : "");
        row.createCell(cellNum++).setCellValue(asset.getAssetCategory() != null ? asset.getAssetCategory() : "");
        row.createCell(cellNum++).setCellValue(asset.getClassificationName() != null ? asset.getClassificationName() : "");
        row.createCell(cellNum++).setCellValue(asset.getClassificationCode() != null ? asset.getClassificationCode() : "");
        row.createCell(cellNum++).setCellValue(asset.getOriginalValue() != null ? asset.getOriginalValue().doubleValue() : 0.0);
        row.createCell(cellNum++).setCellValue(asset.getNetValue() != null ? asset.getNetValue().doubleValue() : 0.0);
        row.createCell(cellNum++).setCellValue(asset.getAccumulatedDepreciation() != null ? asset.getAccumulatedDepreciation().doubleValue() : 0.0);
        row.createCell(cellNum++).setCellValue(asset.getDepreciationMonths() != null ? asset.getDepreciationMonths() : 0);
        row.createCell(cellNum++).setCellValue(asset.getUsefulLife() != null ? asset.getUsefulLife() : 0);
        row.createCell(cellNum++).setCellValue(asset.getDepreciationStatus() != null ? asset.getDepreciationStatus() : "");
        row.createCell(cellNum++).setCellValue(asset.getResponsiblePerson() != null ? asset.getResponsiblePerson() : "");
        row.createCell(cellNum++).setCellValue(asset.getAssetStatus() != null ? asset.getAssetStatus() : "");
        row.createCell(cellNum++).setCellValue(asset.getDepartment() != null ? asset.getDepartment() : "");
        row.createCell(cellNum++).setCellValue(asset.getUserName() != null ? asset.getUserName() : "");
        row.createCell(cellNum++).setCellValue(asset.getLocation() != null ? asset.getLocation() : "");
        row.createCell(cellNum++).setCellValue(asset.getSpecification() != null ? asset.getSpecification() : "");
        row.createCell(cellNum++).setCellValue(asset.getStartDate() != null ? dateFormat.format(asset.getStartDate()) : "");
        row.createCell(cellNum++).setCellValue(asset.getOldAssetCode() != null ? asset.getOldAssetCode() : "");
        row.createCell(cellNum++).setCellValue(asset.getAcquisitionMethod() != null ? asset.getAcquisitionMethod() : "");
        row.createCell(cellNum++).setCellValue(asset.getProcurementForm() != null ? asset.getProcurementForm() : "");
        row.createCell(cellNum++).setCellValue(asset.getAssetPurpose() != null ? asset.getAssetPurpose() : "");
        row.createCell(cellNum++).setCellValue(asset.getFundingSource() != null ? asset.getFundingSource() : "");
        row.createCell(cellNum++).setCellValue(asset.getBrand() != null ? asset.getBrand() : "");
        row.createCell(cellNum++).setCellValue(asset.getRemarks() != null ? asset.getRemarks() : "");
    }

    /**
     * 添加CSV单元格
     */
    private void appendCsvCell(StringBuilder sb, Object value) {
        if (sb.length() > 0) {
            sb.append(",");
        }
        sb.append("\"").append(value != null ? value.toString().replace("\"", "\"\"") : "").append("\"");
    }
}