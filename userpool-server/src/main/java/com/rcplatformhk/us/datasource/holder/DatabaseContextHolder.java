package com.rcplatformhk.us.datasource.holder;


import com.rcplatformhk.us.common.DatabaseType;

public class DatabaseContextHolder {
    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();

    public static void setDatabaseType(DatabaseType type){
        contextHolder.set(type);
    }
    public static DatabaseType getDatabaseType(){
        return contextHolder.get();
    }
}
