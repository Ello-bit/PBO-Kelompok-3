package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML private StackPane contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnProfil;
    @FXML private Button btnTransaksi;
    @FXML private Button btnSkor;
    @FXML private Button btnLaporan;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        // Tampilkan halaman Transaksi sebagai default saat pertama kali login
        menuTransaksi(null);
    }

    @FXML
    public void menuDashboard(ActionEvent event) {
        setActiveButton(btnDashboard);
        loadPage("transaksi"); // Mengarahkan ke transaksi/ringkasan
    }

    @FXML
    public void menuProfilUsaha(ActionEvent event) {
        setActiveButton(btnProfil);
        loadPage("profilUsaha");
    }

    @FXML
    public void menuTransaksi(ActionEvent event) {
        setActiveButton(btnTransaksi);
        loadPage("transaksi");
    }

    @FXML
    public void menuSkorKelayakan(ActionEvent event) {
        setActiveButton(btnSkor);
        loadPage("skorKelayakan");
    }

    @FXML
    public void menuLaporan(ActionEvent event) {
        setActiveButton(btnLaporan);
        loadPage("laporan");
    }

    @FXML
    public void logout(ActionEvent event) {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Memuat file FXML ke dalam contentArea di bagian tengah dashboard.
     */
    private void loadPage(String fxml) {
        try {
            var resource = getClass().getResource(fxml + ".fxml");
            if (resource == null) {
                // Tampilan fallback jika modul teman belum selesai dibuat
                tampilkanHalamanPlaceholder(fxml);
                return;
            }
            Parent view = FXMLLoader.load(resource);
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            tampilkanHalamanPlaceholder(fxml);
        }
    }

    private void setActiveButton(Button activeButton) {
        if (activeButton == null) return;
        Button[] buttons = {btnDashboard, btnProfil, btnTransaksi, btnSkor, btnLaporan};
        for (Button btn : buttons) {
            if (btn != null) {
                btn.getStyleClass().remove("sidebar-item-active");
                if (!btn.getStyleClass().contains("sidebar-item")) {
                    btn.getStyleClass().add("sidebar-item");
                }
            }
        }
        activeButton.getStyleClass().remove("sidebar-item");
        if (!activeButton.getStyleClass().contains("sidebar-item-active")) {
            activeButton.getStyleClass().add("sidebar-item-active");
        }
    }

    private void tampilkanHalamanPlaceholder(String namaModul) {
        VBox placeholder = new VBox(10);
        placeholder.setStyle("-fx-alignment: center; -fx-padding: 40;");
        Label lblJudul = new Label("Modul " + namaModul.toUpperCase() + " Sedang Dikembangkan");
        lblJudul.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #64748B;");
        Label lblKet = new Label("Halaman ini sedang dikerjakan oleh rekan kelompok Anda.");
        lblKet.setStyle("-fx-font-size: 13px; -fx-text-fill: #94A3B8;");
        placeholder.getChildren().addAll(lblJudul, lblKet);
        contentArea.getChildren().setAll(placeholder);
    }
}
