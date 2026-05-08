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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
import JocDelPingui.controller.gestionBBD;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;

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
    private ImageView dadoImagen1;
    @FXML
    private ImageView dadoImagen2;
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
    @FXML
    private HBox avatarContainer;
    @FXML
    private Label turnoNombreLabel;
    @FXML
    private Slider volumeSlider;
    

    private partida partida;
    private mainApp mainApp;
    private Random random = new Random();
    private ArrayList<StackPane> casillasGraficas = new ArrayList<>();
    private ArrayList<StackPane> fichasJugadores = new ArrayList<>();
    private gestionBBD gestionBD;
    private String usuariActual;

    @FXML
    private void initialize() {
        
        
        usarRapidoBtn.setOnAction(e -> handleUsarRapido());
        usarLentoBtn.setOnAction(e -> handleUsarLento());
        usarNieveBtn.setOnAction(e -> handleUsarNieve());

        
        dadoResultado.setText("5");

        dadoResultado.setVisible(false);
        
     
        cargarImagen(iconoRapido, "dado_rapido.png");
        cargarImagen(iconoLento, "dado_lento.png");
        cargarImagen(iconoPez, "pez.png");
        cargarImagen(iconoNieve, "bola_nieve.png");
        
     
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
            
            Text fallback = new Text("❓");
            fallback.setStyle("-fx-font-size: 16px;");
            
        }
    }
    
    private void cargarImagenObjeto(ImageView imageView, String nombreArchivo) {
        try {
            String ruta = "/JocDelPingui/images/" + nombreArchivo;
            Image img = new Image(getClass().getResourceAsStream(ruta));
            imageView.setImage(img);
        } catch (Exception e) {
            System.out.println("No se pudo cargar imagen objeto: " + nombreArchivo);
            
            Text fallback = new Text("?");
            fallback.setStyle("-fx-font-size: 16px;");
            
        }
    }

    public void setPartida(partida partida) {
        this.partida = partida;
        partida.setVistaActual(this);

        
        crearTablero();

        
        crearFichasJugadores();

        
        crearAvatares();

        
        actualizarInventarios();

        
        marcarJugadorActual();
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

    @FXML
    private void handleGuardarPartida() {
        if (gestionBD != null && partida != null && usuariActual != null) {
            boolean guardat = gestionBD.guardarPartida(partida, usuariActual);
            if (guardat) {
                agregarEvento("✅ Partida guardada correctament!");
                mostrarMissatgeInfo("Partida guardada", "La partida s'ha guardat a la base de dades.");
            } else {
                agregarEvento("❌ Error en guardar la partida");
                mostrarMissatgeError("Error", "No s'ha pogut guardar la partida", "Revisa la connexió a la base de dades.");
            }
        } else {
            agregarEvento("❌ No es pot guardar: falta connexió o partida");
        }
    }

    @FXML
    private void handleSalir() {
        ButtonType btnGuardar = new ButtonType("Guardar i sortir", ButtonBar.ButtonData.YES);
        ButtonType btnSortir = new ButtonType("Sortir sense guardar", ButtonBar.ButtonData.NO);
        ButtonType btnCancelar = new ButtonType("Cancel·lar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tableroGrid.getScene().getWindow());
        alert.setTitle("Sortir de la partida");
        alert.setHeaderText("Vols guardar la partida abans de sortir?");
        alert.setContentText("Si surts sense guardar, perdràs el progrés actual.");
        alert.getButtonTypes().setAll(btnGuardar, btnSortir, btnCancelar);

        Optional<ButtonType> resultat = alert.showAndWait();
        if (mainApp != null) mainApp.setPantallaCompleta();
        if (resultat.isPresent()) {
            if (resultat.get() == btnGuardar) {
                
                if (gestionBD != null && partida != null && usuariActual != null) {
                    gestionBD.guardarPartida(partida, usuariActual);
                    agregarEvento("💾 Partida guardada abans de sortir");
                }
                volverAlMenu();
            } else if (resultat.get() == btnSortir) {
                
                volverAlMenu();
            }
            
        }
    }
    
    private void mostrarMissatgeInfo(String titol, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(tableroGrid.getScene().getWindow());
        alert.setTitle(titol);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        if (mainApp != null) mainApp.setPantallaCompleta();
    }

    private void mostrarMissatgeError(String titol, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(tableroGrid.getScene().getWindow());
        alert.setTitle(titol);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        if (mainApp != null) mainApp.setPantallaCompleta();
    }

    private void volverAlMenu() {
        if (mainApp != null) {
            mainApp.mostrarSeleccion();
        } else {
            
            if (tableroGrid.getScene() != null) {
                tableroGrid.getScene().getWindow().hide();
            }
        }
    }

    private void crearAvatares() {
        if (avatarContainer != null) {
            avatarContainer.getChildren().clear();
            for (jugador j : partida.getJugadores()) {
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                vbox.setSpacing(2);
                
                
                String colorAvatar = j.getColor().toLowerCase();
                String rutaAvatar = "/JocDelPingui/images/pingu_" + colorAvatar + ".png";
                ImageView icono = new ImageView();
                try {
                    Image imgAvatar = new Image(getClass().getResourceAsStream(rutaAvatar));
                    icono.setImage(imgAvatar);
                } catch (Exception e) {
                    System.out.println("No se pudo cargar avatar: " + rutaAvatar);
                }
                icono.setFitWidth(45);
                icono.setFitHeight(45);
                icono.setPreserveRatio(true);
                
                Label nombre = new Label(j.getNombre().toUpperCase());
                String colorCSS = "badge-" + j.getColor().toLowerCase();
                nombre.getStyleClass().addAll("avatar-badge", colorCSS);
                
                vbox.getChildren().addAll(icono, nombre);
                avatarContainer.getChildren().add(vbox);
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
                
                casillaPane.getChildren().add(numero);
            } else {
                
                try {
                    String rutaImagen = c.getRutaImagen();
                    System.out.println("Cargando: " + rutaImagen);

                    Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));
                    ImageView imageView = new ImageView(imagen);
                    imageView.setFitWidth(30); 
                    imageView.setFitHeight(30);
                    imageView.setPreserveRatio(true);

                    
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

            
            String colorImagen = j.getColor().toLowerCase();
            String rutaImagen = "/JocDelPingui/images/pingu_" + colorImagen + ".png";
            try {
                Image imgPingu = new Image(getClass().getResourceAsStream(rutaImagen));
                ImageView pinguView = new ImageView(imgPingu);
                pinguView.setPreserveRatio(true);
                ficha.getChildren().add(pinguView);
            } catch (Exception e) {
                System.out.println("No se pudo cargar imagen: " + rutaImagen);
                Text texto = new Text("🐧");
                ficha.getChildren().add(texto);
            }

            fichasJugadores.add(ficha);
        }
        
        recolocarFichas();
    }

    private void ajustarTamañoFicha(StackPane ficha, double size) {
        ficha.setPrefSize(size, size);
        ficha.setMaxSize(size, size);
        
        if (!ficha.getChildren().isEmpty()) {
            if (ficha.getChildren().get(0) instanceof ImageView) {
                ImageView iv = (ImageView) ficha.getChildren().get(0);
                iv.setFitWidth(size);
                iv.setFitHeight(size);
            } else if (ficha.getChildren().get(0) instanceof Text) {
                Text t = (Text) ficha.getChildren().get(0);
                t.setStyle("-fx-font-size: " + (int)(size * 0.7) + "px;");
            }
        }
    }

    private void recolocarFichas() {
        
        for (StackPane ficha : fichasJugadores) {
            for (StackPane casilla : casillasGraficas) {
                casilla.getChildren().remove(ficha);
            }
        }

        
        for (int pos = 0; pos < casillasGraficas.size(); pos++) {
            ArrayList<Integer> jugadoresEnPos = new ArrayList<>();
            for (int i = 0; i < partida.getJugadores().size(); i++) {
                if (partida.getJugadores().get(i).getPosicion() == pos) {
                    jugadoresEnPos.add(i);
                }
            }

            if (!jugadoresEnPos.isEmpty()) {
                StackPane casilla = casillasGraficas.get(pos);
                int count = jugadoresEnPos.size();

                
                double size;
                double[][] offsets;
                if (count == 1) {
                    size = 35;
                    offsets = new double[][]{{0, 6}};
                } else if (count == 2) {
                    size = 24;
                    offsets = new double[][]{{-10, 6}, {10, 6}};
                } else if (count == 3) {
                    size = 22;
                    offsets = new double[][]{{-10, 2}, {10, 2}, {0, 16}};
                } else {
                    size = 20;
                    offsets = new double[][]{{-10, 2}, {10, 2}, {-10, 16}, {10, 16}};
                }

                for (int k = 0; k < jugadoresEnPos.size(); k++) {
                    int idx = jugadoresEnPos.get(k);
                    StackPane ficha = fichasJugadores.get(idx);
                    ajustarTamañoFicha(ficha, size);
                    ficha.setTranslateX(offsets[k][0]);
                    ficha.setTranslateY(offsets[k][1]);
                    StackPane.setAlignment(ficha, Pos.CENTER);
                    casilla.getChildren().add(ficha);
                }
            }
        }
    }

    private void marcarJugadorActual() {
        if (partida != null && !partida.getJugadores().isEmpty()) {
            
            for (StackPane casilla : casillasGraficas) {
                casilla.getStyleClass().remove("turno-activo");
            }

            
            int idxActual = partida.getJugadorActual();
            if (idxActual >= 0 && idxActual < partida.getJugadores().size()) {
                jugador j = partida.getJugadores().get(idxActual);
                if (j.getPosicion() < casillasGraficas.size()) {
                    StackPane casillaActual = casillasGraficas.get(j.getPosicion());
                    casillaActual.getStyleClass().add("turno-activo");
                }
                
                if (turnoNombreLabel != null) {
                    turnoNombreLabel.setText(j.getNombre().toUpperCase());
                    
                    turnoNombreLabel.getStyleClass().removeAll("badge-rojo", "badge-azul", "badge-verde", "badge-amarillo");
                    String colorCSS = "badge-" + j.getColor().toLowerCase();
                    turnoNombreLabel.getStyleClass().add(colorCSS);
                }
            }
        }
    }

    private void moverFicha(int jugadorIdx, int nuevaPosicion) {
        
        recolocarFichas();
    }

    @FXML
    private void handleTirarDado() {
        if (partida != null && !partida.isFinalizada()) {
            int idxActual = partida.getJugadorActual();
            jugador jugadorActual = partida.getJugadores().get(idxActual);
            pingino p = (pingino) jugadorActual;

            int posAnterior = jugadorActual.getPosicion();
            int resultado = p.getDadoActual().tirar();
            boolean esRapido = p.getDadoActual().getTipo().equals("rapido");

            dadoResultado.setText(String.valueOf(resultado));
            animarDado(resultado, esRapido);

            
            partida.moverJugador(jugadorActual, resultado);

            
            moverFicha(idxActual, jugadorActual.getPosicion());

            
            if (partida.isFinalizada()) {
                agregarEvento("¡" + jugadorActual.getNombre() + " ha llegado a la meta!");
                mostrarVictoria(jugadorActual);
            } else {
                
                actualizarInventarios();
                partida.siguienteTurno();
                marcarJugadorActual();
            }
        }
    }

    private void mostrarVictoria(jugador ganador) {
        
        tirarDadoBtn.setDisable(true);
        usarRapidoBtn.setDisable(true);
        usarLentoBtn.setDisable(true);
        usarNieveBtn.setDisable(true);

        
        if (gestionBD != null && partida != null) {
            gestionBD.finalitzarPartida(partida);
            agregarEvento("Partida finalitzada! Estadístiques actualitzades.");
        }	

        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (tableroGrid != null && tableroGrid.getScene() != null) {
            alert.initOwner(tableroGrid.getScene().getWindow());
        }
        alert.setTitle("¡Fin de la partida!");
        alert.setHeaderText("¡" + ganador.getNombre() + " ha ganado!");
        alert.setContentText("¡Felicidades! " + ganador.getNombre() + " ha sido el primero en llegar a la meta.");
        alert.showAndWait();
        if (mainApp != null) mainApp.setPantallaCompleta();

        
        volverAlMenu();
    }

    private void animarDado(int resultado, boolean esRapido) {
        actualizarImagenDado(resultado, esRapido);

        
        dadoImagen1.setScaleX(1.0);
        dadoImagen1.setScaleY(1.0);

        FadeTransition ft1 = new FadeTransition(Duration.millis(400), dadoImagen1);
        ft1.setFromValue(0.4);
        ft1.setToValue(1.0);
        ft1.play();
        
        if (esRapido && dadoImagen2 != null) {
            dadoImagen2.setScaleX(1.0);
            dadoImagen2.setScaleY(1.0);
            FadeTransition ft2 = new FadeTransition(Duration.millis(400), dadoImagen2);
            ft2.setFromValue(0.4);
            ft2.setToValue(1.0);
            ft2.play();
        }
    }

    @FXML
    private void handleUsarRapido() {
        if (partida != null && !partida.isFinalizada()) {
            int idxActual = partida.getJugadorActual();
            pingino p = (pingino) partida.getJugadores().get(idxActual);

            if (p.getInventario().getDausRapidos() > 0) {
                p.setDadoActual(new dado("rapido"));
                agregarEvento("¡Dado rápido activado! (5-10 casillas)");
                actualizarInventarios();
            }
        }
    }

    @FXML
    private void handleUsarLento() {
        if (partida != null && !partida.isFinalizada()) {
            int idxActual = partida.getJugadorActual();
            pingino p = (pingino) partida.getJugadores().get(idxActual);

            if (p.getInventario().getDausLentos() > 0) {
                p.setDadoActual(new dado("lento"));
                agregarEvento("¡Dado lento activado! (1-3 casillas)");
                actualizarInventarios();
            }
        }
    }

    @FXML
    private void handleUsarNieve() {
        if (partida != null && !partida.isFinalizada()) {
            int idxActual = partida.getJugadorActual();
            pingino p = (pingino) partida.getJugadores().get(idxActual);

            if (p.getInventario().getBolasNieve() > 0) {
                
                int objetivo = (idxActual + 1) % partida.getJugadores().size();
                jugador objetivoJugador = partida.getJugadores().get(objetivo);

                p.usarBolaNieve(objetivoJugador);
                agregarEvento(p.getNombre() + " lanzó una bola de nieve a " +
                        objetivoJugador.getNombre());

                
                moverFicha(objetivo, objetivoJugador.getPosicion());

                actualizarInventarios();
            } else {
                agregarEvento("No tienes bolas de nieve");
            }
        }
    }

    private void actualizarInventarios() {
        if (partida != null && !partida.getJugadores().isEmpty()) {
            int idxActual = partida.getJugadorActual();
            if (idxActual >= 0 && idxActual < partida.getJugadores().size()) {
                pingino p = (pingino) partida.getJugadores().get(idxActual);

                rapidoCantidad.setText(String.valueOf(p.getInventario().getDausRapidos()));
                lentoCantidad.setText(String.valueOf(p.getInventario().getDausLentos()));
                pecesCantidad.setText(String.valueOf(p.getInventario().getPeces()));
                nieveCantidad.setText(String.valueOf(p.getInventario().getBolasNieve()));
            }
        }
    }

    private void agregarEvento(String mensaje) {
        HBox eventoBox = new HBox(8);
        eventoBox.setAlignment(Pos.TOP_LEFT);
        eventoBox.getStyleClass().add("evento-mensaje");

        
        ImageView imagenEvento = new ImageView();
        imagenEvento.setFitWidth(20);
        imagenEvento.setFitHeight(20);
        imagenEvento.setPreserveRatio(true);
        cargarImagenEvento(imagenEvento, mensaje);

        Text mensajeText = new Text(mensaje);
        mensajeText.getStyleClass().add("evento-texto");
        
        mensajeText.wrappingWidthProperty().bind(
            eventosLista.widthProperty().subtract(60)
        );

        eventoBox.getChildren().addAll(imagenEvento, mensajeText);
        eventosLista.getChildren().add(0, eventoBox);

        
        if (eventosLista.getChildren().size() > 15) {
            eventosLista.getChildren().remove(15, eventosLista.getChildren().size());
        }
    }
    
    private void cargarImagenEvento(ImageView imageView, String mensaje) {
        String nombreArchivo = obtenerNombreImagenEvento(mensaje);
        if (nombreArchivo != null) {
            try {
                String ruta = "/JocDelPingui/images/" + nombreArchivo;
                Image img = new Image(getClass().getResourceAsStream(ruta));
                imageView.setImage(img);
            } catch (Exception e) {
                
                System.out.println("No se pudo cargar imagen evento: " + nombreArchivo);
            }
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
        if (mensaje.contains("Pierdes un turno") || mensaje.contains("pierde el turno")) return "evento_turno.png";
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

    private void actualizarImagenDado(int resultado, boolean esRapido) {
        try {
            if (esRapido) {
                int d1 = Math.min(6, resultado - 1);
                int d2 = resultado - d1;
                
                String ruta1 = "/JocDelPingui/images/dado_" + d1 + ".png";
                String ruta2 = "/JocDelPingui/images/dado_" + d2 + ".png";
                
                if (dadoImagen1 != null) {
                    dadoImagen1.setImage(new Image(getClass().getResourceAsStream(ruta1)));
                }
                if (dadoImagen2 != null) {
                    dadoImagen2.setImage(new Image(getClass().getResourceAsStream(ruta2)));
                    dadoImagen2.setVisible(true);
                    dadoImagen2.setManaged(true);
                }
            } else {
                String ruta = "/JocDelPingui/images/dado_" + resultado + ".png";
                if (dadoImagen1 != null) {
                    dadoImagen1.setImage(new Image(getClass().getResourceAsStream(ruta)));
                }
                if (dadoImagen2 != null) {
                    dadoImagen2.setVisible(false);
                    dadoImagen2.setManaged(false);
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar imagen del dado.");
        }
    }
    
    public void setGestionBD(gestionBBD gestionBD) {
        this.gestionBD = gestionBD;
    }

    public void setUsuariActual(String usuariActual) {
        this.usuariActual = usuariActual;
    }
}