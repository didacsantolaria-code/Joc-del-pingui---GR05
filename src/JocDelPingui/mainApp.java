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
import java.lang.reflect.Method;
import java.net.URL;

// Esta es la clase principal que arranca toda la app
public class mainApp extends Application {
    
    private Stage primaryStage;       // la ventana principal
    private gestionBBD gestionBD;     // para conectar con la base de datos
    private String usuariActual;      // el usuario que ha iniciado sesion
    private Object mediaPlayer;       // el reproductor de musica de fondo
    private double volumenActual = 0.5; // volumen de la musica (de 0 a 1)
    
    // Esto se ejecuta al abrir la app: pone titulo, tamaño y abre el menu
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("El Joc del Pingüí");
        this.primaryStage.setMinWidth(1000);
        this.primaryStage.setMinHeight(700);
        
        setPantallaCompleta();
        
        // Conecta con la base de datos
        gestionBD = new gestionBBD();
        
        // Pone la musica de fondo
        inicializarMusica();
        
        // Muestra el menu de login
        mostrarMenu();
    }
    
    // Carga y reproduce la musica de fondo en un hilo aparte para no bloquear la app
    private void inicializarMusica() {
        new Thread(() -> {
            try {
                URL urlResource = getClass().getResource("/JocDelPingui/images/sonido_fondo.mp3");
                if (urlResource != null) {
                    String url = urlResource.toExternalForm();
                    
                    // Carga el reproductor de musica usando reflexion
                    Class<?> mediaClass = Class.forName("javafx.scene.media.Media");
                    Class<?> playerClass = Class.forName("javafx.scene.media.MediaPlayer");
                    
                    Object media = mediaClass.getConstructor(String.class).newInstance(url);
                    mediaPlayer = playerClass.getConstructor(mediaClass).newInstance(media);
                    
                    // Hace que la musica se repita para siempre
                    Method setCycle = playerClass.getMethod("setCycleCount", int.class);
                    setCycle.invoke(mediaPlayer, Integer.MAX_VALUE);
                    
                    // Pone el volumen
                    Method setVol = playerClass.getMethod("setVolume", double.class);
                    setVol.invoke(mediaPlayer, volumenActual);
                    
                    // Dale al play
                    Method play = playerClass.getMethod("play");
                    play.invoke(mediaPlayer);
                    
                    System.out.println("Música de fondo iniciada correctamente.");
                } else {
                    System.out.println("Archivo de música no encontrado.");
                }
            } catch (Exception e) {
                System.out.println("No se pudo iniciar la música de fondo: " + e.getMessage());
                mediaPlayer = null;
            }
        }, "MusicThread").start();
    }
    
    // Devuelve el reproductor de musica
    public Object getMediaPlayer() {
        return mediaPlayer;
    }
    
    // Devuelve el volumen actual
    public double getVolumenActual() {
        return volumenActual;
    }
    
    // Cambia el volumen de la musica
    public void setVolumen(double vol) {
        this.volumenActual = vol;
        if (mediaPlayer != null) {
            try {
                Method setVol = mediaPlayer.getClass().getMethod("setVolume", double.class);
                setVol.invoke(mediaPlayer, vol);
            } catch (Exception e) {
                System.out.println("No se pudo cambiar el volumen: " + e.getMessage());
            }
        }
    }
    
    // Carga y muestra la pantalla de login
    public void mostrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaMenu.fxml"));
            Parent root = loader.load();
            
            // Le pasa al menu la app principal y la conexion a la BD
            menuView controller = loader.getController();
            controller.setMainApp(this);
            controller.setGestionBD(gestionBD);
            
            cambiarEscena(root);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Carga y muestra la pantalla donde eliges jugadores y ves partidas guardadas
    public void mostrarSeleccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaSeleccion.fxml"));
            Parent root = loader.load();
            
            seleccionView controller = loader.getController();
            controller.setMainApp(this);
            controller.setGestionBD(gestionBD);
            controller.setUsuariActual(usuariActual);
            
            cambiarEscena(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Crea una partida nueva con los jugadores que le pases y abre la pantalla de juego
    public void nuevaPartida(ArrayList<String[]> jugadoresInfo) {
        try {
            // Crea la partida y mete los jugadores
            partida partida = new partida();
            partida.inicializarPartida(jugadoresInfo);
            
            // Carga la pantalla de juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            // Le pasa toda la info necesaria a la pantalla de juego
            partidaView controller = loader.getController();
            controller.setPartida(partida);
            controller.setMainApp(this);
            controller.setGestionBD(gestionBD);
            controller.setUsuariActual(usuariActual);
            
            cambiarEscena(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Carga una partida guardada desde la base de datos y la abre
    public void cargarPartida(int numPartida) {
        try {
            partida partidaCarregada = gestionBD.carregarPartidaCompleta(numPartida);
            
            // Solo la abre si tiene jugadores
            if (partidaCarregada != null && !partidaCarregada.getJugadores().isEmpty()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaJuego.fxml"));
                Parent root = loader.load();
                
                partidaView controller = loader.getController();
                controller.setPartida(partidaCarregada);
                controller.setMainApp(this);
                controller.setGestionBD(gestionBD);
                controller.setUsuariActual(usuariActual);
                
                cambiarEscena(root);
            } else if (partidaCarregada == null) {
                System.out.println("❌ No s'ha pogut carregar la partida " + numPartida);
            } else {
                System.out.println("❌ La partida " + numPartida + " no té jugadors.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Devuelve el nombre del usuario actual
    public String getUsuariActual() {
        return usuariActual;
    }
    
    // Cambia el usuario actual
    public void setUsuariActual(String usuariActual) {
        this.usuariActual = usuariActual;
    }
    
    // Pone la app en pantalla completa
    public void setPantallaCompleta() {
        if (primaryStage != null) {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
        }
    }
    
    // Cambia lo que se ve en la ventana (la escena)
    private void cambiarEscena(Parent root) {
        if (primaryStage.getScene() == null) {
            // Si no hay escena todavia, crea una nueva
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            setPantallaCompleta();
        } else {
            // Si ya hay una, solo cambia el contenido
            primaryStage.getScene().setRoot(root);
        }
    }
    
    // Cuando cierras la app, cierra la conexion a la base de datos
    @Override
    public void stop() {
        if (gestionBD != null) {
            gestionBD.tancar();
        }
    }
    
    // Punto de entrada de la app
    public static void main(String[] args) {
        launch(args);
    }
}