package com.example.app.models;

public abstract class Pengguna {
    private int id;
    private String username;
    private String password;

    public Pengguna (int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

     
    public void registrasi(){
        System.out.println("Proses Registrasi Pengguna");
    }

    public boolean login(String username, boolean passwordValid){
        return this.username.equals(username) && passwordValid;
    }

    public void logout(){
        System.out.println("Pengguna Berhasil Logout");
    }

    public abstract void getDashboard();

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}