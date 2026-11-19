package com.example;

import java.io.IOException;

import com.example.app.dao.PenggunaDAO;
import com.example.app.models.Pengguna;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML 
    private TextField usernameField;

    @FXML 
    private PasswordField passwordField;

    private final PenggunaDAO penggunaDAO = new PenggunaDAO(); 

    @FXML
    private void goToSignUp(ActionEvent event) throws IOException {
        App.setRoot("signin");
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Username dan Password Wajib Di Isi");
            return;
        }

        Pengguna pengguna = penggunaDAO.login(username, password);

        if (pengguna == null){
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau Password Salah, atau Akun Tidak Aktif");
            return;
        }

        App.setCurrentUser(pengguna);

        App.setRoot("dashboard");
    }

    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}