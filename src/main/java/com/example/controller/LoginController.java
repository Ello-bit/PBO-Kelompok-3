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
        // LANGKAH 1: ambil isi form dan pastikan tidak kosong
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            App.tampilkanPesan("Username dan password wajib diisi.");
            return;
        }

        // LANGKAH 2: cari akun di database. Hasilnya null kalau username/password salah.
        Pengguna pengguna = null;
        try {
            pengguna = PenggunaDAO.login(username, password);
        } catch (SQLException e) {
            // Database tidak bisa dihubungi (MySQL mati, atau DB_PASSWORD salah/belum diatur)
            App.tampilkanPesan("Tidak dapat terhubung ke database.\n"
                    + "Pastikan MySQL Laragon menyala dan variabel DB_PASSWORD sudah benar.\n\n"
                    + "Detail: " + e.getMessage());
            return;
        }
        if (pengguna == null) {
            App.tampilkanPesan("Username atau password salah.");
            return;
        }

        // LANGKAH 3: akun yang dinonaktifkan Admin tidak boleh masuk
        if (!pengguna.isAktif()) {
            App.tampilkanPesan("Akun ini sudah dinonaktifkan oleh Admin.");
            return;
        }

        // LANGKAH 4: simpan pengguna yang login, lalu buka dashboard sesuai perannya
        App.currentUser = pengguna;
        if (pengguna.getRole().equalsIgnoreCase("Admin")) {
            App.setRoot("admin_dashboard");
            return;
        }

        // LANGKAH 5 (khusus pemilik): muat profil usaha + transaksinya dari database,
        // lalu pasang ke objek PemilikUMKM (CASTING dari tipe Pengguna ke tipe anak)
        PemilikUMKM pemilik = (PemilikUMKM) pengguna;
        pemilik.setProfilUMKM(ProfilDAO.ambilProfilPemilik(pemilik.getId()));
        App.setRoot("dashboard");
    }
}
