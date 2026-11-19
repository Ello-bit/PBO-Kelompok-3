package com.example.controller;

import com.example.App;
import com.example.database.PenggunaDAO;
import com.example.model.Admin;
import com.example.model.PemilikUMKM;
import com.example.model.Pengguna;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/*
 * Controller halaman kelola_pengguna.fxml (khusus Admin).
 * KONSEP OOP:
 *  - POLYMORPHISM : tabel berisi objek bertipe Pengguna, padahal isinya campuran
 *                   Admin dan PemilikUMKM.
 *  - INHERITANCE  : Admin mewarisi Pengguna, lalu menambah method sendiri
 *                   (kelolaPengguna, ubahStatusPengguna) yang tidak dimiliki PemilikUMKM.
 */
public class KelolaPenggunaController {

    @FXML private TextField usernameBaruField;
    @FXML private PasswordField passwordBaruField;

    @FXML private TableView<Pengguna> tabelPengguna;
    @FXML private TableColumn<Pengguna, Integer> kolomId;
    @FXML private TableColumn<Pengguna, String> kolomUsername;
    @FXML private TableColumn<Pengguna, String> kolomRole;
    @FXML private TableColumn<Pengguna, String> kolomStatus;

    private Admin admin;

    @FXML
    public void initialize() {
        admin = (Admin) App.currentUser;   // CASTING: pengguna yang login pasti Admin di halaman ini
        kolomId.setCellValueFactory(new PropertyValueFactory<>("id"));
        kolomUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        kolomRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        kolomStatus.setCellValueFactory(new PropertyValueFactory<>("statusTeks"));

        refreshTabel();
    }

    @FXML
    private void tambahPengguna() {
        // LANGKAH 1: ambil teks dari form, cek tidak kosong
        String username = usernameBaruField.getText().trim();
        String password = passwordBaruField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            App.tampilkanPesan("Username dan password wajib diisi.");
            return;
        }

        // LANGKAH 2: buat akun PemilikUMKM baru, lalu simpan lewat PenggunaDAO.
        Pengguna baru = new PemilikUMKM(0, username, password, "PEMILIK", true, null);
        if (!PenggunaDAO.daftarPengguna(baru)) {
            App.tampilkanPesan("Gagal menambah pengguna. Username mungkin sudah dipakai.");
            return;
        }

        // LANGKAH 3: kosongkan form dan perbarui tabel
        usernameBaruField.clear();
        passwordBaruField.clear();
        refreshTabel();
    }

    @FXML
    private void ubahStatus() {
        // LANGKAH 1: ambil baris yang dipilih (null kalau belum ada yang dipilih)
        Pengguna dipilih = tabelPengguna.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            App.tampilkanPesan("Pilih dulu akun yang ingin diubah statusnya.");
            return;
        }

        // LANGKAH 2: admin tidak boleh menonaktifkan akunnya sendiri (nanti tidak bisa login lagi)
        if (dipilih.getId() == admin.getId()) {
            App.tampilkanPesan("Anda tidak bisa menonaktifkan akun Anda sendiri.");
            return;
        }

        // LANGKAH 3: balik status di objek (aktif <-> nonaktif), lalu simpan ke database
        admin.ubahStatusPengguna(dipilih);
        if (!PenggunaDAO.updateStatus(dipilih)) {
            App.tampilkanPesan("Gagal menyimpan perubahan status.");
        }

        // LANGKAH 4: muat ulang tabel dari database
        refreshTabel();
    }

    // Daftar pengguna diambil lewat method kelolaPengguna() milik Admin
    private void refreshTabel() {
        tabelPengguna.getItems().setAll(admin.kelolaPengguna());
    }
}
