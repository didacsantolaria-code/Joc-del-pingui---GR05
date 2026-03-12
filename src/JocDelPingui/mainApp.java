package JocDelPingui;

import javafx.application.Application;
import javafx.stage.Stage;

public class mainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("JavaFX funciona!");
        primaryStage.setTitle("Ventana vacía");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}