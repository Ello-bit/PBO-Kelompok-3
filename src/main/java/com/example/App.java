package com.example;

import java.io.File;
import java.io.IOException;

import com.example.model.Pengguna;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class App extends Application {

    // Pengguna yang sedang login. null = belum ada yang login.
    public static Pengguna currentUser;

    private static Scene scene;
    private static Stage stage;

    // Dipakai untuk membuka file (misal PDF laporan) dengan aplikasi bawaan komputer
    private static HostServices hostServices;

    @Override
    @SuppressWarnings("exports")
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        hostServices = getHostServices();
        scene = new Scene(loadFXML("login"), 720, 470);
        scene.getStylesheets().add(App.class.getResource("style/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Kurtera");
        stage.show();
        stage.setFullScreen(true);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Menampilkan kotak pesan. Dipakai semua controller supaya tidak menulis Alert berulang-ulang.
    public static void tampilkanPesan(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        // Supaya pesan panjang tidak terpotong menjadi "..."
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    // Membuka file (contoh PDF laporan) dengan aplikasi bawaan komputer.
    public static void bukaFile(String lokasiFile) {
        File file = new File(lokasiFile);
        if (!file.exists()) {
            tampilkanPesan("File tidak ditemukan: " + lokasiFile);
            return;
        }
        hostServices.showDocument(file.toURI().toString());
    }

    // Mengubah angka jadi teks rupiah, contoh: 1500000 -> "Rp 1.500.000"
    // Locale Indonesia dipakai supaya hasilnya sama di semua laptop (titik sebagai pemisah ribuan).
    public static String formatRupiah(double angka) {
        return String.format(java.util.Locale.forLanguageTag("id-ID"), "Rp %,.0f", angka);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}