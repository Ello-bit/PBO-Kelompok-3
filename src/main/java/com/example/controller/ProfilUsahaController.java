package com.example.controller;

import com.example.App;
import com.example.database.ProfilDAO;
import com.example.model.PemilikUMKM;
import com.example.model.ProfilUMKM;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller untuk form input Profil UMKM.
 * Menerima input nama usaha, jenis usaha, dan alamat dari pengguna,
 * memvalidasi, lalu menyimpan/memperbarui objek ProfilUMKM.
 *
 * Data profil diambil dari pengguna yang sedang login (App.currentUser)
 * dan disimpan ke tabel profil_umkm lewat ProfilDAO.
 */
public class ProfilUsahaController {

    @FXML private TextField namaUsahaField;
    @FXML private ComboBox<String> jenisUsahaCombo;
    @FXML private TextArea alamatArea;
    @FXML private Button simpanButton;
    @FXML private Button resetButton;
    @FXML private Label statusLabel;

    // Pengguna yang sedang login (diambil dari App.currentUser)
    private PemilikUMKM pemilikAktif;

    // Nilai awal yang dibuat saat pendaftaran; ditampilkan sebagai kolom kosong
    private static final String BELUM_DIISI = "Belum diisi";

    @FXML
    public void initialize() {
        jenisUsahaCombo.getItems().addAll("Kuliner", "Fashion", "Jasa", "Kerajinan", "Lainnya");

        if (App.currentUser instanceof PemilikUMKM) {
            pemilikAktif = (PemilikUMKM) App.currentUser;
        }

        muatProfilJikaAda();
    }

    private void muatProfilJikaAda() {
        if (pemilikAktif == null || !pemilikAktif.hasProfilUMKM()) {
            tampilkanError("Profil usaha tidak ditemukan. Silakan login ulang.");
            return;
        }

        ProfilUMKM p = pemilikAktif.getProfilUMKM();
        if (!p.getNamaUsaha().equals(BELUM_DIISI)) {
            namaUsahaField.setText(p.getNamaUsaha());
        }
        if (p.getJenisUsaha() != null && !p.getJenisUsaha().equals(BELUM_DIISI)) {
            jenisUsahaCombo.setValue(p.getJenisUsaha());
        }
        if (p.getAlamat() != null && !p.getAlamat().equals(BELUM_DIISI)) {
            alamatArea.setText(p.getAlamat());
        }
        statusLabel.setText("Lengkapi atau perbarui data usaha Anda di bawah.");
    }

    @FXML
    private void onSimpan() {
        String nama = namaUsahaField.getText() != null ? namaUsahaField.getText().trim() : "";
        String jenis = jenisUsahaCombo.getValue() != null ? jenisUsahaCombo.getValue().trim() : "";
        String alamat = alamatArea.getText() != null ? alamatArea.getText().trim() : "";

        // ===== Validasi input =====
        if (nama.isEmpty()) {
            tampilkanError("Nama usaha wajib diisi.");
            return;
        }
        if (nama.length() < 3) {
            tampilkanError("Nama usaha minimal 3 karakter.");
            return;
        }
        if (jenis.isEmpty()) {
            tampilkanError("Jenis usaha wajib dipilih atau diisi.");
            return;
        }
        if (alamat.isEmpty()) {
            tampilkanError("Alamat usaha wajib diisi.");
            return;
        }

        if (pemilikAktif == null || !pemilikAktif.hasProfilUMKM()) {
            tampilkanError("Profil usaha tidak ditemukan. Silakan login ulang.");
            return;
        }

        try {
            // LANGKAH 1: ubah isi objek profil di memori
            ProfilUMKM p = pemilikAktif.getProfilUMKM();
            p.setNamaUsaha(nama);
            p.setJenisUsaha(jenis);
            p.setAlamat(alamat);

            // LANGKAH 2: simpan perubahan ke database lewat ProfilDAO
            boolean berhasil = ProfilDAO.simpanProfil(p);
            if (berhasil) {
                tampilkanSukses("Profil usaha berhasil disimpan.");
            } else {
                tampilkanError("Gagal menyimpan ke database. Periksa koneksi MySQL.");
            }
        } catch (IllegalArgumentException ex) {
            tampilkanError(ex.getMessage());
        }
    }

    @FXML
    private void onReset() {
        namaUsahaField.clear();
        jenisUsahaCombo.setValue(null);
        alamatArea.clear();
        statusLabel.setText("");
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
    }

    private void tampilkanError(String pesan) {
        statusLabel.setText(pesan);
        statusLabel.getStyleClass().removeAll("status-success");
        if (!statusLabel.getStyleClass().contains("status-error")) {
            statusLabel.getStyleClass().add("status-error");
        }
    }

    private void tampilkanSukses(String pesan) {
        statusLabel.setText(pesan);
        statusLabel.getStyleClass().removeAll("status-error");
        if (!statusLabel.getStyleClass().contains("status-success")) {
            statusLabel.getStyleClass().add("status-success");
        }
    }
}