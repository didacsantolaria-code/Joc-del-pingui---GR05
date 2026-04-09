package JocDelPingui.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import JocDelPingui.mainApp;
import JocDelPingui.model.partida;
import JocDelPingui.model.pingino;
import JocDelPingui.model.jugador;
import JocDelPingui.model.casilla;
import JocDelPingui.model.dado;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class partidaView {

    @FXML
    private GridPane tableroGrid;
    @FXML
    private Label dadoResultado;
    @FXML
    private Button tirarDadoBtn;
    @FXML
    private Button usarRapidoBtn;
    @FXML
    private Button usarLentoBtn;
    @FXML
    private Button usarPezBtn;
    @FXML
    private Button usarNieveBtn;
    @FXML
    private Label rapidoCantidad;
    @FXML
    private Label lentoCantidad;
    @FXML
    private Label pecesCantidad;
    @FXML
    private Label nieveCantidad;
    @FXML
    private VBox eventosLista;
    @FXML
    private ImageView dadoImagen;
    @FXML 
    private ImageView iconoRapido;
    @FXML 
    private ImageView iconoLento;
    @FXML 
    private ImageView iconoPez;
    @FXML 
    private ImageView iconoNieve;
    @FXML
    private Button guardarBtn;
    @FXML
    private Button salirBtn;
    

    private partida partida;
    private mainApp mainApp;
    private Random random = new Random();
    private ArrayList<StackPane> casillasGraficas = new ArrayList<>();
    private ArrayList<StackPane> fichasJugadores = new ArrayList<>();

    @FXML
    private void initialize() {
        // Configurar botones
        // tirarDadoBtn.setOnAction(e -> handleTirarDado());
        usarRapidoBtn.setOnAction(e -> handleUsarRapido());
        usarLentoBtn.setOnAction(e -> handleUsarLento());
        usarPezBtn.setOnAction(e -> handleUsarPez());
        usarNieveBtn.setOnAction(e -> handleUsarNieve());

        // Inicializar con valores de ejemplo
        dadoResultado.setText("5");

        dadoResultado.setVisible(false);
        
     // Cargar imágenes de objetos
        cargarImagen(iconoRapido, "dado_rapido.png");
        cargarImagen(iconoLento, "dado_lento.png");
        cargarImagen(iconoPez, "pez.png");
        cargarImagen(iconoNieve, "bola_nieve.png");
        
     // Cargar imágenes de los objetos del panel lateral
        cargarImagenObjeto(iconoRapido, "dado_rapido.png");
        cargarImagenObjeto(iconoLento, "dado_lento.png");
        cargarImagenObjeto(iconoPez, "pez.png");
        cargarImagenObjeto(iconoNieve, "bola_nieve.png");
    }
    
    private void cargarImagen(ImageView imageView, String nombreArchivo) {
        try {
            String ruta = "/JocDelPingui/images/" + nombreArchivo;
            Image imagen = new Image(getClass().getResourceAsStream(ruta));
            imageView.setImage(imagen);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + nombreArchivo);
            // Opcional: mostrar un emoji como fallback
            Text fallback = new Text("❓");
            fallback.setStyle("-fx-font-size: 16px;");
            // No es trivial reemplazar el ImageView por Text, así que mejor dejamos el ImageView vacío
        }
    }
    
    private void cargarImagenObjeto(ImageView imageView, String nombreArchivo) {
        try {
            String ruta = "/JocDelPingui/images/" + nombreArchivo;
            Image img = new Image(getClass().getResourceAsStream(ruta));
            imageView.setImage(img);
        } catch (Exception e) {
            System.out.println("No se pudo cargar imagen objeto: " + nombreArchivo);
            // Opcional: poner un emoji de fallback
            Text fallback = new Text("?");
            fallback.setStyle("-fx-font-size: 16px;");
            // No podemos añadir el Text al ImageView fácilmente, así que mejor dejar vacío
        }
    }

    public void setPartida(partida partida) {
        this.partida = partida;
        partida.setVistaActual(this);

        // Crear el tablero gráfico
        crearTablero();

        // Crear las fichas de los jugadores
        crearFichasJugadores();

        // Actualizar inventarios
        actualizarInventarios();

        // Marcar jugador actual
        marcarJugadorActual();
    }

    public void setMainApp(mainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleGuardarPartida() {
        // TODO: En el futuro, guardar en base de datos
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(tableroGrid.getScene().getWindow());
        alert.setTitle("Guardar Partida");
        alert.setHeaderText("Partida guardada");
        alert.setContentText("La partida se ha guardado correctamente.\n(Funcionalidad pendiente de conexión a BBDD)");
        alert.showAndWait();
        agregarEvento("💾 Partida guardada");
    }

    @FXML
    private void handleSalir() {
        ButtonType btnGuardar = new ButtonType("Guardar y Salir", ButtonBar.ButtonData.YES);
        ButtonType btnSalir = new ButtonType("Salir sin guardar", ButtonBar.ButtonData.NO);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tableroGrid.getScene().getWindow());
        alert.setTitle("Salir de la partida");
        alert.setHeaderText("¿Quieres guardar la partida antes de salir?");
        alert.setContentText("Si sales sin guardar, perderás el progreso actual.");
        alert.getButtonTypes().setAll(btnGuardar, btnSalir, btnCancelar);

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent()) {
            if (resultado.get() == btnGuardar) {
                // Guardar y salir
                handleGuardarPartida();
                volverAlMenu();
            } else if (resultado.get() == btnSalir) {
                // Salir sin guardar
                volverAlMenu();
            }
            // Si es Cancelar, no hacer nada
        }
    }

    private void volverAlMenu() {
        if (mainApp != null) {
            mainApp.mostrarSeleccion();
        } else {
            // Intentar cerrar la ventana si no hay mainApp
            if (tableroGrid.getScene() != null) {
                tableroGrid.getScene().getWindow().hide();
            }
        }
    }

    private void crearTablero() {
        tableroGrid.getChildren().clear();
        casillasGraficas.clear();

        for (int i = 0; i < 50; i++) {
            int fila = i / 10;
            int columna = i % 10;

            casilla c = partida.getTablero().getCasilla(i);

            StackPane casillaPane = new StackPane();
            casillaPane.getStyleClass().add("casilla");
            casillaPane.setPrefSize(70, 70);

            Text numero = new Text(String.valueOf(i));
            numero.getStyleClass().add("casilla-numero");
            StackPane.setAlignment(numero, Pos.TOP_LEFT);
            StackPane.setMargin(numero, new javafx.geometry.Insets(2, 0, 0, 2));

            boolean esCasillaNormal = c.getTipo().equals("casillaNormal") && i != 0 && i != 49;

            if (esCasillaNormal) {
                // Casillas normales: solo el número, sin imagen
                casillaPane.getChildren().add(numero);
            } else {
                // Casillas especiales: imagen en la esquina superior derecha
                try {
                    String rutaImagen = c.getRutaImagen();
                    System.out.println("Cargando: " + rutaImagen);

                    Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));
                    ImageView imageView = new ImageView(imagen);
                    imageView.setFitWidth(30); // se reduce un poco el tamaño para que quepa bien
                    imageView.setFitHeight(30);
                    imageView.setPreserveRatio(true);

                    // Alinear la foto arriba a la derecha
                    StackPane.setAlignment(imageView, Pos.TOP_RIGHT);
                    StackPane.setMargin(imageView, new javafx.geometry.Insets(2, 2, 0, 0));

                    casillaPane.getChildren().addAll(numero, imageView);

                } catch (Exception e) {
                    System.out.println("Error con: " + c.getRutaImagen());
                    Text icono = new Text("❄️");
                    icono.setStyle("-fx-font-size: 20px;");
                    StackPane.setAlignment(icono, Pos.TOP_RIGHT);
                    StackPane.setMargin(icono, new javafx.geometry.Insets(2, 2, 0, 0));
                    casillaPane.getChildren().addAll(numero, icono);
                }
            }

            casillasGraficas.add(casillaPane);
            tableroGrid.add(casillaPane, columna, fila);
        }
    }

    private String obtenerClaseCasilla(String tipo) {
        switch (tipo) {
            case "casillaOso":
                return "casilla-oso";
            case "casillaAgujero":
                return "casilla-agujero";
            case "casillaTrineo":
                return "casilla-trineo";
            case "casillaInterrogante":
                return "casilla-interrogante";
            case "casillaTierraQuebradiza":
                return "casilla-hielo-roto";
            case "casillaNormal":
                if (descripcionTiene("Meta"))
                    return "casilla-meta";
                return "casilla-nieve";
            default:
                return "casilla-nieve";
        }
    }

    private boolean descripcionTiene(String texto) {
        // Método auxiliar para simplificar
        return false;
    }

    private String obtenerIconoCasilla(String tipo) {
        switch (tipo) {
            case "casillaOso":
                return "🐾";
            case "casillaAgujero":
                return "💧";
            case "casillaTrineo":
                return "🛷";
            case "casillaInterrogante":
                return "🎁";
            case "casillaTierraQuebradiza":
                return "⚠️";
            case "casillaNormal":
                return "❄️";
            default:
                return "❄️";
        }
    }

    private void crearFichasJugadores() {
        int numJugadores = partida.getJugadores().size();

        for (int i = 0; i < numJugadores; i++) {
            jugador j = partida.getJugadores().get(i);
            StackPane ficha = new StackPane();
            ficha.setPrefSize(40, 40);
            ficha.setMaxSize(40, 40);

            // Círculo de fondo según el color del jugador
            javafx.scene.shape.Circle circulo = new javafx.scene.shape.Circle(20);
            switch (j.getColor()) {
                case "Rojo":
                    circulo.setFill(javafx.scene.paint.Color.web("#E53935"));
                    break;
                case "Azul":
                    circulo.setFill(javafx.scene.paint.Color.web("#1E88E5"));
                    break;
                case "Verde":
                    circulo.setFill(javafx.scene.paint.Color.web("#43A047"));
                    break;
                case "Amarillo":
                    circulo.setFill(javafx.scene.paint.Color.web("#FDD835"));
                    break;
                default:
                    circulo.setFill(javafx.scene.paint.Color.web("#9E9E9E"));
                    break;
            }

            Text texto = new Text("🐧");
            texto.setStyle("-fx-font-size: 24px;");

            ficha.getChildren().addAll(circulo, texto);

            // Posición inicial (casilla 0)
            if (i < casillasGraficas.size()) {
                StackPane casilla = casillasGraficas.get(0);
                casilla.getChildren().add(ficha);
                StackPane.setAlignment(ficha, Pos.CENTER);
            }

            fichasJugadores.add(ficha);
        }
    }

    private void marcarJugadorActual() {
        // Quitar clase de todos
        for (StackPane casilla : casillasGraficas) {
            casilla.getStyleClass().remove("turno-activo");
        }

        // Añadir clase al jugador actual
        int idxActual = partida.getJugadorActual();
        jugador j = partida.getJugadores().get(idxActual);

        StackPane casillaActual = casillasGraficas.get(j.getPosicion());
        casillaActual.getStyleClass().add("turno-activo");
    }

    private void moverFicha(int jugadorIdx, int nuevaPosicion) {
        if (jugadorIdx < fichasJugadores.size()) {
            StackPane ficha = fichasJugadores.get(jugadorIdx);

            // Quitar ficha de su casilla actual
            for (StackPane casilla : casillasGraficas) {
                casilla.getChildren().remove(ficha);
            }

            // Añadir a nueva casilla
            if (nuevaPosicion < casillasGraficas.size()) {
                StackPane nuevaCasilla = casillasGraficas.get(nuevaPosicion);
                nuevaCasilla.getChildren().add(ficha);
                StackPane.setAlignment(ficha, Pos.CENTER);
            }
        }
    }

    @FXML
    private void handleTirarDado() {
        if (partida == null || partida.isFinalizada())
            return;

        int idxActual = partida.getJugadorActual();
        jugador jugadorActual = partida.getJugadores().get(idxActual);
        pingino p = (pingino) jugadorActual;

        int posAnterior = jugadorActual.getPosicion();
        int resultado = p.getDadoActual().tirar();

        dadoResultado.setText(String.valueOf(resultado));
        animarDado(resultado);

        // Mover jugador en el modelo
        partida.moverJugador(jugadorActual, resultado);

        // Animar la ficha del jugador en la vista
        moverFicha(idxActual, jugadorActual.getPosicion());

        // Comprobar si el jugador ha llegado a la meta
        if (partida.isFinalizada()) {
            agregarEvento("¡" + jugadorActual.getNombre() + " ha llegado a la meta!");
            mostrarVictoria(jugadorActual);
            return;
        }

        // Actualizar inventario, turno, etc.
        actualizarInventarios();
        partida.siguienteTurno();
        marcarJugadorActual();
    }

    private void mostrarVictoria(jugador ganador) {
        // Desactivar todos los botones de juego
        tirarDadoBtn.setDisable(true);
        usarRapidoBtn.setDisable(true);
        usarLentoBtn.setDisable(true);
        usarPezBtn.setDisable(true);
        usarNieveBtn.setDisable(true);

        // Mostrar alerta de victoria
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (tableroGrid != null && tableroGrid.getScene() != null) {
            alert.initOwner(tableroGrid.getScene().getWindow());
        }
        alert.setTitle("¡Fin de la partida!");
        alert.setHeaderText("¡" + ganador.getNombre() + " ha ganado!");
        alert.setContentText("¡Felicidades! " + ganador.getNombre() + " ha sido el primero en llegar a la meta.");
        alert.showAndWait();
    }

    private void animarDado(int resultado) {
        actualizarImagenDado(resultado);

        // Resetear escala por si había animación anterior
        dadoImagen.setScaleX(1.0);
        dadoImagen.setScaleY(1.0);

        FadeTransition ft = new FadeTransition(Duration.millis(400), dadoImagen);
        ft.setFromValue(0.4);
        ft.setToValue(1.0);
        ft.play();
    }

    @FXML
    private void handleUsarRapido() {
        if (partida == null || partida.isFinalizada()) return;
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);

        if (p.getInventario().getDausRapidos() > 0) {
            p.setDadoActual(new dado("rapido"));
            agregarEvento("¡Dado rápido activado! (5-10 casillas)");
            actualizarInventarios();
        }
    }

    @FXML
    private void handleUsarLento() {
        if (partida == null || partida.isFinalizada()) return;
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);

        if (p.getInventario().getDausLentos() > 0) {
            p.setDadoActual(new dado("lento"));
            agregarEvento("¡Dado lento activado! (1-3 casillas)");
            actualizarInventarios();
        }
    }

    @FXML
    private void handleUsarPez() {
        if (partida == null || partida.isFinalizada()) return;
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);

        if (p.getInventario().getPeces() > 0) {
            agregarEvento("Usaste un pez (tienes " + p.getInventario().getPeces() + ")");
        } else {
            agregarEvento("No tienes peces");
        }
    }

    @FXML
    private void handleUsarNieve() {
        if (partida == null || partida.isFinalizada()) return;
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);

        if (p.getInventario().getBolasNieve() > 0) {
            // Atacar al siguiente jugador
            int objetivo = (idxActual + 1) % partida.getJugadores().size();
            jugador objetivoJugador = partida.getJugadores().get(objetivo);

            p.usarBolaNieve(objetivoJugador);
            agregarEvento(p.getNombre() + " lanzó una bola de nieve a " +
                    objetivoJugador.getNombre());

            // Animar retroceso del objetivo
            moverFicha(objetivo, objetivoJugador.getPosicion());

            actualizarInventarios();
        } else {
            agregarEvento("No tienes bolas de nieve");
        }
    }

    private void actualizarInventarios() {
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);

        rapidoCantidad.setText(String.valueOf(p.getInventario().getDausRapidos()));
        lentoCantidad.setText(String.valueOf(p.getInventario().getDausLentos()));
        pecesCantidad.setText(String.valueOf(p.getInventario().getPeces()));
        nieveCantidad.setText(String.valueOf(p.getInventario().getBolasNieve()));
    }

    private void agregarEvento(String mensaje) {
        HBox eventoBox = new HBox(8);
        eventoBox.setAlignment(Pos.CENTER_LEFT);
        eventoBox.getStyleClass().add("evento-mensaje");

        // Cargar imagen según el mensaje
        ImageView imagenEvento = new ImageView();
        imagenEvento.setFitWidth(20);
        imagenEvento.setFitHeight(20);
        imagenEvento.setPreserveRatio(true);
        cargarImagenEvento(imagenEvento, mensaje);

        Text mensajeText = new Text(mensaje);
        mensajeText.getStyleClass().add("evento-texto");
        mensajeText.setWrappingWidth(200);

        eventoBox.getChildren().addAll(imagenEvento, mensajeText);
        eventosLista.getChildren().add(0, eventoBox);

        // Limitar a 10 eventos
        if (eventosLista.getChildren().size() > 10) {
            eventosLista.getChildren().remove(10, eventosLista.getChildren().size());
        }
    }
    
    private void cargarImagenEvento(ImageView imageView, String mensaje) {
        String nombreArchivo = obtenerNombreImagenEvento(mensaje);
        if (nombreArchivo == null) return;

        try {
            String ruta = "/JocDelPingui/images/" + nombreArchivo;
            Image img = new Image(getClass().getResourceAsStream(ruta));
            imageView.setImage(img);
        } catch (Exception e) {
            // Si no existe, no mostrar nada (o podrías mostrar un emoji de fallback)
            System.out.println("No se pudo cargar imagen evento: " + nombreArchivo);
        }
    }
    
    private String obtenerNombreImagenEvento(String mensaje) {
        if (mensaje.contains("Dado rápido activado") || mensaje.contains("DADO RÁPIDO"))
            return "dado_rapido.png";
        if (mensaje.contains("Dado lento activado") || mensaje.contains("dado lento"))
            return "dado_lento.png";
        if (mensaje.contains("lanzó una bola de nieve") || mensaje.contains("bola(s) de nieve"))
            return "bola_nieve.png";
        if (mensaje.contains("Usaste un pez") || mensaje.contains("pez"))
            return "pez.png";

        if (mensaje.contains("pez")) return "pez.png";
        if (mensaje.contains("bola(s) de nieve")) return "bola_nieve.png";
        if (mensaje.contains("DADO RÁPIDO")) return "dado_rapido.png";
        if (mensaje.contains("dado lento")) return "dado_lento.png";
        if (mensaje.contains("Pierdes un turno")) return "evento_perder_turno.png";
        if (mensaje.contains("perdido un objeto")) return "evento_perder_objeto.png";

        if (mensaje.contains("Casilla misteriosa") || mensaje.contains("❓"))
            return "casilla_interrogante.png";
        if (mensaje.contains("oso")) return "casilla_oso.png";
        if (mensaje.contains("Trineo")) return "casilla_trineo.png";
        if (mensaje.contains("agujero")) return "casilla_agujero.png";
        if (mensaje.contains("tierra") || mensaje.contains("Tierra")) return "casilla_tierrarota.png";

        if (mensaje.contains("Turno de")) return "evento_turno.png";
        if (mensaje.contains("ha llegado a la meta") || mensaje.contains("GANA"))
            return "casilla_meta.png";

        return null;
    }

    public void agregarMensaje(String mensaje) {
        agregarEvento(mensaje);
    }

    private void actualizarImagenDado(int resultado) {
        try {
            String ruta = "/JocDelPingui/images/dado_" + resultado + ".png";
            Image imagen = new Image(getClass().getResourceAsStream(ruta));
            if (dadoImagen != null) {
                dadoImagen.setImage(imagen);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar dado_" + resultado + ".png");
        }
    }
}