package JocDelPingui;

import JocDelPingui.controller.gestionBBD;
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
    private gestionBBD gestionBD;
    private String usuariActual;  
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("El Joc del Pingüí");
        this.primaryStage.setMinWidth(1000);
        this.primaryStage.setMinHeight(700);
        
        setPantallaCompleta();
        
        
        gestionBD = new gestionBBD();
        
        mostrarMenu();
    }
    
    public void mostrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaMenu.fxml"));
            Parent root = loader.load();
            
            menuView controller = loader.getController();
            controller.setMainApp(this);
            controller.setGestionBD(gestionBD);
            
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            setPantallaCompleta();
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
            controller.setGestionBD(gestionBD);
            controller.setUsuariActual(usuariActual);
            
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            setPantallaCompleta();
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
            controller.setGestionBD(gestionBD);
            controller.setUsuariActual(usuariActual);
            
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            setPantallaCompleta();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cargarPartida(int numPartida) {
        try {
            partida partidaCarregada = gestionBD.carregarPartidaCompleta(numPartida);
            
            if (partidaCarregada == null) {
                System.out.println("❌ No s'ha pogut carregar la partida " + numPartida);
                return;
            }

            if (partidaCarregada.getJugadores().isEmpty()) {
                System.out.println("❌ La partida " + numPartida + " no té jugadors.");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            partidaView controller = loader.getController();
            controller.setPartida(partidaCarregada);
            controller.setMainApp(this);
            controller.setGestionBD(gestionBD);
            controller.setUsuariActual(usuariActual);
            
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            setPantallaCompleta();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getUsuariActual() {
        return usuariActual;
    }
    
    public void setUsuariActual(String usuariActual) {
        this.usuariActual = usuariActual;
    }
    
    public void setPantallaCompleta() {
        if (primaryStage != null) {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
        }
    }
    
    @Override
    public void stop() {
        if (gestionBD != null) {
            gestionBD.tancar();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}