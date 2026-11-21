package com.example.controller;

import com.example.App;
import com.example.database.PenggunaDAO;
import com.example.database.ProfilDAO;
import com.example.model.PemilikUMKM;
import com.example.model.Pengguna;
import com.example.model.ProfilUMKM;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController {

    @FXML private TextField namaField;
    @FXML private TextField usernameField;
    @FXML private TextField namaUsahaField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void daftar() throws IOException {

        String nama = namaField.getText().trim();
        String username = usernameField.getText().trim();
        String namaUsaha = namaUsahaField.getText().trim();
        String password = passwordField.getText();
        String konfirmasi = confirmPasswordField.getText();

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            App.tampilkanPesan("Nama, username, password, dan konfirmasi password wajib diisi.");
            return;
        }
        if (username.contains(" ")) {
            App.tampilkanPesan("Username tidak boleh mengandung spasi.");
            return;
        }
        if (!password.equals(konfirmasi)) {
            App.tampilkanPesan("Password dan konfirmasi password tidak sama.");
            return;
        }

        Pengguna baru = new PemilikUMKM(0, username, password, "PEMILIK", true, null);
        boolean berhasil = PenggunaDAO.daftarPengguna(baru);
        if (!berhasil) {
            App.tampilkanPesan("Pendaftaran gagal. Username mungkin sudah dipakai atau database belum tersambung.");
            return;
        }

        if (!namaUsaha.isEmpty()) {
            simpanNamaUsaha(username, password, namaUsaha);
        }

        App.tampilkanPesan("Pendaftaran berhasil. Selamat datang, " + nama + "! Silakan login memakai username Anda.");
        App.setRoot("login");
    }

    private void simpanNamaUsaha(String username, String password, String namaUsaha) {
        Pengguna akun = null;
        try {
            akun = PenggunaDAO.login(username, password);
        } catch (SQLException e) {
            System.out.println("Gagal membaca akun baru: " + e.getMessage());
        }
        if (akun == null) {
            return;
        }
        ProfilUMKM profil = ProfilDAO.ambilProfilPemilik(akun.getId());
        if (profil == null) {
            return;
        }
        profil.setNamaUsaha(namaUsaha);
        ProfilDAO.simpanProfil(profil);
    }
}
