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
        System.out.println("5. ENTRANDO A nuevaPartida()");
        try {
            System.out.println("6. Creando partida...");
            partida partida = new partida();
            partida.inicializarPartida("Jugador 1");
            System.out.println("7. Partida creada con " + partida.getJugadores().size() + " jugadores");
            
            System.out.println("8. Cargando FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaJuego.fxml"));
            Parent root = loader.load();
            System.out.println("9. FXML cargado correctamente");
            
            System.out.println("10. Obteniendo controller...");
            partidaView controller = loader.getController();
            System.out.println("11. Controller obtenido: " + (controller != null ? "OK" : "NULL"));
            
            System.out.println("12. Asignando partida al controller...");
            controller.setPartida(partida);
            System.out.println("13. Partida asignada");
            
            System.out.println("14. Creando escena...");
            Scene scene = new Scene(root, 1000, 700);
            
            System.out.println("15. Cambiando escena...");
            primaryStage.setScene(scene);
            System.out.println("16. Escena cambiada - DEBERÍAS VER LA PANTALLA DE JUEGO");
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}