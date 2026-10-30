package com.example.app.models;

public class ProfilUMKM {
    private int idUsaha;
    private String namaUsaha;
    private String jenisUsaha;
    private String alamat;

    public ProfilUMKM(int idUsaha, String namaUsaha, String jenisUsaha, String alamat){
        this.idUsaha = idUsaha;
        this.namaUsaha = namaUsaha;
        this.jenisUsaha = jenisUsaha;
        this.alamat = alamat;
    }

    public void tambahTransaksi(){

    }

    public int getIdUsaha(){
        return idUsaha;
    }

    public void setIdUsaha(int idUsaha){
        this.idUsaha = idUsaha;
    }

    public String getNamaUsaha(){
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha){
        this.namaUsaha = namaUsaha;
    }

    public String getJenisUsaha(){
        return jenisUsaha;
    }

    public void setJenisUsaha(String jenisUsaha){
        this.jenisUsaha = jenisUsaha;
    }

    public String getAlamat(){
        return alamat;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
}