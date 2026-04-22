package com.assetmanagement.service;

import com.assetmanagement.entity.Asset;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ExcelImportService {

    @Autowired
    private AssetService assetService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 从Excel文件导入资产数据
     */
    public ImportResult importFromExcel(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }

        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            throw new IllegalArgumentException("只支持Excel文件格式 (.xlsx, .xls)");
        }

        List<Asset> assets = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel文件没有工作表");
            }

            // 跳过标题行，从第二行开始读取数据
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                try {
                    Asset asset = parseRowToAsset(row);
                    assets.add(asset);
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    errors.add(String.format("第%d行: %s", rowIndex + 1, e.getMessage()));
                }
            }

            // 批量保存资产
            if (!assets.isEmpty()) {
                for (Asset asset : assets) {
                    try {
                        assetService.save(asset);
                    } catch (Exception e) {
                        errorCount++;
                        successCount--;
                        errors.add(String.format("资产编号%s保存失败: %s",
                            asset.getAssetCode(), e.getMessage()));
                    }
                }
            }

        } catch (Exception e) {
            throw new IOException("读取Excel文件失败: " + e.getMessage());
        }

        return new ImportResult(successCount, errorCount, errors);
    }

    /**
     * 解析Excel行数据为Asset对象
     */
    private Asset parseRowToAsset(Row row) throws ParseException {
        Asset asset = new Asset();

        try {
            // 资产编号 (必填)
            Cell assetCodeCell = row.getCell(0);
            if (assetCodeCell == null || getCellValueAsString(assetCodeCell).trim().isEmpty()) {
                throw new IllegalArgumentException("资产编号不能为空");
            }
            asset.setAssetCode(getCellValueAsString(assetCodeCell).trim());

            // 资产名称 (必填)
            Cell assetNameCell = row.getCell(1);
            if (assetNameCell == null || getCellValueAsString(assetNameCell).trim().isEmpty()) {
                throw new IllegalArgumentException("资产名称不能为空");
            }
            asset.setAssetName(getCellValueAsString(assetNameCell).trim());

            // 资产类别
            Cell categoryCell = row.getCell(2);
            if (categoryCell != null) {
                asset.setAssetCategory(getCellValueAsString(categoryCell).trim());
            }

            // 使用部门
            Cell departmentCell = row.getCell(3);
            if (departmentCell != null) {
                asset.setDepartment(getCellValueAsString(departmentCell).trim());
            }

            // 使用人
            Cell userNameCell = row.getCell(4);
            if (userNameCell != null) {
                asset.setUserName(getCellValueAsString(userNameCell).trim());
            }

            // 存放地点
            Cell locationCell = row.getCell(5);
            if (locationCell != null) {
                asset.setLocation(getCellValueAsString(locationCell).trim());
            }

            // 资产状态
            Cell statusCell = row.getCell(6);
            if (statusCell != null) {
                String status = getCellValueAsString(statusCell).trim();
                if (!status.isEmpty()) {
                    asset.setAssetStatus(status);
                }
            }

            // 原值
            Cell originalValueCell = row.getCell(7);
            if (originalValueCell != null) {
                String valueStr = getCellValueAsString(originalValueCell).trim();
                if (!valueStr.isEmpty()) {
                    try {
                        asset.setOriginalValue(new BigDecimal(valueStr));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("原值格式错误");
                    }
                }
            }

            // 净值
            Cell netValueCell = row.getCell(8);
            if (netValueCell != null) {
                String valueStr = getCellValueAsString(netValueCell).trim();
                if (!valueStr.isEmpty()) {
                    try {
                        asset.setNetValue(new BigDecimal(valueStr));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("净值格式错误");
                    }
                }
            }

            // 品牌
            Cell brandCell = row.getCell(9);
            if (brandCell != null) {
                asset.setBrand(getCellValueAsString(brandCell).trim());
            }

            // 规格型号
            Cell specCell = row.getCell(10);
            if (specCell != null) {
                asset.setSpecification(getCellValueAsString(specCell).trim());
            }

            // 开始使用日期
            Cell startDateCell = row.getCell(11);
            if (startDateCell != null) {
                String dateStr = getCellValueAsString(startDateCell).trim();
                if (!dateStr.isEmpty()) {
                    try {
                        asset.setStartDate(DATE_FORMAT.parse(dateStr));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("开始使用日期格式错误，应为 yyyy-MM-dd");
                    }
                }
            }

            // 负责人
            Cell responsibleCell = row.getCell(12);
            if (responsibleCell != null) {
                asset.setResponsiblePerson(getCellValueAsString(responsibleCell).trim());
            }

            // 备注
            Cell remarksCell = row.getCell(13);
            if (remarksCell != null) {
                asset.setRemarks(getCellValueAsString(remarksCell).trim());
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("数据解析失败: " + e.getMessage());
        }

        return asset;
    }

    /**
     * 获取单元格字符串值
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DATE_FORMAT.format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * 检查是否为空行
     */
    private boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell).trim();
                if (!value.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 导入结果类
     */
    public static class ImportResult {
        private int successCount;
        private int errorCount;
        private List<String> errors;

        public ImportResult(int successCount, int errorCount, List<String> errors) {
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.errors = errors;
        }

        // Getters
        public int getSuccessCount() { return successCount; }
        public int getErrorCount() { return errorCount; }
        public List<String> getErrors() { return errors; }
        public int getTotalCount() { return successCount + errorCount; }
    }
}