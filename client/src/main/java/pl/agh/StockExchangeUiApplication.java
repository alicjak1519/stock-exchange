package pl.agh;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;

public class StockExchangeUiApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = Paths.get("./src/main/resources/StockExchangeUiApplication.fxml").toUri().toURL();
        Parent root = FXMLLoader.load(url);

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Stock Exchange");
        stage.setScene(scene);
        stage.show();
    }
}