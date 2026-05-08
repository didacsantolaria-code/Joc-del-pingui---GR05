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

public class mainApp extends Application {
    
    private Stage primaryStage;
    private gestionBBD gestionBD;
    private String usuariActual;  
    private Object mediaPlayer; // javafx.scene.media.MediaPlayer via reflection
    private double volumenActual = 0.5;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("El Joc del Pingüí");
        this.primaryStage.setMinWidth(1000);
        this.primaryStage.setMinHeight(700);
        
        setPantallaCompleta();
        
        
        gestionBD = new gestionBBD();
        
        inicializarMusica();
        
        mostrarMenu();
    }
    
    private void inicializarMusica() {
        new Thread(() -> {
            try {
                URL urlResource = getClass().getResource("/JocDelPingui/images/sonido_fondo.mp3");
                if (urlResource != null) {
                    String url = urlResource.toExternalForm();
                    
                    Class<?> mediaClass = Class.forName("javafx.scene.media.Media");
                    Class<?> playerClass = Class.forName("javafx.scene.media.MediaPlayer");
                    
                    Object media = mediaClass.getConstructor(String.class).newInstance(url);
                    mediaPlayer = playerClass.getConstructor(mediaClass).newInstance(media);
                    
                    Method setCycle = playerClass.getMethod("setCycleCount", int.class);
                    setCycle.invoke(mediaPlayer, Integer.MAX_VALUE);
                    
                    Method setVol = playerClass.getMethod("setVolume", double.class);
                    setVol.invoke(mediaPlayer, volumenActual);
                    
                    Method play = playerClass.getMethod("play");
                    play.invoke(mediaPlayer);
                    
                    System.out.println("M\u00fasica de fondo iniciada correctamente.");
                } else {
                    System.out.println("Archivo de m\u00fasica no encontrado.");
                }
            } catch (Exception e) {
                System.out.println("No se pudo iniciar la m\u00fasica de fondo: " + e.getMessage());
                mediaPlayer = null;
            }
        }, "MusicThread").start();
    }
    
    public Object getMediaPlayer() {
        return mediaPlayer;
    }
    
    public double getVolumenActual() {
        return volumenActual;
    }
    
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
    
    public void mostrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JocDelPingui/view/PantallaMenu.fxml"));
            Parent root = loader.load();
            
            menuView controller = loader.getController();
            controller.setMainApp(this);
            controller.setGestionBD(gestionBD);
            
            cambiarEscena(root);
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
            
            cambiarEscena(root);
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
            
            cambiarEscena(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cargarPartida(int numPartida) {
        try {
            partida partidaCarregada = gestionBD.carregarPartidaCompleta(numPartida);
            
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
                System.out.println("\u274c No s'ha pogut carregar la partida " + numPartida);
            } else {
                System.out.println("\u274c La partida " + numPartida + " no t\u00e9 jugadors.");
            }
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
    
    private void cambiarEscena(Parent root) {
        if (primaryStage.getScene() == null) {
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            setPantallaCompleta();
        } else {
            primaryStage.getScene().setRoot(root);
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