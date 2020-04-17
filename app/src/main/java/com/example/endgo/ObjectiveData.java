package com.example.endgo;

public class ObjectiveData {

    // the fields are the same as the information we have in the database for each objective
    // we use public fields as the getValue function of a datasnapshot can initialize an object
    // of this type automatically
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
