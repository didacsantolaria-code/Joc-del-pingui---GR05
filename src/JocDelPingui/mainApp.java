package JocDelPingui;

import JocDelPingui.model.partida;
import JocDelPingui.view.menuView;
import JocDelPingui.view.partidaView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainApp extends Application {
    
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("El Joc del Pingüí");
        this.primaryStage.setMinWidth(1000);
        this.primaryStage.setMinHeight(700);
        
        mostrarMenu();
    }
    
    public void mostrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaMenu.fxml"));
            Parent root = loader.load();
            
            menuView controller = loader.getController();
            controller.setMainApp(this);
            
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void nuevaPartida() {
        try {
            partida partida = new partida();
            partida.inicializarPartida("Jugador 1");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            partidaView controller = loader.getController();
            controller.setPartida(partida);
            
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}