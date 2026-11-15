package com.example.app.models;

public abstract class Transaksi {
    protected int idTransaksi;
    protected int idUsaha;
    protected double nominal;
    protected String tanggal;
    protected String catatan;
    protected KategoriTransaksi kategori;

    public Transaksi() {
    }

    public Transaksi(int idTransaksi, int idUsaha, double nominal, String tanggal, String catatan,
            KategoriTransaksi kategori) {
        setNominal(nominal); // Memvalidasi nominal > 0 sesuai FR-07
        this.idTransaksi = idTransaksi;
        this.idUsaha = idUsaha;
        this.tanggal = tanggal;
        this.catatan = catatan;
        this.kategori = kategori;
    }

    public Transaksi(int idUsaha, double nominal, String tanggal, String catatan, KategoriTransaksi kategori) {
        this(0, idUsaha, nominal, tanggal, catatan, kategori);
    }

    public abstract double hitungDampakSaldo();

    public abstract String getJenisTransaksi();

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
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

    public void setNominal(double nominal) {
        if (nominal <= 0) {
            throw new IllegalArgumentException("Nominal transaksi harus lebih besar dari 0");
        }
        this.nominal = nominal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
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
