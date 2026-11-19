package com.example.model;

import java.time.LocalDateTime;

/**
 * Class Pengeluaran merupakan turunan dari Transaksi yang mengurangi saldo usaha.
 * Konsep PBO: Inheritance, Polymorphism
 * Sesuai PRD Bagian 3.3 dan Bagian 6.
 */
public class Pengeluaran extends Transaksi {

    public Pengeluaran() {
        super();
    }

    public Pengeluaran(int id, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori) {
        super(id, nominal, tanggal, catatan, kategori);
    }

    public Pengeluaran(int id, int idUsaha, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori) {
        super(id, idUsaha, nominal, tanggal, catatan, kategori);
    }

    /**
     * Pengeluaran mengurangi saldo usaha.
     * @return nominal negatif (-nominal)
     */
    @Override
    public double hitungDampakSaldo() {
        return -getNominal();
    }

    @Override
    public String getJenisTransaksi() {
        return "PENGELUARAN";
    }

    /**
     * Mengecek apakah pengeluaran ini dihitung sebagai beban usaha dalam laba bersih.
     * Sesuai PRD Bagian 3.3:
     * - Operasional: beban usaha.
     */
    public boolean isBebanUsaha() {
        return this.kategori == KategoriTransaksi.OPERASIONAL;
    }
}
