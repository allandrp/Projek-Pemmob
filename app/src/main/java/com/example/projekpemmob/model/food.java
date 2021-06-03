package com.example.projekpemmob.model;

public class food {
    String nama, deskripsi;
    int harga;

    public food(String nama, String deskripsi, int harga) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
    }

    public food() {
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
