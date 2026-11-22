package com.example.controller;

import com.example.App;
import com.example.database.PenggunaDAO;
import com.example.database.ProfilDAO;
import com.example.model.PemilikUMKM;
import com.example.model.Pengguna;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void goToSignUp(ActionEvent event) throws IOException {
        App.setRoot("signin");
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            App.tampilkanPesan("Username dan password wajib diisi.");
            return;
        }

        Pengguna pengguna = null;
        try {
            pengguna = PenggunaDAO.login(username, password);
        } catch (SQLException e) {
            App.tampilkanPesan("Tidak dapat terhubung ke database.\n"
                    + "Pastikan MySQL Laragon menyala dan variabel DB_PASSWORD sudah benar.\n\n"
                    + "Detail: " + e.getMessage());
            return;
        }
        if (pengguna == null) {
            App.tampilkanPesan("Username atau password salah.");
            return;
        }

        if (!pengguna.isAktif()) {
            App.tampilkanPesan("Akun ini sudah dinonaktifkan oleh Admin.");
            return;
        }

        App.currentUser = pengguna;
        boolean penggunaAdalahAdmin = pengguna.getRole().equalsIgnoreCase("Admin");

        if (!penggunaAdalahAdmin) {
            PemilikUMKM pemilik = (PemilikUMKM) pengguna;
            pemilik.setProfilUMKM(ProfilDAO.ambilProfilPemilik(pemilik.getId()));
        }

        pengguna.getDashboard();

        if (penggunaAdalahAdmin) {
            App.setRoot("admin_dashboard");
        } else {
            App.setRoot("dashboard");
        }
    }
}
