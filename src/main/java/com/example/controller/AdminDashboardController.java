package com.example.controller;

import com.example.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class AdminDashboardController {

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        bukaHalaman("kelola_pengguna");
    }

    @FXML private void menuKelolaPengguna() { bukaHalaman("kelola_pengguna"); }
    @FXML private void menuVerifikasi()     { bukaHalaman("verifikasi_laporan"); }

    @FXML
    private void logout() throws Exception {
        App.currentUser.logout();
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
