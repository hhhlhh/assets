package com.fixedasset.system.entity;

public enum AssetType {
    DESKTOP("台式机"),
    LAPTOP("笔记本"),
    PRINTER("打印机"),
    MONITOR("显示器"),
    SERVER("服务器"),
    NETWORK_DEVICE("网络设备"),
    FURNITURE("办公家具"),
    OTHER("其他");

    private final String displayName;

    AssetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}