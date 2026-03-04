package com.example.comufavor;

public class Job {
    private String title;
    private String location;
    private String price;
    private String date;

    public Job(String title, String location, String price, String date) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
