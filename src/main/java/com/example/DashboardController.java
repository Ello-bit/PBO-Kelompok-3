
package com.example;

import java.io.IOException;
import java.util.Optional;

import com.example.app.models.Pengguna;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {

        Pengguna pengguna = App.getCurrentUser();

        if (pengguna == null) {
            kembaliKeLogin();
            return;
        }

        System.out.println("Pengguna login: " + pengguna.getUsername());
        System.out.println("ID pengguna: " + pengguna.getId());
    }

    @FXML
    private void handleLogout() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah kamu yakin ingin logout?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            App.clearCurrentUser();
            kembaliKeLogin();
        }
    }

    private void kembaliKeLogin() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}