-- 固定资产管理系统 - 优化版数据库
-- 基于原有Excel模板，优化表结构设计

CREATE DATABASE IF NOT EXISTS asset_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE asset_management;

-- 固定资产表（基于您的Excel模板）
CREATE TABLE fixed_assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    new_asset_code VARCHAR(50) COMMENT '新资产编号',
    asset_code VARCHAR(50) UNIQUE NOT NULL COMMENT '资产编号',
    finance_card_code VARCHAR(50) COMMENT '财务卡片编号',
    asset_name VARCHAR(200) NOT NULL COMMENT '资产名称',
    asset_category VARCHAR(100) COMMENT '资产大类',
    classification_name VARCHAR(100) COMMENT '资产分类名称',
    classification_code VARCHAR(50) COMMENT '资产分类代码',
    original_value DECIMAL(12,2) COMMENT '原值',
    net_value DECIMAL(12,2) COMMENT '净值',
    accumulated_depreciation DECIMAL(12,2) COMMENT '累计折旧',
    depreciation_months INT COMMENT '已计提月份',
    useful_life INT COMMENT '使用年限',
    depreciation_status VARCHAR(20) COMMENT '计提状态',
    responsible_person VARCHAR(50) COMMENT '负责人',
    asset_status VARCHAR(20) DEFAULT '在用' COMMENT '资产状态',
    department VARCHAR(100) COMMENT '使用部门',
    user_name VARCHAR(50) COMMENT '使用人',
    location VARCHAR(200) COMMENT '存放地点',
    specification VARCHAR(200) COMMENT '规格型号',
    start_date DATE COMMENT '开始使用日期',
    old_asset_code VARCHAR(50) COMMENT '旧资产编码',
    acquisition_method VARCHAR(50) COMMENT '取得方式',
    procurement_form VARCHAR(50) COMMENT '组织采购形式',
    asset_purpose VARCHAR(100) COMMENT '资产用途',
    funding_source VARCHAR(100) COMMENT '资金来源',
    brand VARCHAR(100) COMMENT '品牌',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='固定资产表';

-- 创建索引
CREATE INDEX idx_asset_code ON fixed_assets(asset_code);
CREATE INDEX idx_department ON fixed_assets(department);
CREATE INDEX idx_asset_status ON fixed_assets(asset_status);
CREATE INDEX idx_asset_name ON fixed_assets(asset_name);
CREATE INDEX idx_created_at ON fixed_assets(created_at);

-- 插入示例数据
INSERT INTO fixed_assets (
    asset_code, asset_name, asset_category, department,
    user_name, location, original_value, net_value, asset_status,
    specification, start_date, responsible_person, brand
) VALUES
('FA001', '联想台式电脑', '电子设备', '技术部', '张三', '办公室A-101',
 5000.00, 4500.00, '在用', 'ThinkCentre M720', '2023-01-15', '张三', '联想'),
('FA002', '佳能打印机', '办公设备', '行政部', '李四', '办公室B-201',
 2000.00, 1800.00, '在用', 'Canon LBP2900', '2023-02-20', '李四', '佳能'),
('FA003', '办公桌椅', '办公家具', '财务部', '王五', '办公室C-301',
 1500.00, 1200.00, '在用', '实木办公桌', '2022-12-10', '王五', '震旦'),
('FA004', '戴尔显示器', '电子设备', '技术部', '赵六', '办公室A-102',
 1200.00, 1100.00, '在用', 'Dell U2419H', '2023-03-01', '赵六', '戴尔'),
('FA005', '会议桌', '办公家具', '行政部', NULL, '会议室1',
 3000.00, 2700.00, '在用', '大型会议桌', '2022-11-15', '行政部门', '震旦');

-- 验证数据
SELECT COUNT(*) as '资产总数' FROM fixed_assets;
SELECT * FROM fixed_assets LIMIT 3;