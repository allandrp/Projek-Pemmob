package com.example.projekpemmob.model;

import java.util.ArrayList;

public class History{

    ArrayList<FoodCart>listMakanan;
    String date;
    double longitude, latitude;
    int totalPrice;
    String status;

    public History() {
    }

    public History(ArrayList<FoodCart> listMakanan, String date, int total, double longitude, double latitude, String done) {
        this.listMakanan    = listMakanan;
        this.date           = date;
        this.totalPrice     = total;
        this.longitude      = longitude;
        this.latitude       = latitude;
        this.status = done;
    }

    public ArrayList<FoodCart> getListMakanan() {
        return listMakanan;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getDate() {
        return date;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getStatus() {
        return status;
    }
}
