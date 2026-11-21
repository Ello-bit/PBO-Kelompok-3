package com.example.controller;

import com.example.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        bukaHalaman("beranda");
    }

    @FXML private void menuBeranda() { bukaHalaman("beranda"); }
    @FXML private void menuProfil()  { bukaHalaman("profilUsaha"); }
    @FXML private void menuTransaksi() { bukaHalaman("transaksi"); }
    @FXML private void menuSkor()      { bukaHalaman("skorkelayakan"); }
    @FXML private void menuLaporan()   { bukaHalaman("laporan"); }

    @FXML
    private void logout() throws Exception {
        App.currentUser.logout();    // tandai pengguna sudah keluar
        App.currentUser = null;      // kosongkan pengguna yang sedang login
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
