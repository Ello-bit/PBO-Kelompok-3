package com.example;
import java.io.IOException;

import javafx.fxml.FXML;

public class SignInController {

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }
}