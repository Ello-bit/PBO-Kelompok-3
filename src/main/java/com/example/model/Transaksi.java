package com.example.model;

import java.time.LocalDateTime;

public abstract class Transaksi {
    protected int id;
    protected int idUsaha;
    protected double nominal;
    protected LocalDateTime tanggal;
    protected String catatan;
    protected KategoriTransaksi kategori;

    public Transaksi() {
    }

    public Transaksi(int id, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori) {
        setNominal(nominal); // Validasi nominal > 0 sesuai FR-07
        this.id = id;
        this.tanggal = tanggal;
        this.catatan = catatan;
        this.kategori = kategori;
    }

    public Transaksi(int id, int idUsaha, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori) {
        this(id, nominal, tanggal, catatan, kategori);
        this.idUsaha = idUsaha;
    }

    /**
     * Menghitung dampak transaksi terhadap saldo kas usaha (+ / -).
     * @return nilai perubahan saldo
     */
    public abstract double hitungDampakSaldo();

    /**
     * Mendapatkan jenis transaksi ('PEMASUKAN' / 'PENGELUARAN').
     * @return jenis transaksi
     */
    public abstract String getJenisTransaksi();

    // Getters and Setters (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTransaksi() {
        return id;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.id = idTransaksi;
    }

    public int getIdUsaha() {
        return idUsaha;
    }

    public void setIdUsaha(int idUsaha) {
        this.idUsaha = idUsaha;
    }

    public double getNominal() {
        return nominal;
    }

    /**
     * Mengatur nilai nominal transaksi dengan validasi sesuai FR-07 PRD:
     * Menolak nominal kurang dari atau sama dengan nol.
     */
    public void setNominal(double nominal) {
        if (nominal <= 0) {
            throw new IllegalArgumentException("Nominal transaksi harus lebih besar dari 0");
        }
        this.nominal = nominal;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public KategoriTransaksi getKategori() {
        return kategori;
    }

    public void setKategori(KategoriTransaksi kategori) {
        this.kategori = kategori;
    }
}
