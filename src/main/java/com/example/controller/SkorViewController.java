package com.example.controller;

import com.example.App;
import com.example.model.PemilikUMKM;
import com.example.model.SkorKelayakan;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class SkorViewController {

    @FXML private Label lblTotalSkor;
    @FXML private Label lblLevel;
    @FXML private Label lblPeriode;

    @FXML private Label lblK1;
    @FXML private Label lblK2;
    @FXML private Label lblK3;
    @FXML private Label lblK4;


    private final SkorController skorController =
            new SkorController();

    private int idUsaha;

    @FXML
    public void initialize() {

        if (!(App.currentUser instanceof PemilikUMKM)) {
            lblLevel.setText("Skor hanya untuk Pemilik UMKM");
            return;
        }


        PemilikUMKM pemilik = (PemilikUMKM) App.currentUser;
        if (!pemilik.hasProfilUMKM()) {
            lblLevel.setText("Lengkapi Profil Usaha terlebih dahulu");
            return;
        }
        idUsaha = pemilik.getProfilUMKM().getIdUsaha();


        hitungSkor();
    }

    @FXML
    private void hitungSkor() {

        try {
        
            SkorKelayakan skor =
                    skorController.prosesSkor(idUsaha);

        
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
        } 
    }

    private String getNamaLevel(int level) {

        switch (level) {
            case 5:
                return "Sangat Baik";
            case 4:
                return "Baik";
            case 3:
                return "Cukup";
            case 2:
                return "Kurang";
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