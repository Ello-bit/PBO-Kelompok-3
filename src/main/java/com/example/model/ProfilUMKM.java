package com.example.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProfilUMKM {

    private int idUsaha;
    private String namaUsaha;
    private String jenisUsaha;
    private String alamat;

    private final List<Transaksi> daftarTransaksi = new ArrayList<>();

    private String periode;

    private static final DateTimeFormatter FORMAT_PERIODE =
            DateTimeFormatter.ofPattern(
                    "MMMM yyyy", new Locale("id", "ID")
            );

    public ProfilUMKM(int idUsaha, String namaUsaha,
                      String jenisUsaha, String alamat) {
        this.idUsaha = idUsaha;
        setNamaUsaha(namaUsaha);
        setJenisUsaha(jenisUsaha);
        setAlamat(alamat);
    }

    // Pengelolaan transaksi
    public void tambahTransaksi() {
        // Dipertahankan untuk kompatibilitas kode lama.
    }

    public void tambahTransaksi(Transaksi transaksi) {
        daftarTransaksi.add(transaksi);
    }

    public List<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    // Periode dashboard
    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    // Nilai periode yang berarti "tanpa batas periode" (semua transaksi dihitung)
    public static final String SEMUA_PERIODE = "Semua Periode";

    public List<String> getDaftarPeriode() {
        List<String> daftar = daftarTransaksi.stream()
                .map(t -> t.getTanggal().format(FORMAT_PERIODE))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        // Pilihan pertama selalu "Semua Periode", disusul bulan yang punya transaksi
        daftar.add(0, SEMUA_PERIODE);
        return daftar;
    }

    private boolean sesuaiPeriode(Transaksi t) {
        if (periode == null || periode.isBlank() || periode.equals(SEMUA_PERIODE)) {
            return true;
        }

        return periode.equalsIgnoreCase(
                t.getTanggal().format(FORMAT_PERIODE)
        );
    }

    // Perhitungan keuangan
    public double hitungTotalPemasukan() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t instanceof Pemasukan)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    public double hitungTotalPengeluaran() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t instanceof Pengeluaran)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    public double hitungTotalPenjualan() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t.getKategori() == KategoriTransaksi.PENJUALAN)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    public double hitungTotalOperasional() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .filter(t -> t.getKategori() == KategoriTransaksi.OPERASIONAL)
                .mapToDouble(Transaksi::getNominal)
                .sum();
    }

    public double hitungSaldo() {
        return daftarTransaksi.stream()
                .filter(this::sesuaiPeriode)
                .mapToDouble(Transaksi::hitungDampakSaldo)
                .sum();
    }

    public double hitungLabaBersih() {
        return hitungTotalPenjualan() - hitungTotalOperasional();
    }

    // Getter dan setter
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
            throw new IllegalArgumentException(
                    "Nama usaha tidak boleh kosong."
            );
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
        return "ProfilUMKM{idUsaha=" + idUsaha
                + ", namaUsaha='" + namaUsaha
                + "', jenisUsaha='" + jenisUsaha
                + "', alamat='" + alamat + "'}";
    }
}