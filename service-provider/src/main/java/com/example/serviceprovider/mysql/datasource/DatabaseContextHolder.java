package com.example.serviceprovider.mysql.datasource;

/**
 * 数据源Holder
 */
public class DatabaseContextHolder {
    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();

    public static void setDataBaseType(DatabaseType type) {
        contextHolder.set(type);
    }

    public static DatabaseType getDatabaseType() {
        return contextHolder.get();
    }

    public static void reset() {
        contextHolder.set(DatabaseType.durian);
    }
}
