package com.example.projekpemmob.model;

import java.util.ArrayList;

public class Cart {

    int jumlahPesanan, totalHarga;
    ArrayList<Object>listMakanan;

    public Cart() {
    }

    public Cart(int jumlahPesanan, int totalHarga, ArrayList<Object> listMakanan) {
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
