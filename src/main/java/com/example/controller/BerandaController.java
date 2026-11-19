package com.example.controller;

import com.example.App;
import com.example.model.PemilikUMKM;
import com.example.model.ProfilUMKM;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/*
 * Controller halaman beranda.fxml: sapaan + 6 kartu ringkasan keuangan + grafik.
 * KONSEP OOP:
 *  - POLYMORPHISM : App.currentUser.getDashboard() dipanggil lewat tipe Pengguna;
 *                   yang dijalankan adalah versi milik PemilikUMKM.
 *  - POLYMORPHISM : profil.hitungSaldo() memanggil hitungDampakSaldo() tiap transaksi
 *                   (Pemasukan = +nominal, Pengeluaran = -nominal).
 */
public class BerandaController {

    @FXML private Label labelSapaan;
    @FXML private Label labelSaldo;
    @FXML private Label labelPenjualan;
    @FXML private Label labelOperasional;
    @FXML private Label labelLaba;
    @FXML private Label labelPemasukan;
    @FXML private Label labelPengeluaran;
    @FXML private PieChart grafikKeuangan;

    @FXML private ComboBox<String> periodeCombo;

    private ProfilUMKM profil;

    @FXML
    public void initialize() {
        // LANGKAH 1: ambil profil milik pengguna yang sedang login (CASTING ke tipe anak)
        PemilikUMKM pemilik = (PemilikUMKM) App.currentUser;
        profil = pemilik.getProfil();

        // LANGKAH 2: tampilkan sapaan
        labelSapaan.setText("Halo, " + App.currentUser.getUsername());

        // LANGKAH 3: isi pilihan periode ("Semua Periode" + bulan yang punya transaksi).
        // Kalau periode yang dipilih sebelumnya sudah tidak punya transaksi, kembali ke "Semua Periode".
        periodeCombo.getItems().setAll(profil.getDaftarPeriode());
        if (!periodeCombo.getItems().contains(profil.getPeriode())) {
            profil.setPeriode("Semua Periode");
        }
        periodeCombo.setValue(profil.getPeriode());

        // LANGKAH 4: tampilkan angka-angka
        tampilkanAngka();
    }

    // Dipanggil saat pengguna memilih periode lain di ComboBox
    @FXML
    private void gantiPeriode() {
        if (periodeCombo.getValue() == null) {
            return;
        }
        profil.setPeriode(periodeCombo.getValue());   // dipakai juga oleh halaman Skor & Laporan
        tampilkanAngka();
    }

    private void tampilkanAngka() {
        // Rumusnya ada di ProfilUMKM, dan hanya menghitung transaksi pada periode terpilih:
        //   Saldo Kas         = jumlah semua dampak saldo (MODAL & PENJUALAN +, OPERASIONAL -)
        //   Total Penjualan   = jumlah transaksi PENJUALAN
        //   Total Operasional = jumlah transaksi OPERASIONAL
        //   Laba Bersih       = Total Penjualan - Total Operasional
        labelSaldo.setText(App.formatRupiah(profil.hitungSaldo()));
        labelPenjualan.setText(App.formatRupiah(profil.hitungTotalPenjualan()));
        labelOperasional.setText(App.formatRupiah(profil.hitungTotalOperasional()));
        labelLaba.setText(App.formatRupiah(profil.hitungLabaBersih()));

        // Total Pemasukan   = jumlah semua transaksi berjenis PEMASUKAN (MODAL + PENJUALAN)
        // Total Pengeluaran = jumlah semua transaksi berjenis PENGELUARAN
        double pemasukan = profil.hitungTotalPemasukan();
        double pengeluaran = profil.hitungTotalPengeluaran();
        labelPemasukan.setText(App.formatRupiah(pemasukan));
        labelPengeluaran.setText(App.formatRupiah(pengeluaran));

        // Grafik lingkaran: perbandingan pemasukan dan pengeluaran pada periode terpilih
        grafikKeuangan.getData().clear();
        grafikKeuangan.getData().add(new PieChart.Data("Pemasukan", pemasukan));
        grafikKeuangan.getData().add(new PieChart.Data("Pengeluaran", pengeluaran));
    }
}
