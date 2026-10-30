package com.example;

import java.io.IOException;

import com.example.app.dao.PenggunaDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController {

    @FXML 
    private TextField usernameField;

    @FXML 
    private PasswordField passwordField;

    @FXML 
    private PasswordField confirmPasswordField;

    private final PenggunaDAO penggunaDAO = new PenggunaDAO();

    @FXML
    private void handleSignUp() {
        
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "peringatan", "Semua Kolom Wajib Di Isi");
            return;
        }

        if (password.length() < 8){
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Konfirmasi Password Tidak Cocok");
            return;
        }

        boolean berhasil = penggunaDAO.registrasi(username, password);

        if (berhasil){
            showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun Berhasil Dibuat. Silahkan Login");

            try{
                App.setRoot("login");
            }
            catch (IOException e){
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal Membuka Halaman Login");
                e.printStackTrace();
            }
        } else{
            showAlert (Alert.AlertType.ERROR, "Registrasi Gagal", "Username mungkin sudah digunakan atau Terjadi kesalahan database");
        }
    }

    @FXML 
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }

    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}