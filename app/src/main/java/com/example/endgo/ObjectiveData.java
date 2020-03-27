package com.example.endgo;

public class ObjectiveData {

    public String description;
    public int difficulty;
    public String name;
    public double longitude;
    public double latitude;

    public ObjectiveData() {

    }

    public ObjectiveData(String name, int difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }
}
