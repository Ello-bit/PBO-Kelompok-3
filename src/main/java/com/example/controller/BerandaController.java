package com.example.controller;

import com.example.App;
import com.example.model.PemilikUMKM;
import com.example.model.ProfilUMKM;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

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
        PemilikUMKM pemilik = (PemilikUMKM) App.currentUser;
        profil = pemilik.getProfil();

        labelSapaan.setText("Halo, " + App.currentUser.getUsername());

        periodeCombo.getItems().setAll(profil.getDaftarPeriode());
        if (!periodeCombo.getItems().contains(profil.getPeriode())) {
            profil.setPeriode("Semua Periode");
        }
        periodeCombo.setValue(profil.getPeriode());

        tampilkanAngka();
    }

    @FXML
    private void gantiPeriode() {
        if (periodeCombo.getValue() == null) {
            return;
        }
        profil.setPeriode(periodeCombo.getValue());
        tampilkanAngka();
    }

    private void tampilkanAngka() {
        labelSaldo.setText(App.formatRupiah(profil.hitungSaldo()));
        labelPenjualan.setText(App.formatRupiah(profil.hitungTotalPenjualan()));
        labelOperasional.setText(App.formatRupiah(profil.hitungTotalOperasional()));
        labelLaba.setText(App.formatRupiah(profil.hitungLabaBersih()));

        double pemasukan = profil.hitungTotalPemasukan();
        double pengeluaran = profil.hitungTotalPengeluaran();
        labelPemasukan.setText(App.formatRupiah(pemasukan));
        labelPengeluaran.setText(App.formatRupiah(pengeluaran));

        grafikKeuangan.getData().clear();
        grafikKeuangan.getData().add(new PieChart.Data("Pemasukan", pemasukan));
        grafikKeuangan.getData().add(new PieChart.Data("Pengeluaran", pengeluaran));
    }
}
