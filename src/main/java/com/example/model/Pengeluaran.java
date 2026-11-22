package com.example.model;

import java.time.LocalDateTime;

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

    @Override
    public double hitungDampakSaldo() {
        return -getNominal();
    }

    @Override
    public String getJenisTransaksi() {
        return "PENGELUARAN";
    }

    public boolean isBebanUsaha() {
        return this.kategori == KategoriTransaksi.OPERASIONAL;
    }
}
