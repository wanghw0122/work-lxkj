package com.rcplatformhk.us.common;

public enum DatabaseType {

    major("major", 1),
    secondary("secondary", 2);

    String name;
    Integer id;

    DatabaseType(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public static DatabaseType getTypeById(int id){
        for (DatabaseType databaseType: DatabaseType.values()){
            if (databaseType.getId() == id)
                return databaseType;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
