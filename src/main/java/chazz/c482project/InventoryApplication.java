package chazz.c482project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class InventoryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 834.0, 406.0);
        stage.setTitle("Inventory Package Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
        //begin working on adding product, poopulating table views
}