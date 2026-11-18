package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML private StackPane contentArea;
    @FXML private Button dashboardButton;
    @FXML private Button profilButton;
    @FXML private Button transaksiButton;
    @FXML private Button laporanButton;

    @FXML
    private void handleProfilUsaha() {
        muatHalaman("/com/example/profilUsaha.fxml", profilButton);
    }

    @FXML
    private void handleTransaksi() {
        muatHalaman("/com/example/transaksi.fxml", transaksiButton);
    }

    @FXML
    private void handleLaporanKUR() {
        muatHalaman("/com/example/laporan/laporan.fxml", laporanButton);
    }

    private void muatHalaman(String resource, Button tombolAktif) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Parent halaman = loader.load();
            contentArea.getChildren().setAll(halaman);

            for (Button tombol : new Button[] {
                    dashboardButton, profilButton, transaksiButton, laporanButton }) {
                if (tombol != null) {
                    tombol.getStyleClass().setAll(tombol == tombolAktif
                            ? "sidebar-item-active" : "sidebar-item");
                }
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}
