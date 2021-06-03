package com.example.projekpemmob.model;

public class FoodCart {

    int jumlahPesan, harga;
    String nama;

    public FoodCart() {
    }

    public FoodCart(int jumlahPesan, int harga, String nama) {
        this.jumlahPesan = jumlahPesan;
        this.harga = harga;
        this.nama = nama;
    }

    public int getJumlahPesan() {
        return jumlahPesan;
    }

    public int getHarga() {
        return harga;
    }

    public String getNama() {
        return nama;
    }
}
