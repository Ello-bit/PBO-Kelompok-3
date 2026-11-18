package com.example.app.models;

public abstract class Pengguna {
    private int id;
    private String username;
    private String password;
    private String role;
    private boolean aktif;

    public Pengguna(int id, String username, String password,
                    String role, boolean aktif) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.aktif = aktif;
    }

    public Pengguna(int id, String username, String password) {
        this(id, username, password, null, false);
    }

    public void registrasi() {
        System.out.println("Proses Registrasi Pengguna");
    }

    public boolean login(String username, boolean passwordValid) {
        return this.username.equals(username) && passwordValid;
    }

    public void logout() {
        System.out.println("Pengguna Berhasil Logout");
    }

    public abstract void getDashboard();

    // Mengambil role pengguna
    public String getRole() {
        return role;
    }

    // Mengecek apakah akun aktif
    public boolean isAktif() {
        return aktif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}