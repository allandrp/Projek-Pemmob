package com.example.projekpemmob.model;

public class Food {
    String nama, deskripsi;
    int harga;

    public Food(String nama, String deskripsi, int harga) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
    }

    public Food() {
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getHarga() {
        return harga;
    }
}
