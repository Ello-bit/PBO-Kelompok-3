package com.example.app.models;

public class Pengeluaran extends Transaksi {

    public Pengeluaran() {
        super();
    }

    public Pengeluaran(int idTransaksi, int idUsaha, double nominal, String tanggal, String catatan,
            KategoriTransaksi kategori) {
        super(idTransaksi, idUsaha, nominal, tanggal, catatan, kategori);
    }

    public Pengeluaran(int idUsaha, double nominal, String tanggal, String catatan, KategoriTransaksi kategori) {
        super(idUsaha, nominal, tanggal, catatan, kategori);
    }

    @Override
    public double hitungDampakSaldo() {
        return -this.nominal;
    }

    @Override
    public String getJenisTransaksi() {
        return "PENGELUARAN";
    }

    public boolean isBebanUsaha() {
        return this.kategori == KategoriTransaksi.OPERASIONAL;
    }
}
