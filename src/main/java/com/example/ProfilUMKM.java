package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * ProfilUMKM menyimpan informasi usaha milik PemilikUMKM.
 * Menjadi penghubung antara identitas usaha, transaksi, skor,
 * dan laporan pengajuan KUR (relasi 1:N - akan diisi tim transaksi).
 */
public class ProfilUMKM {

    private int idUsaha;
    private String namaUsaha;
    private String jenisUsaha;
    private String alamat;

    // Placeholder sederhana; akan diganti/disinkronkan dengan branch "transaksi"
    private final List<Object> daftarTransaksi = new ArrayList<>();

    public ProfilUMKM(int idUsaha, String namaUsaha, String jenisUsaha, String alamat) {
        this.idUsaha = idUsaha;
        setNamaUsaha(namaUsaha);
        setJenisUsaha(jenisUsaha);
        setAlamat(alamat);
    }

    public void tambahTransaksi(Object transaksi) {
        // TODO: ganti Object dengan kelas Transaksi setelah branch "transaksi" di-merge
        daftarTransaksi.add(transaksi);
    }

    public List<Object> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    // ===== Getter & Setter dengan validasi sederhana (encapsulation) =====

    public int getIdUsaha() {
        return idUsaha;
    }

    public void setIdUsaha(int idUsaha) {
        this.idUsaha = idUsaha;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        if (namaUsaha == null || namaUsaha.isBlank()) {
            throw new IllegalArgumentException("Nama usaha tidak boleh kosong.");
        }
        this.namaUsaha = namaUsaha;
    }

    public String getJenisUsaha() {
        return jenisUsaha;
    }

    public void setJenisUsaha(String jenisUsaha) {
        this.jenisUsaha = jenisUsaha;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String toString() {
        return "ProfilUMKM{idUsaha=" + idUsaha + ", namaUsaha='" + namaUsaha + "', jenisUsaha='"
                + jenisUsaha + "', alamat='" + alamat + "'}";
    }
}