package com.example;

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
 * VERSI SEKARANG: data disimpan di memori saja (objek Java),
 * belum terhubung ke database. Koneksi database menyusul setelah
 * skema final disepakati tim (branch database-integration).
 */
public class profilUsahaController {

    @FXML private TextField namaUsahaField;
    @FXML private ComboBox<String> jenisUsahaCombo;
    @FXML private TextArea alamatArea;
    @FXML private Button simpanButton;
    @FXML private Button resetButton;
    @FXML private Label statusLabel;

    // Sementara: pengguna yang sedang login (akan digantikan oleh session/context nyata dari Alya)
    private PemilikUMKM pemilikAktif;

    private static int idUsahaCounter = 1;

    @FXML
    public void initialize() {
        jenisUsahaCombo.getItems().addAll("Kuliner", "Fashion", "Jasa", "Kerajinan", "Lainnya");

        // Contoh pengguna aktif sementara, ganti dengan data session login sesungguhnya
        pemilikAktif = new PemilikUMKM(1, "ayu.lestari", "dummy");

        muatProfilJikaAda();
    }

    private void muatProfilJikaAda() {
        if (pemilikAktif.hasProfilUMKM()) {
            ProfilUMKM p = pemilikAktif.getProfilUMKM();
            namaUsahaField.setText(p.getNamaUsaha());
            jenisUsahaCombo.setValue(p.getJenisUsaha());
            alamatArea.setText(p.getAlamat());
            statusLabel.setText("Profil usaha dimuat. Anda dapat memperbarui data di bawah.");
        }
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

        try {
            if (pemilikAktif.hasProfilUMKM()) {
                // Update profil yang sudah ada
                ProfilUMKM p = pemilikAktif.getProfilUMKM();
                p.setNamaUsaha(nama);
                p.setJenisUsaha(jenis);
                p.setAlamat(alamat);
                tampilkanSukses("Profil usaha berhasil diperbarui.");
            } else {
                // Buat profil baru
                ProfilUMKM baru = new ProfilUMKM(idUsahaCounter++, nama, jenis, alamat);
                pemilikAktif.setProfilUMKM(baru);
                tampilkanSukses("Profil usaha berhasil disimpan.");
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