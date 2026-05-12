package JocDelPingui.controller;

import JocDelPingui.model.jugador;
import JocDelPingui.model.tablero;

// Gestiona las acciones basicas de un jugador durante la partida
public class gestionJugador {

    // Registra un jugador por su nombre (solo muestra un mensaje)
    public void jugadorUsuario(String nombre) {
        System.out.println("Jugador " + nombre + " registrado");
    }
    
    // Marca que un jugador ha sido seleccionado para jugar su turno
    public void jugadorSeleccion(jugador j, int i, int pasos, tablero t) {
        System.out.println("Jugador " + j.getNombre() + " seleccionado");
    }
    
    // Marca que el turno de un jugador ha acabado
    public void jugadorFinalizarTurno(jugador j) {
        System.out.println("Turno de " + j.getNombre() + " finalizado");
    }
}