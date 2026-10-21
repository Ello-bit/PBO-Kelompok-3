import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginView;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(LoginView.createView(), 400, 300);

        stage.setTitle("Sistem Kelayakan UMKM");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}