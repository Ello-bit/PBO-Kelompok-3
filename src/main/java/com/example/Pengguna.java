package com.example;

/**
 * STUB SEMENTARA.
 * Implementasi lengkap (registrasi, login, logout) dikerjakan oleh Alya
 * di branch "login-register". File ini hanya kerangka agar PemilikUMKM
 * bisa di-compile dan di-test di branch "profil-umkm" sebelum merge.
 *
 * PENTING saat merge: pastikan signature method di bawah sama persis
 * dengan versi final dari Alya, supaya tidak perlu ubah PemilikUMKM lagi.
 */
public abstract class Pengguna {

    private int id;
    private String username;
    private String password;

    public Pengguna(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public boolean registrasi() {
        // TODO: implementasi oleh Alya (branch login-register)
        throw new UnsupportedOperationException("Belum diimplementasikan - lihat branch login-register");
    }

    public boolean login(String inputUsername, String inputPassword) {
        // TODO: implementasi oleh Alya (branch login-register)
        throw new UnsupportedOperationException("Belum diimplementasikan - lihat branch login-register");
    }

    public void logout() {
        // TODO: implementasi oleh Alya (branch login-register)
        throw new UnsupportedOperationException("Belum diimplementasikan - lihat branch login-register");
    }

    public abstract String getDashboard();

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}