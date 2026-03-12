package JocDelPingui.controller;

import java.io.*;

import model1.partida;

public class guardarCargar {
    private String urlBBDD;
    private String username;
    private String password;
    private encriptacionUtil encriptador;
    
    public guardarCargar(String urlBBDD, String username, String password) {
        this.urlBBDD = urlBBDD;
        this.username = username;
        this.password = password;
        this.encriptador = new encriptacionUtil();
    }
    
    public boolean guardarPartida(partida partida, String rutaArchivo) {
        try {
            FileWriter writer = new FileWriter(rutaArchivo);
            writer.write("Partida guardada: " + partida.getIdPartida() + "\n");
            writer.write("Turnos: " + partida.getTurnos() + "\n");
            writer.write("Jugador actual: " + partida.getJugadorActual() + "\n");
            writer.close();
            
            System.out.println("✅ Partida guardada en: " + rutaArchivo);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error al guardar: " + e.getMessage());
            return false;
        }
    }
    
    public partida cargarPartida(String rutaArchivo) {
        System.out.println("Función de carga desactivada");
        return null;
    }
}