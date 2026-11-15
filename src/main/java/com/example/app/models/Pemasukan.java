package com.example.app.models;

public class Pemasukan extends Transaksi {

    public Pemasukan() {
        super();
    }

    public Pemasukan(int idTransaksi, int idUsaha, double nominal, String tanggal, String catatan,
            KategoriTransaksi kategori) {
        super(idTransaksi, idUsaha, nominal, tanggal, catatan, kategori);
    }

    public Pemasukan(int idUsaha, double nominal, String tanggal, String catatan, KategoriTransaksi kategori) {
        super(idUsaha, nominal, tanggal, catatan, kategori);
    }

    @Override
    public double hitungDampakSaldo() {
        return this.nominal;
    }

    @Override
    public String getJenisTransaksi() {
        return "PEMASUKAN";
    }

    /**
     * Mengecek apakah pemasukan ini dihitung sebagai pendapatan usaha dalam
     * perhitungan laba bersih.
     * Sesuai PRD Bagian 3.3:
     * - Penjualan: dihitung sebagai pendapatan usaha.
     * - Modal: penambahan kas tapi BUKAN pendapatan usaha (laba).
     */
    public boolean isPendapatanUsaha() {
        return this.kategori == KategoriTransaksi.PENJUALAN;
    }
}
