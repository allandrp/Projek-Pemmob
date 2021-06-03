package com.example.projekpemmob.model;

import java.util.ArrayList;

public class cart {

    int jumlahPesanan, totalHarga;
    ArrayList<Object>listMakanan;

    public cart() {
    }

    public cart(int jumlahPesanan, int totalHarga, ArrayList<Object> listMakanan) {
        this.jumlahPesanan = jumlahPesanan;
        this.totalHarga = totalHarga;
        this.listMakanan = listMakanan;
    }

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public ArrayList<Object> getListMakanan() {
        return listMakanan;
    }
}
