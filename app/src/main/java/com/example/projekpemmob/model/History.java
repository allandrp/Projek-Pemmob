package com.example.projekpemmob.model;

import java.util.ArrayList;
import java.util.Date;

public class History{

    ArrayList<Object>listMakanan;
    Date date;

    public History() {
    }

    public History(ArrayList<Object> listMakanan, Date date) {
        this.listMakanan = listMakanan;
        this.date = date;
    }

    public ArrayList<Object> getListMakanan() {
        return listMakanan;
    }

    public Date getDate() {
        return date;
    }
}
