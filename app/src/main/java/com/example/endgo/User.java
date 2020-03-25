package com.example.endgo;

public class User {
    public String name, email, imgUrl;
    public int points;

    public User() {

    }

    public User (String name, String email, String imgUrl, int points) {
        this.name = name;
        this.email = email;
        this.imgUrl = imgUrl;
        this.points = points;
    }
}
