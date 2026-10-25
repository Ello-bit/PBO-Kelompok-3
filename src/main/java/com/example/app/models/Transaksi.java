package com.example.app.models;

import java.time.LocalDateTime;

abstract class Transaksi {
    private int id;
    private double nominal;
    private LocalDateTime tanggal;
    private String catatan;

    public Transaksi(int id, double nominal, LocalDateTime tanggal, String catatan){
        this.id = id;
        this.nominal = nominal;
        this.tanggal = tanggal;
        this.catatan = catatan;
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