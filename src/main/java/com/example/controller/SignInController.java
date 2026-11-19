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

/*
 * KELAS: SignInController
 * Controller halaman signin.fxml (pendaftaran akun Pemilik UMKM).
 * KONSEP OOP:
 *  - POLYMORPHISM : akun baru dibuat sebagai PemilikUMKM tetapi dikirim ke
 *                   PenggunaDAO sebagai tipe Pengguna (induk).
 *
 * CATATAN: email dipakai sebagai nama login (kolom username di tabel pengguna).
 * Tabel pengguna tidak punya kolom nama, jadi nama hanya dipakai untuk pesan sambutan.
 */
public class SignInController {

    @FXML private TextField namaField;
    @FXML private TextField emailField;
    @FXML private TextField namaUsahaField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void daftar() throws IOException {
        // LANGKAH 1: ambil isi form
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String namaUsaha = namaUsahaField.getText().trim();
        String password = passwordField.getText();
        String konfirmasi = confirmPasswordField.getText();

        // LANGKAH 2: periksa isian. Nama usaha boleh kosong (bisa diisi nanti di menu Profil Usaha).
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            App.tampilkanPesan("Nama, email, password, dan konfirmasi password wajib diisi.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            App.tampilkanPesan("Format email tidak valid. Contoh: nama@email.com");
            return;
        }
        if (!password.equals(konfirmasi)) {
            App.tampilkanPesan("Password dan konfirmasi password tidak sama.");
            return;
        }

        // LANGKAH 3: simpan akun baru (sekaligus membuat profil usaha kosong)
        Pengguna baru = new PemilikUMKM(0, email, password, "PEMILIK", true, null);
        boolean berhasil = PenggunaDAO.daftarPengguna(baru);
        if (!berhasil) {
            App.tampilkanPesan("Pendaftaran gagal. Email mungkin sudah terdaftar atau database belum tersambung.");
            return;
        }

        // LANGKAH 4: kalau nama usaha diisi, simpan ke profil yang baru dibuat
        if (!namaUsaha.isEmpty()) {
            simpanNamaUsaha(email, password, namaUsaha);
        }

        // LANGKAH 5: beri tahu pengguna, lalu kembali ke halaman login
        App.tampilkanPesan("Pendaftaran berhasil. Selamat datang, " + nama + "! Silakan login memakai email Anda.");
        App.setRoot("login");
    }

    // Mengisi nama usaha pada profil yang baru dibuat oleh PenggunaDAO.daftarPengguna().
    private void simpanNamaUsaha(String email, String password, String namaUsaha) {
        Pengguna akun = null;
        try {
            akun = PenggunaDAO.login(email, password);
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
