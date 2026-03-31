#!/bin/bash

# 固定资产管理系统启动脚本

echo "=========================================="
echo "固定资产管理系统 - 启动脚本"
echo "=========================================="

# 检查Java版本
echo "检查Java版本..."
java_version=$(java -version 2>&1 | head -1 | cut -d'"' -f2)
echo "当前Java版本: $java_version"

# 检查Maven版本
echo "检查Maven版本..."
mvn_version=$(mvn --version 2>&1 | head -1 | cut -d' ' -f3)
echo "当前Maven版本: $mvn_version"

# 检查数据库连接
echo "检查数据库连接..."
if mysql -h 192.168.12.209 -u root -prootroot -e "USE fixed_asset_db;" 2>/dev/null; then
    echo "✓ 数据库连接正常"
else
    echo "⚠ 数据库连接失败，请检查："
    echo "1. MySQL服务器是否运行在 192.168.12.209:3306"
    echo "2. 用户名和密码是否为 root/rootroot"
    echo "3. 数据库 fixed_asset_db 是否存在"
    echo ""
    echo "可以手动创建数据库:"
    echo "mysql -h 192.168.12.209 -u root -prootroot -e \"CREATE DATABASE IF NOT EXISTS fixed_asset_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\""
fi

# 编译项目
echo "编译项目..."
if mvn clean compile -DskipTests; then
    echo "✓ 编译成功"
else
    echo "✗ 编译失败"
    exit 1
fi

# 运行应用
echo "启动应用..."
echo "应用将在 http://localhost:8080 上运行"
echo "默认管理员账户: admin / admin123"
echo ""
echo "按 Ctrl+C 停止服务"

# 后台启动应用
mvn spring-boot:run &

# 等待应用启动
sleep 5

# 检查应用状态
if curl -s http://localhost:8080 > /dev/null; then
    echo "✓ 应用启动成功"
    echo "访问地址: http://localhost:8080"
else
    echo "✗ 应用启动失败，请检查日志"
fi

# 保持脚本运行
wait