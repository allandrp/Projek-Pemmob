package com.example.projekpemmob.model;

import java.util.ArrayList;
import java.util.Date;

public class History{

    ArrayList<FoodCart>listMakanan;
    String date;
    int totalPrice;

    public History() {
    }

    public History(ArrayList<FoodCart> listMakanan, String date, int total) {
        this.listMakanan    = listMakanan;
        this.date           = date;
        this.totalPrice     = total;
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
}
