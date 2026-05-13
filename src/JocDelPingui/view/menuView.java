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

// Pantalla de login: aqui metes tu usuario y contraseña para entrar al juego
public class menuView extends VBox {
    
    // Elementos de la pantalla (campos de texto, botones, etc)
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private StackPane stackPane;
    @FXML private Slider volumeSlider;
    
    private mainApp mainApp;       // la app principal
    private gestionBBD gestionBD;  // conexion a la base de datos
    
    public menuView() {
    }
    
    // Se ejecuta al cargar la pantalla: pone la imagen de fondo y el estilo
    @FXML
    private void initialize() {
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setPadding(new Insets(50));
        getStyleClass().add("menu-container");
        
        // Carga la imagen de fondo del login
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
    
    // Conecta este menu con la app principal y configura el control de volumen
    public void setMainApp(mainApp mainApp) {
        this.mainApp = mainApp;
        if (volumeSlider != null) {
            volumeSlider.setValue(mainApp.getVolumenActual());
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                mainApp.setVolumen(newVal.doubleValue());
            });
        }
    }
    
    // Guarda la conexion a la base de datos
    public void setGestionBD(gestionBBD gestionBD) {
        this.gestionBD = gestionBD;
    }
    
    // Cuando pulsas el boton de salir, cierra toda la app
    @FXML
    private void handleSalirPrograma(ActionEvent event) {
        javafx.application.Platform.exit();
        System.exit(0);
    }
    
    // Cuando pulsas "Login": comprueba usuario y contraseña y si son correctos entra
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = userField.getText().trim();
        String password = passField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            mostrarMissatgeError("Error", "Camps buits", "Introdueix usuari i contrasenya.");
        } else if (gestionBD == null || !gestionBD.isConnected()) {
            mostrarMissatgeError("Error de connexió", "No es pot connectar a la base de dades",
                                "Comprova la connexió i torna a intentar.");
        } else if (gestionBD.validarLogin(username, password)) {
            // Login correcto: pasa a la pantalla de seleccion
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
    
    // Cuando pulsas "Registrar": crea una cuenta nueva si todo esta bien
    @FXML
    private void handleRegister() {
        String username = userField.getText().trim();
        String password = passField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            mostrarMissatgeError("Error", "Camps buits", "Introdueix usuari i contrasenya.");
        } else if (password.length() < 3) {
            mostrarMissatgeError("Error", "Contrasenya massa curta", "La contrasenya ha de tenir almenys 3 caràcters.");
        } else if (gestionBD == null || !gestionBD.isConnected()) {
            mostrarMissatgeError("Error de connexió", "No es pot connectar a la base de dades",
                                "Comprova la connexió i torna a intentar.");
        } else if (gestionBD.existeixJugador(username)) {
            mostrarMissatgeError("Error de registre", "L'usuari ja existeix",
                                "El nom d'usuari '" + username + "' ja està registrat.");
        } else if (gestionBD.registrarJugador(username, password)) {
            // Registro correcto
            System.out.println("✅ Usuari registrat: " + username);
            mostrarMissatgeInfo("Registre correcte", "Usuari '" + username + "' registrat amb èxit!");
            userField.clear();
            passField.clear();
        } else {
            mostrarMissatgeError("Error de registre", "No s'ha pogut registrar", "Torna a intentar.");
        }
    }
    
    // Muestra un aviso de informacion al usuario
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
    
    // Muestra un aviso de error al usuario
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