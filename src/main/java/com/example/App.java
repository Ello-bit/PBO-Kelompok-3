package com.example;

import java.io.IOException;

import com.example.app.models.Pengguna;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private static Pengguna currentUser;

    @Override
    @SuppressWarnings("exports")
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        scene = new Scene(loadFXML("login"), 720, 470);
        scene.getStylesheets().add(App.class.getResource("style/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Kurtera");
        stage.show();
        stage.setFullScreen(true);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setCurrentUser (Pengguna pengguna){
        currentUser = pengguna;
    }

    public static Pengguna getCurrentUser(){
        return currentUser;
    }

    public static void clearCurrentUser(){
        currentUser = null;
    }

    public static void main(String[] args) {
        launch();
    }
}