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
    
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    
    private mainApp mainApp;
    
    public menuView() {
    }
    
    @FXML
    private void initialize() {
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setPadding(new Insets(50));
        getStyleClass().add("menu-container");
    }
    
    public void setMainApp(mainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = userField.getText();
        String password = passField.getText();
        
        if (!username.isEmpty() && !password.isEmpty()) {
            if (mainApp != null) {
                mainApp.nuevaPartida();
            }
        }
    }
    
    @FXML
    private void handleRegister() {
    }
    
    @FXML
    private void handleNewGame() {
        if (mainApp != null) {
            mainApp.nuevaPartida();
        }
    }
    
    @FXML
    private void handleSaveGame() {
    }
    
    @FXML
    private void handleLoadGame() {
    }
    
    @FXML
    private void handleQuitGame() {
        System.exit(0);
    }
}