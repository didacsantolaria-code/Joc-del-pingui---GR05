package JocDelPingui.model;

import java.util.ArrayList;
import JocDelPingui.view.partidaView;

public class partida {
    private tablero tablero;
    private ArrayList<jugador> jugadores;
    private int turnos;
    private int jugadorActual;
    private boolean finalizada;
    private jugador ganador;
    private String idPartida;
    private static partidaView vistaActual;
    
    public partida() {
        this.tablero = new tablero();
        this.jugadores = new ArrayList<>();
        this.turnos = 0;
        this.jugadorActual = 0;
        this.finalizada = false;
        this.idPartida = "PARTIDA_" + System.currentTimeMillis();
    }
    
    public void inicializarPartida(String nombreUsuario) {
        jugadores.add(new pingino(nombreUsuario, "Azul"));
        jugadores.add(new pingino("Jugador 2", "Rojo"));
        jugadores.add(new pingino("Jugador 3", "Verde"));
        jugadores.add(new pingino("Jugador 4", "Amarillo"));
    }
    
    public void moverJugador(jugador jugador, int pasos) {
        int nuevaPosicion = jugador.getPosicion() + pasos;
        
        if (nuevaPosicion >= tablero.getNumCasillas() - 1) {
            jugador.setPosicion(tablero.getNumCasillas() - 1);
            this.finalizada = true;
            this.ganador = jugador;
            return;
        }
        
        jugador.setPosicion(nuevaPosicion);
        casilla casilla = tablero.getCasilla(jugador.getPosicion());
        casilla.realizarAccion(this, jugador);
    }
    
    public void siguienteTurno() {
        turnos++;
        jugadores.get(jugadorActual).setPierdeTurno(false);
        
        do {
            jugadorActual = (jugadorActual + 1) % jugadores.size();
        } while (jugadores.get(jugadorActual).isPierdeTurno());
    }
    
    public static void setVistaActual(partidaView vista) {
        vistaActual = vista;
    }
    
    public void mostrarMensaje(String mensaje) {
        if (vistaActual != null) {
            vistaActual.agregarMensaje(mensaje);
        }
        System.out.println(mensaje);
    }
    

    
    // Getters y Setters
    public tablero getTablero() {
    	return tablero;
    }
    public void setTablero(tablero tablero) { this.tablero = tablero; }
    public ArrayList<jugador> getJugadores() { return jugadores; }
    public void setJugadores(ArrayList<jugador> jugadores) { this.jugadores = jugadores; }
    public int getTurnos() { return turnos; }
    public void setTurnos(int turnos) { this.turnos = turnos; }
    public int getJugadorActual() { return jugadorActual; }
    public void setJugadorActual(int jugadorActual) { this.jugadorActual = jugadorActual; }
    public boolean isFinalizada() { return finalizada; }
    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }
    public jugador getGanador() { return ganador; }
    public void setGanador(jugador ganador) { this.ganador = ganador; }
    public String getIdPartida() { return idPartida; }
    public void setIdPartida(String idPartida) { this.idPartida = idPartida; }
}