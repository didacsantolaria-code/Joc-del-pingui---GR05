package JocDelPingui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import JocDelPingui.mainApp;

public class menuView extends VBox {
    
    // Elementos del FXML
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    
    private mainApp mainApp;
    
    // Constructor vacío para FXML
    public menuView() {
        // No hacer nada aquí, la configuración se hace en initialize()
    }
    
    @FXML
    private void initialize() {
        System.out.println("menuView initialized");
        // Configurar estilos si es necesario
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setPadding(new Insets(50));
        getStyleClass().add("menu-container");
    }
    
    // 🔴 NUEVO MÉTODO: Para recibir mainApp desde mainApp.java
    public void setMainApp(mainApp mainApp) {
        this.mainApp = mainApp;
        System.out.println("mainApp asignado al menuView: " + (mainApp != null));
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = userField.getText();
        String password = passField.getText();
        
        System.out.println("1. Login pressed: " + username + " / " + password);
        
        if (!username.isEmpty() && !password.isEmpty()) {
            System.out.println("2. Campos válidos, mainApp es: " + (mainApp != null ? "no null" : "null"));
            if (mainApp != null) {
                System.out.println("3. Llamando a mainApp.nuevaPartida()");
                mainApp.nuevaPartida();
                System.out.println("4. Después de llamar a nuevaPartida()");
            }
        } else {
            System.out.println("Please enter user and password.");
        }
    }
    
    @FXML
    private void handleRegister() {
        System.out.println("Register pressed");
        // TODO: Implementar registro
    }
    
    @FXML
    private void handleNewGame() {
        System.out.println("New Game clicked");
        if (mainApp != null) {
            mainApp.nuevaPartida();
        }
    }
    
    @FXML
    private void handleSaveGame() {
        System.out.println("Save Game clicked");
        // TODO
    }
    
    @FXML
    private void handleLoadGame() {
        System.out.println("Load Game clicked");
        // TODO
    }
    
    @FXML
    private void handleQuitGame() {
        System.out.println("Quit Game clicked");
        System.exit(0);
    }
}