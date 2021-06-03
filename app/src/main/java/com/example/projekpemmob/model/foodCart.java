package com.example.projekpemmob.model;

public class foodCart {

    int jumlahPesan, harga;
    String nama;

    public foodCart() {
    }

    public foodCart(int jumlahPesan, int harga, String nama) {
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
