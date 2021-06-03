package com.example.projekpemmob.model;

public class user {
    private String nama, notelp, email, password;

    public user(String nama, String notelp, String email, String password) {
        this.nama = nama;
        this.notelp = notelp;
        this.email = email;
        this.password = password;
    }

    public user() {
    }

    public String getNama() {
        return nama;
    }

    public String getNotelp() {
        return notelp;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
