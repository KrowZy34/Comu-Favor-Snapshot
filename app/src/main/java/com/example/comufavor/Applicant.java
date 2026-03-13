package com.example.comufavor;

import java.io.Serializable;

public class Applicant implements Serializable {
    private String name;
    private double rating;
    private int imageResId;
    private String description;
    private String price;
    private java.util.List<String> skills;

    public Applicant(String name, double rating, int imageResId, String description, String price, java.util.List<String> skills) {
        this.name = name;
        this.rating = rating;
        this.imageResId = imageResId;
        this.description = description;
        this.price = price;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public java.util.List<String> getSkills() {
        return skills;
    }
}
