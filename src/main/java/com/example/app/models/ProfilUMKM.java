package com.example.app.models;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * ProfilUMKM menyimpan informasi usaha milik PemilikUMKM, serta
 * menyediakan perhitungan ringkasan keuangan (saldo, laba bersih,
 * total per kategori/jenis) berdasarkan daftar Transaksi yang dimilikinya.
 * Sesuai PRD Bagian 3.2, 3.3, dan 3.4 (Dashboard Keuangan).
 */
public class ProfilUMKM {

    private int idUsaha;
    private String namaUsaha;
    private String jenisUsaha;
    private String alamat;

    private final List<Transaksi> daftarTransaksi = new ArrayList<>();

    /**
     * Periode yang sedang dipilih untuk memfilter perhitungan dashboard,
     * format "MMMM yyyy" (contoh: "Oktober 2026"). Null/kosong = semua periode.
     * Sesuai flowchart 4.1.3: "Pilih Periode(harian/bulanan)".
     */
    private String periode;

    private static final DateTimeFormatter FORMAT_PERIODE =
            DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("id", "ID"));

    public ProfilUMKM(int idUsaha, String namaUsaha, String jenisUsaha, String alamat) {
        this.idUsaha = idUsaha;
        setNamaUsaha(namaUsaha);
        setJenisUsaha(jenisUsaha);
        setAlamat(alamat);
    }

    // ===== Pengelolaan transaksi =====

    public void tambahTransaksi(Transaksi transaksi) {
        daftarTransaksi.add(transaksi);
    }

    public List<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    // ===== Periode untuk filter dashboard =====

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    /** Daftar periode (bulan-tahun) yang muncul dari transaksi yang sudah tercatat, untuk dropdown filter. */
    public List<String> getDaftarPeriode() {
        return daftarTransaksi.stream()
                .map(t -> t.getTanggal().format(FORMAT_PERIODE))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private boolean sesuaiPeriode(Transaksi t) {
        if (periode == null || periode.isBlank()) {
            return true; // tidak difilter -> hitung semua periode
        }
        return periode.equalsIgnoreCase(t.getTanggal().format(FORMAT_PERIODE));
    }

    // ===== Perhitungan keuangan (PRD Bagian 3.3 & 3.4) =====

    /** Total seluruh transaksi bertipe Pemasukan pada periode terpilih. */
    public double hitungTotalPemasukan() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t instanceof Pemasukan)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    /** Total seluruh transaksi bertipe Pengeluaran pada periode terpilih. */
    public double hitungTotalPengeluaran() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t instanceof Pengeluaran)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    /** Total transaksi berkategori PENJUALAN (pendapatan usaha). */
    public double hitungTotalPenjualan() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t.getKategori() == KategoriTransaksi.PENJUALAN)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    /** Total transaksi berkategori OPERASIONAL (beban usaha). */
    public double hitungTotalOperasional() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t.getKategori() == KategoriTransaksi.OPERASIONAL)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    /**
     * Saldo usaha = akumulasi dampak saldo seluruh transaksi pada periode terpilih.
     * Modal & Penjualan menambah saldo, Operasional mengurangi saldo (lihat Transaksi.hitungDampakSaldo()).
     */
    public double hitungSaldo() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .mapToDouble(Transaksi::hitungDampakSaldo)
                .sum();
    }

    /**
     * Laba bersih = pendapatan usaha (Penjualan) - beban usaha (Operasional).
     * Modal TIDAK dihitung sebagai pendapatan, sesuai PRD Bagian 3.3.
     */
    public double hitungLabaBersih() {
        return hitungTotalPenjualan() - hitungTotalOperasional();
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