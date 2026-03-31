-- 固定资产管理系统数据库初始化脚本

-- 固定资产管理系统数据库初始化脚本

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') DEFAULT 'USER',
    department VARCHAR(50),
    phone VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建资产表
CREATE TABLE IF NOT EXISTS assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type ENUM('DESKTOP', 'LAPTOP', 'PRINTER', 'MONITOR', 'SERVER', 'NETWORK_DEVICE', 'FURNITURE', 'OTHER') NOT NULL,
    model VARCHAR(100),
    serial_number VARCHAR(100) UNIQUE,
    purchase_date DATE,
    price DECIMAL(10,2),
    status ENUM('IN_USE', 'IDLE', 'MAINTENANCE', 'SCRAPPED') DEFAULT 'IN_USE',
    location VARCHAR(100),
    description TEXT,
    assigned_to VARCHAR(50),
    department VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建维护记录表
CREATE TABLE IF NOT EXISTS maintenance_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    asset_id BIGINT NOT NULL,
    maintenance_date DATETIME NOT NULL,
    type ENUM('REPAIR', 'MAINTENANCE', 'UPGRADE') NOT NULL,
    description TEXT,
    cost DECIMAL(10,2),
    technician VARCHAR(50),
    location VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE
);

-- 创建索引优化查询性能
CREATE INDEX IF NOT EXISTS idx_assets_type ON assets(type);
CREATE INDEX IF NOT EXISTS idx_assets_status ON assets(status);
CREATE INDEX IF NOT EXISTS idx_assets_department ON assets(department);
CREATE INDEX IF NOT EXISTS idx_assets_serial_number ON assets(serial_number);
CREATE INDEX IF NOT EXISTS idx_maintenance_asset ON maintenance_records(asset_id);
CREATE INDEX IF NOT EXISTS idx_maintenance_date ON maintenance_records(maintenance_date);

-- 检查并插入默认管理员用户
INSERT IGNORE INTO users (username, email, password, role, department, enabled)
VALUES ('admin', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', 'IT部门', TRUE);

-- 插入示例资产数据
INSERT IGNORE INTO assets (name, type, model, serial_number, purchase_date, price, status, location, department) VALUES
('Dell OptiPlex 7090', 'DESKTOP', 'OptiPlex 7090', 'SN001', '2023-01-15', 5800.00, 'IN_USE', '北京办公室A座', 'IT部门'),
('MacBook Pro 16"', 'LAPTOP', 'MacBook Pro M1 Max', 'SN002', '2023-02-20', 21999.00, 'IN_USE', '上海办公室B座', '设计部'),
('HP LaserJet Pro', 'PRINTER', 'LaserJet Pro M404n', 'SN003', '2023-03-10', 1299.00, 'IN_USE', '深圳办公室C座', '行政部'),
('Samsung Monitor 27"', 'MONITOR', 'U2720Q', 'SN004', '2023-04-05', 2199.00, 'IDLE', '仓库', '仓储部');

-- 插入示例维护记录
INSERT IGNORE INTO maintenance_records (asset_id, maintenance_date, type, description, cost, technician, location) VALUES
(1, '2023-12-01 10:30:00', 'MAINTENANCE', '常规保养，清理内部灰尘', 200.00, '张师傅', '北京办公室A座'),
(2, '2023-11-15 14:20:00', 'REPAIR', '屏幕出现花屏问题，已修复', 500.00, '李工程师', '上海办公室B座');

-- 显示数据库状态信息
SELECT '固定资产管理系统数据库初始化完成' AS message;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_assets FROM assets;
SELECT COUNT(*) AS total_maintenance_records FROM maintenance_records;

-- 显示创建的表和示例数据
SELECT '数据库初始化完成' AS message;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_assets FROM assets;
SELECT COUNT(*) AS total_maintenance_records FROM maintenance_records;