package com.example.app.models;

import java.time.LocalDateTime;

public class Pengeluaran extends Transaksi {
    
    public Pengeluaran (int id, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori){
        super(id, nominal, tanggal, catatan, kategori);
    }

    @Override 
    public double hitungDampakSaldo(){
        return -getNominal();
    }
}