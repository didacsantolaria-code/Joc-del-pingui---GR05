package JocDelPingui;

import java.util.ArrayList;
import java.util.List;

public class partida {
	  // Getters y Setters
    public tablero getTablero() { 
    	return tablero; 
    }
    
    public void setTablero(tablero tablero) { 
    	this.tablero = tablero; 
    }
    
    public ArrayList<jugador> getJugadores() { 
    	return jugadores; 
    }
    
    public void setJugadores(ArrayList<jugador> jugadores) { 
    	this.jugadores = jugadores; 
    }
    
    public int getTurnos() { 
    	return turnos; 
    }
    
    public void setTurnos(int turnos) { 
    	this.turnos = turnos; 
    }
    
    public int getJugadorActual() { 
    	return jugadorActual; 
    }
    
    public void setJugadorActual(int jugadorActual) { 
    	this.jugadorActual = jugadorActual; 
    }
    
    public boolean isFinalizada() { 
    	return finalizada; 
    }
    
    public void setFinalizada(boolean finalizada) { 
    	this.finalizada = finalizada; 
    }
    
    public jugador getGanador() { 
    	return ganador; 
    }
    
    public void setGanador(jugador ganador) { 
    	this.ganador = ganador; 
    }
    
    public String getIdPartida() { 
    	return idPartida; 
    }
    
    public void setIdPartida(String idPartida) { 
    	this.idPartida = idPartida; 
    }
    
    
    private tablero tablero;
    private ArrayList<jugador> jugadores;
    private int turnos;
    private int jugadorActual;
    private boolean finalizada;
    private jugador ganador;
    private String idPartida;
    
    public partida() {
        this.tablero = new tablero();
        this.jugadores = new ArrayList<>();
        this.turnos = 0;
        this.jugadorActual = 0;
        this.finalizada = false;
        this.idPartida = "PARTIDA_" + System.currentTimeMillis();
    }
    
    public void inicializarPartida(String nombreUsuario) {
        // Crear jugadores (mínimo 2)
        jugadores.add(new jugador(nombreUsuario, "Rojo"));
        jugadores.add(new jugador("Jugador 2", "Azul"));
        
        // Inicializar posiciones
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).setPosicion(0);
        }
        
        // Inicializar inventarios
        for (jugador j : jugadores) {
            j.getInventario().inicializarInventario();
        }
    }
    
    public void moverJugador(jugador jugador, int pasos) {
        int nuevaPosicion = jugador.getPosicion() + pasos;
        
        // Verificar si llegó al final
        if (nuevaPosicion >= tablero.getNumCasillas() - 1) {
            jugador.setPosicion(tablero.getNumCasillas() - 1);
            this.finalizada = true;
            this.ganador = jugador;
            return;
        }
        
        jugador.setPosicion(nuevaPosicion);
        
        // Aplicar efecto de la casilla
        casilla casilla = tablero.getCasilla(jugador.getPosicion());
        casilla.aplicarEfecto(jugador, this);
    }
    
    public void siguienteTurno() {
        turnos++;
        jugadorActual = (jugadorActual + 1) % jugadores.size();
    }
}