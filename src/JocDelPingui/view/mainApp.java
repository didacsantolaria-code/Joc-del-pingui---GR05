package JocDelPingui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import JocDelPingui.view.MenuView;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Crear el menú
        MenuView menu = new MenuView(this);
        
        // Crear la escena
        Scene scene = new Scene(menu, 400, 300);
        
        // Configurar ventana
        primaryStage.setTitle("Juego del Pingüino");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}