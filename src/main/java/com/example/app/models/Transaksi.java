package com.example.app.models;

import java.time.LocalDateTime;

public abstract class Transaksi {
    private int id;
    private double nominal;
    private LocalDateTime tanggal;
    private String catatan;
    private KategoriTransaksi kategori;

    public Transaksi(int id, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori){
        this.id = id;
        this.nominal = nominal;
        this.tanggal = tanggal;
        this.catatan = catatan;
        this.kategori = kategori;
    }

    public abstract double hitungDampakSaldo();

    public int getId() {
        return id;
    }

    public double getNominal() {
        return nominal;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public String getCatatan() {
        return catatan;
    }
}