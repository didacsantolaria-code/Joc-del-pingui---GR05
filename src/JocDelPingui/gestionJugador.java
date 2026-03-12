package JocDelPingui;

public class gestionJugador {
    public void jugadorUsuario(String nombre) {
        System.out.println("Jugador " + nombre + " registrado");
    }
    
    public void jugadorSeleccion(jugador j, int i, int pasos, tablero t) {
        System.out.println("Jugador " + j.getNombre() + " seleccionado");
    }
    
    public void jugadorFinalizarTurno(jugador j) {
        System.out.println("Turno de " + j.getNombre() + " finalizado");
    }
}