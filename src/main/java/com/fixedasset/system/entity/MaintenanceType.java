package com.fixedasset.system.entity;

public enum MaintenanceType {
    REPAIR("维修"),
    MAINTENANCE("保养"),
    UPGRADE("升级");

    private final String displayName;

    MaintenanceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}