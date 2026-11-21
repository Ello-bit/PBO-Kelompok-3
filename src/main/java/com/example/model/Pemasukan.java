package com.example.model;

import java.time.LocalDateTime;



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

    
    
    @Override
    public double hitungDampakSaldo() {
        return getNominal();
    }

    @Override
    public String getJenisTransaksi() {
        return "PEMASUKAN";
    }

    
    public boolean isPendapatanUsaha() {
        return this.kategori == KategoriTransaksi.PENJUALAN;
    }
}
