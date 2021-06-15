package com.example.projekpemmob.model;

import java.util.ArrayList;
import java.util.Date;

public class History{

    ArrayList<FoodCart>listMakanan;
    String date;
    double longitude, latitude;
    int totalPrice;

    public History() {
    }

    public History(ArrayList<FoodCart> listMakanan, String date, int total, double longitude, double latitude) {
        this.listMakanan    = listMakanan;
        this.date           = date;
        this.totalPrice     = total;
        this.longitude      = longitude;
        this.latitude       = latitude;
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
}
