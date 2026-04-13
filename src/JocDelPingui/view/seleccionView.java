package JocDelPingui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import JocDelPingui.mainApp;
import JocDelPingui.controller.gestionBBD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class seleccionView {

    @FXML private StackPane stackPane;
    @FXML private Label lblNumJugadores;
    @FXML private Button btnMenos;
    @FXML private Button btnMas;

    @FXML private HBox filaJugador1;
    @FXML private HBox filaJugador2;
    @FXML private HBox filaJugador3;
    @FXML private HBox filaJugador4;

    @FXML private TextField nombreJ1;
    @FXML private TextField nombreJ2;
    @FXML private TextField nombreJ3;
    @FXML private TextField nombreJ4;

    @FXML private ComboBox<String> colorJ1;
    @FXML private ComboBox<String> colorJ2;
    @FXML private ComboBox<String> colorJ3;
    @FXML private ComboBox<String> colorJ4;

    @FXML private Label lblError;
    @FXML private Button btnJugar;

    @FXML private ListView<String> listaPartidas;
    @FXML private ListView<String> listaRanking;
    @FXML private Button btnContinuar;
    @FXML private Button btnVolver;

    private mainApp mainApp;
    private gestionBBD gestionBD;
    private String usuariActual;
    private int numJugadores = 2;

    private final ObservableList<String> coloresDisponibles =
            FXCollections.observableArrayList("Azul", "Rojo", "Verde", "Amarillo");

    @FXML
    private void initialize() {
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

        colorJ1.setItems(FXCollections.observableArrayList(coloresDisponibles));
        colorJ2.setItems(FXCollections.observableArrayList(coloresDisponibles));
        colorJ3.setItems(FXCollections.observableArrayList(coloresDisponibles));
        colorJ4.setItems(FXCollections.observableArrayList(coloresDisponibles));

        colorJ1.setValue("Azul");
        colorJ2.setValue("Rojo");
        colorJ3.setValue("Verde");
        colorJ4.setValue("Amarillo");

        actualizarFilas();

        btnContinuar.setDisable(true);
        listaPartidas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnContinuar.setDisable(newVal == null);
        });
    }

    public void setMainApp(mainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setGestionBD(gestionBBD gestionBD) {
        this.gestionBD = gestionBD;
    }

    public void setUsuariActual(String usuariActual) {
        this.usuariActual = usuariActual;
        carregarDadesUsuari();
    }

    private void carregarDadesUsuari() {
        if (gestionBD != null && usuariActual != null) {
            List<String> partides = gestionBD.obtenirPartidesPendents(usuariActual);
            ObservableList<String> partidesList = FXCollections.observableArrayList(partides);
            listaPartidas.setItems(partidesList);
            
            List<String> ranking = gestionBD.obtenirRanking();
            ObservableList<String> rankingList = FXCollections.observableArrayList(ranking);
            listaRanking.setItems(rankingList);
        } else {
            ObservableList<String> partidesDefault = FXCollections.observableArrayList("No hay partidas guardadas");
            listaPartidas.setItems(partidesDefault);
            
            ObservableList<String> rankingDefault = FXCollections.observableArrayList("🥇 Sin datos aún", "🥈 -", "🥉 -");
            listaRanking.setItems(rankingDefault);
        }
    }

    @FXML
    private void handleVolverLogin(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarMenu();
        }
    }

    @FXML
    private void handleContinuarPartida(ActionEvent event) {
        String seleccion = listaPartidas.getSelectionModel().getSelectedItem();
        if (seleccion != null && !seleccion.equals("No hay partidas guardadas")) {
            System.out.println("Continuar partida: " + seleccion);
        }
    }

    @FXML
    private void handleMenos(ActionEvent event) {
        if (numJugadores > 2) {
            numJugadores--;
            lblNumJugadores.setText(String.valueOf(numJugadores));
            actualizarFilas();
        }
    }

    @FXML
    private void handleMas(ActionEvent event) {
        if (numJugadores < 4) {
            numJugadores++;
            lblNumJugadores.setText(String.valueOf(numJugadores));
            actualizarFilas();
        }
    }

    private void actualizarFilas() {
        filaJugador1.setVisible(true);
        filaJugador1.setManaged(true);
        filaJugador2.setVisible(true);
        filaJugador2.setManaged(true);

        filaJugador3.setVisible(numJugadores >= 3);
        filaJugador3.setManaged(numJugadores >= 3);
        filaJugador4.setVisible(numJugadores >= 4);
        filaJugador4.setManaged(numJugadores >= 4);

        lblError.setText("");
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void handleJugar(ActionEvent event) {
        ArrayList<String[]> jugadoresInfo = new ArrayList<>();

        TextField[] nombres = {nombreJ1, nombreJ2, nombreJ3, nombreJ4};
        ComboBox<String>[] colores = new ComboBox[]{colorJ1, colorJ2, colorJ3, colorJ4};

        HashSet<String> coloresUsados = new HashSet<>();

        for (int i = 0; i < numJugadores; i++) {
            String nombre = nombres[i].getText().trim();
            String color = colores[i].getValue();

            if (nombre.isEmpty()) {
                lblError.setText("¡Todos los jugadores deben tener un nombre!");
                return;
            }

            if (color == null || color.isEmpty()) {
                lblError.setText("¡Todos los jugadores deben tener un color!");
                return;
            }

            if (coloresUsados.contains(color)) {
                lblError.setText("¡No se pueden repetir colores! (" + color + " está duplicado)");
                return;
            }

            coloresUsados.add(color);
            jugadoresInfo.add(new String[]{nombre, color});
        }

        lblError.setText("");
        
        // ==============================================
        // COMPROVACIÓ: Verificar que tots els jugadors existeixen a la BD
        // ==============================================
        if (gestionBD != null && gestionBD.isConnected()) {
            for (String[] jugador : jugadoresInfo) {
                String nickname = jugador[0];
                if (!gestionBD.existeixJugador(nickname)) {
                    mostrarMissatgeError("Jugador no registrat", 
                        "El jugador '" + nickname + "' no té una compte creada.",
                        "Has de registrar aquest usuari abans de començar la partida.\n\n" +
                        "Torna al menú principal i crea el compte '" + nickname + "'.");
                    return;
                }
            }
            
            // Verificar que el jugador actual (login) estigui a la llista
            boolean usuariActualTrobat = false;
            for (String[] jugador : jugadoresInfo) {
                if (jugador[0].equals(usuariActual)) {
                    usuariActualTrobat = true;
                    break;
                }
            }
            if (!usuariActualTrobat && usuariActual != null) {
                mostrarMissatgeError("Usuari no inclòs", 
                    "L'usuari '" + usuariActual + "' no està a la llista de jugadors.",
                    "Has d'incloure l'usuari amb el qual has iniciat sessió a la partida.");
                return;
            }
        }
        // ==============================================

        if (mainApp != null) {
            mainApp.nuevaPartida(jugadoresInfo);
        }
    }
    
    private void mostrarMissatgeError(String titol, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (stackPane != null && stackPane.getScene() != null) {
            alert.initOwner(stackPane.getScene().getWindow());
        }
        alert.setTitle(titol);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}