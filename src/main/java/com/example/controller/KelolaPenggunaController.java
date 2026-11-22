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
        admin = (Admin) App.currentUser;
        kolomId.setCellValueFactory(new PropertyValueFactory<>("id"));
        kolomUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        kolomRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        kolomStatus.setCellValueFactory(new PropertyValueFactory<>("statusTeks"));

        refreshTabel();
    }

    @FXML
    private void tambahPengguna() {
        String username = usernameBaruField.getText().trim();
        String password = passwordBaruField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            App.tampilkanPesan("Username dan password wajib diisi.");
            return;
        }

        Pengguna baru = new PemilikUMKM(0, username, password, "PEMILIK", true, null);
        if (!PenggunaDAO.daftarPengguna(baru)) {
            App.tampilkanPesan("Gagal menambah pengguna. Username mungkin sudah dipakai.");
            return;
        }

        usernameBaruField.clear();
        passwordBaruField.clear();
        refreshTabel();
    }

    @FXML
    private void ubahStatus() {
        Pengguna dipilih = tabelPengguna.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            App.tampilkanPesan("Pilih dulu akun yang ingin diubah statusnya.");
            return;
        }

        if (dipilih.getId() == admin.getId()) {
            App.tampilkanPesan("Anda tidak bisa menonaktifkan akun Anda sendiri.");
            return;
        }

        admin.ubahStatusPengguna(dipilih);
        if (!PenggunaDAO.updateStatus(dipilih)) {
            App.tampilkanPesan("Gagal menyimpan perubahan status.");
        }

        refreshTabel();
    }

    private void refreshTabel() {
        tabelPengguna.getItems().setAll(admin.kelolaPengguna());
    }
}
