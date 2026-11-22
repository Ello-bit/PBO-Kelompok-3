package com.example.model;

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
        System.out.println("Registrasi Pengguna berhasil !!!");
    }

    public boolean login(String username, boolean passwordValid) {
        return this.username.equals(username) && passwordValid;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username)
                && this.password.equals(password);
    }

    public void logout() {
        System.out.println("Pengguna berhasil logout !!!");
    }

    public abstract void getDashboard();

    public String getRole() {
        return role;
    }

    public String getStatusTeks() {
        if (aktif) {
            return "Aktif";
        } else {
            return "Nonaktif";
        }
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

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
