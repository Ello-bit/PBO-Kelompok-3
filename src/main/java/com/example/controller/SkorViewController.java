package com.example.controller;

import com.example.model.SkorKelayakan;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SkorViewController {

    @FXML private Label lblTotalSkor;
    @FXML private Label lblLevel;
    @FXML private Label lblPeriode;

    @FXML private Label lblK1;
    @FXML private Label lblK2;
    @FXML private Label lblK3;
    @FXML private Label lblK4;

    @FXML private Button btnHitungSkor;

    private final SkorController skorController =
            new SkorController();

    // Sementara menggunakan ID UMKM 1 untuk pengujian
    private final int idUsaha = 1;

    @FXML
    public void initialize() {
        hitungSkor();
    }

    @FXML
    public void hitungSkor() {

        System.out.println("TOMBOL PERBARUI SKOR DIKLIK!");


        btnHitungSkor.setDisable(true);
        btnHitungSkor.setText("Menghitung...");

        try {
            // Memproses perhitungan skor
            SkorKelayakan skor =
                    skorController.prosesSkor(idUsaha);

            // Menampilkan hasil ke halaman JavaFX
            lblTotalSkor.setText(
                    String.valueOf(skor.getNilaiSkor())
            );

            lblK1.setText(
                    String.valueOf(skor.getSkorK1())
            );

            lblK2.setText(
                    String.valueOf(skor.getSkorK2())
            );

            lblK3.setText(
                    String.valueOf(skor.getSkorK3())
            );

            lblK4.setText(
                    String.valueOf(skor.getSkorK4())
            );

            lblPeriode.setText(
                    "Periode: " + skor.getPeriode()
            );

            lblLevel.setText(
                    "Level " + skor.getLevel()
                    + " - " + getNamaLevel(skor.getLevel())
            );

        } catch (SQLException e) {

            tampilkanError(
                    "Gagal menghitung skor: " + e.getMessage()
            );

        } finally {

            btnHitungSkor.setDisable(false);
            btnHitungSkor.setText("Hitung Ulang Skor");
        }
    }

    private String getNamaLevel(int level) {

        switch (level) {
            case 3:
                return "Baik";
            case 2:
                return "Cukup";
            default:
                return "Rendah";
        }
    }

    private void tampilkanError(String pesan) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText("Proses Skor Gagal");
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}