# 固定资产管理系统 Dockerfile
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制Maven构建文件
COPY pom.xml .
COPY src ./src

# 使用Maven构建项目
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests \
    && rm -rf ~/.m2/repository/* \
    && apt-get purge -y maven \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

# 复制JAR文件
COPY target/fixed-asset-system-*.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1