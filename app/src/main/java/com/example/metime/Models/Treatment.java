package com.example.metime.Models;

public class Treatment {
    private String id;
    private String name;
    private int duration;
    private int price;
    private boolean active;

    public Treatment() {
    }
    public Treatment(String name, int duration, int price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
        this.active=true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
