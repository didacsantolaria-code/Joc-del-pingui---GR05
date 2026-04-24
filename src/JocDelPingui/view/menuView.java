package JocDelPingui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import JocDelPingui.mainApp;
import JocDelPingui.controller.gestionBBD;

public class menuView extends VBox {
    
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private StackPane stackPane;
    @FXML private Slider volumeSlider;
    
    private mainApp mainApp;
    private gestionBBD gestionBD;
    
    public menuView() {
    }
    
    @FXML
    private void initialize() {
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setPadding(new Insets(50));
        getStyleClass().add("menu-container");
        
        try {
            Image imagenFondo = new Image(getClass().getResourceAsStream("/JocDelPingui/view/fondo_login.png"));
            ImageView imageView = new ImageView(imagenFondo);
            imageView.setPreserveRatio(false);
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
            stackPane.getChildren().add(0, imageView);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
        }
    }
    
    public void setMainApp(mainApp mainApp) {
        this.mainApp = mainApp;
        if (volumeSlider != null) {
            volumeSlider.setValue(mainApp.getVolumenActual());
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                mainApp.setVolumen(newVal.doubleValue());
            });
        }
    }
    
    public void setGestionBD(gestionBBD gestionBD) {
        this.gestionBD = gestionBD;
    }
    
    @FXML
    private void handleSalirPrograma(ActionEvent event) {
        javafx.application.Platform.exit();
        System.exit(0);
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = userField.getText().trim();
        String password = passField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            mostrarMissatgeError("Error", "Camps buits", "Introdueix usuari i contrasenya.");
            return;
        }
        
        if (gestionBD == null || !gestionBD.isConnected()) {
            mostrarMissatgeError("Error de connexió", "No es pot connectar a la base de dades", 
                                "Comprova la connexió i torna a intentar.");
            return;
        }
        
        if (gestionBD.validarLogin(username, password)) {
            System.out.println("✅ Login correcte per a: " + username);
            if (mainApp != null) {
                mainApp.setUsuariActual(username);
                mainApp.mostrarSeleccion();
            }
        } else {
            mostrarMissatgeError("Error de login", "Usuari o contrasenya incorrectes", 
                                "Torna a intentar o registra't si no tens compte.");
        }
    }
    
    @FXML
    private void handleRegister() {
        String username = userField.getText().trim();
        String password = passField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            mostrarMissatgeError("Error", "Camps buits", "Introdueix usuari i contrasenya.");
            return;
        }
        
        if (password.length() < 3) {
            mostrarMissatgeError("Error", "Contrasenya massa curta", "La contrasenya ha de tenir almenys 3 caràcters.");
            return;
        }
        
        if (gestionBD == null || !gestionBD.isConnected()) {
            mostrarMissatgeError("Error de connexió", "No es pot connectar a la base de dades", 
                                "Comprova la connexió i torna a intentar.");
            return;
        }
        
        if (gestionBD.existeixJugador(username)) {
            mostrarMissatgeError("Error de registre", "L'usuari ja existeix", 
                                "El nom d'usuari '" + username + "' ja està registrat.");
            return;
        }
        
        if (gestionBD.registrarJugador(username, password)) {
            System.out.println("✅ Usuari registrat: " + username);
            mostrarMissatgeInfo("Registre correcte", "Usuari '" + username + "' registrat amb èxit!");
            userField.clear();
            passField.clear();
        } else {
            mostrarMissatgeError("Error de registre", "No s'ha pogut registrar", "Torna a intentar.");
        }
    }
    
    private void mostrarMissatgeInfo(String titol, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (this.getScene() != null && this.getScene().getWindow() != null) {
            alert.initOwner(this.getScene().getWindow());
        }
        alert.setTitle(titol);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        if (mainApp != null) mainApp.setPantallaCompleta();
    }
    
    private void mostrarMissatgeError(String titol, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (this.getScene() != null && this.getScene().getWindow() != null) {
            alert.initOwner(this.getScene().getWindow());
        }
        alert.setTitle(titol);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        if (mainApp != null) mainApp.setPantallaCompleta();
    }
}