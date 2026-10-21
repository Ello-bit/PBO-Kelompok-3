//nyoba pake ai ini yaa

package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {

    public static VBox createView() {
        Label judul = new Label("Sistem Kelayakan UMKM");
        Label labelUsername = new Label("Username");
        TextField username = new TextField();
        username.setPromptText("Masukkan username");

        Label labelPassword = new Label("Password");
        PasswordField password = new PasswordField();
        password.setPromptText("Masukkan password");

        Button tombolLogin = new Button("Login");

        Label pesan = new Label();

        tombolLogin.setOnAction(event -> {
            pesan.setText("Tampilan login berhasil dijalankan!");
        });

        VBox layout = new VBox(
            10,
            judul,
            labelUsername,
            username,
            labelPassword,
            password,
            tombolLogin,
            pesan
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return layout;
    }
}