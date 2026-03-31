package com.fixedasset.system.entity;

public enum UserRole {
    ADMIN("管理员"),
    USER("普通用户");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}