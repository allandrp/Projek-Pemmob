package com.example.projekpemmob.model;

public class User {
    private String nama, notelp, email, password;

    public User(String nama, String notelp, String email, String password) {
        this.nama = nama;
        this.notelp = notelp;
        this.email = email;
        this.password = password;
    }

    public User() {
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
