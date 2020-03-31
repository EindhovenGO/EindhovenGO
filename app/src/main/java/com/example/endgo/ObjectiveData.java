package com.example.endgo;

public class ObjectiveData {

    public String description;
    public int difficulty;
    public String name;
    public double longitude;
    public double latitude;
    // the parent name in the database
    private String dbName;

    public ObjectiveData() {

    }

    public ObjectiveData(String name, int difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String name) {
        dbName = name;
    }
}
