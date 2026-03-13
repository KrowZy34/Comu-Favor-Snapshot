package com.example.comufavor;

import java.io.Serializable;

public class Job implements Serializable {
    private String title;
    private String location;
    private String price;
    private String date;
    private String status; // "confirmado", "en_proceso", "propuesta_enviada", "finalizado", or null

    public Job(String title, String location, String price, String date) {
        this(title, location, price, date, null);
    }

    public Job(String title, String location, String price, String date, String status) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.date = date;
        this.status = status;
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

    public String getStatus() {
        return status;
    }
}
