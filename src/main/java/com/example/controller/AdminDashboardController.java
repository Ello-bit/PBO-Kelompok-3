package com.example.controller;

import com.example.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/*
 * Controller kerangka dashboard admin (admin_dashboard.fxml): sidebar kiri, isi halaman kanan.
 * Cara kerjanya sama dengan DashboardController milik pemilik.
 */
public class AdminDashboardController {

    @FXML private StackPane contentArea;

    // Halaman pertama yang tampil: Kelola Pengguna
    @FXML
    public void initialize() {
        bukaHalaman("kelola_pengguna");
    }

    @FXML private void menuKelolaPengguna() { bukaHalaman("kelola_pengguna"); }
    @FXML private void menuVerifikasi()     { bukaHalaman("verifikasi_laporan"); }

    @FXML
    private void logout() throws Exception {
        App.currentUser.logout();    // tandai pengguna sudah keluar
        App.currentUser = null;
        App.setRoot("login");
    }

    private void bukaHalaman(String namaFile) {
        try {
            Parent halaman = FXMLLoader.load(App.class.getResource(namaFile + ".fxml"));
            contentArea.getChildren().setAll(halaman);
        } catch (Exception e) {
            System.out.println("Gagal membuka halaman " + namaFile + ": " + e.getMessage());
        }
    }
}
