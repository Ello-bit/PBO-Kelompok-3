package com.example.model;

/**
 * Enum KategoriTransaksi merepresentasikan kategori transaksi keuangan UMKM.
 * Sesuai PRD Bagian 3.3 & Bagian 6:
 * - MODAL: Penambahan modal usaha (menambah saldo, bukan pendapatan laba bersih).
 * - OPERASIONAL: Beban operasional usaha (mengurangi saldo, beban usaha).
 * - PENJUALAN: Hasil penjualan usaha (menambah saldo, pendapatan usaha).
 */
public enum KategoriTransaksi {
    MODAL,
    OPERASIONAL,
    PENJUALAN
}
