package com.example.app.models;

import java.time.LocalDateTime;

public class Pemasukan extends Transaksi {

    public Pemasukan (int id, double nominal, LocalDateTime tanggal, String catatan, KategoriTransaksi kategori){
        super(id, nominal, tanggal, catatan, kategori);
    }
    
    @Override 
    public double hitungDampakSaldo(){
        return getNominal();
    }
}