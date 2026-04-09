package JocDelPingui;

import JocDelPingui.model.partida;
import JocDelPingui.view.menuView;
import JocDelPingui.view.partidaView;
import JocDelPingui.view.seleccionView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class mainApp extends Application {
    
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("El Joc del Pingüí");
        this.primaryStage.setMinWidth(1000);
        this.primaryStage.setMinHeight(700);
        this.primaryStage.setFullScreenExitHint("");
        this.primaryStage.setFullScreen(true);
        
        mostrarMenu();
    }
    
    public void mostrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaMenu.fxml"));
            Parent root = loader.load();
            
            menuView controller = loader.getController();
            controller.setMainApp(this);
            
            if (primaryStage.getScene() == null) {
                Scene scene = new Scene(root, 1000, 700);
                primaryStage.setScene(scene);
                primaryStage.setFullScreenExitHint("");
                primaryStage.setFullScreen(true);
            } else {
                primaryStage.getScene().setRoot(root);
            }
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void mostrarSeleccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaSeleccion.fxml"));
            Parent root = loader.load();
            
            seleccionView controller = loader.getController();
            controller.setMainApp(this);
            
            if (primaryStage.getScene() == null) {
                Scene scene = new Scene(root, 1000, 700);
                primaryStage.setScene(scene);
                primaryStage.setFullScreenExitHint("");
                primaryStage.setFullScreen(true);
            } else {
                primaryStage.getScene().setRoot(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void nuevaPartida(ArrayList<String[]> jugadoresInfo) {
        try {
            partida partida = new partida();
            partida.inicializarPartida(jugadoresInfo);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            partidaView controller = loader.getController();
            controller.setPartida(partida);
            controller.setMainApp(this);
            
            if (primaryStage.getScene() == null) {
                Scene scene = new Scene(root, 1000, 700);
                primaryStage.setScene(scene);
                primaryStage.setFullScreenExitHint("");
                primaryStage.setFullScreen(true);
            } else {
                primaryStage.getScene().setRoot(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}