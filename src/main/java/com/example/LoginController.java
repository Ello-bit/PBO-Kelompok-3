package com.example;
import java.io.IOException;

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
        App.setRoot("dashboard");
    }
}
