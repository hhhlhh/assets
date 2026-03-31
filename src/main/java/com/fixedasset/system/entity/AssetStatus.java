package com.fixedasset.system.entity;

public enum AssetStatus {
    IN_USE("在用"),
    IDLE("闲置"),
    MAINTENANCE("维修中"),
    SCRAPPED("报废");

    private final String displayName;

    AssetStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}