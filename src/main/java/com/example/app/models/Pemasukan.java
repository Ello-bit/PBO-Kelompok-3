package com.example.app.models;

import java.time.LocalDateTime;

/**
 * Class Pemasukan merupakan turunan dari Transaksi yang menambah saldo usaha.
 * Konsep PBO: Inheritance, Polymorphism
 * Sesuai PRD Bagian 3.3 dan Bagian 6.
 */
public class Pemasukan extends Transaksi {

    public Pemasukan() {
        super();
    }

    public Pemasukan(int id, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori) {
        super(id, nominal, tanggal, catatan, kategori);
    }

    public Pemasukan(int id, int idUsaha, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori) {
        super(id, idUsaha, nominal, tanggal, catatan, kategori);
    }

    /**
     * Pemasukan menambah saldo usaha.
     * @return nominal positif (+nominal)
     */
    @Override
    public double hitungDampakSaldo() {
        return getNominal();
    }

    @Override
    public String getJenisTransaksi() {
        return "PEMASUKAN";
    }

    /**
     * Mengecek apakah pemasukan ini dihitung sebagai pendapatan usaha dalam laba bersih.
     * Sesuai PRD Bagian 3.3:
     * - Penjualan: pendapatan usaha.
     * - Modal: bukan pendapatan usaha.
     */
    public boolean isPendapatanUsaha() {
        return this.kategori == KategoriTransaksi.PENJUALAN;
    }
}
