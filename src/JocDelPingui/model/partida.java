package JocDelPingui.model;

import java.util.ArrayList;
import JocDelPingui.view.partidaView;

// Esta clase controla toda la logica de una partida: turnos, movimientos, quien gana, etc.
public class partida {
    private tablero tablero;                // el tablero con todas las casillas
    private ArrayList<jugador> jugadores;   // la lista de jugadores
    private int turnos;                     // cuantos turnos se han jugado
    private int jugadorActual;              // a quien le toca jugar ahora
    private boolean finalizada;             // si la partida ya acabo
    private jugador ganador;                // quien gano
    private String idPartida;               // identificador unico de la partida
    private static partidaView vistaActual; // la pantalla que muestra el juego

    // Crea una partida nueva con el tablero vacio y sin jugadores
    public partida() {
        this.tablero = new tablero();
        this.jugadores = new ArrayList<>();
        this.turnos = 0;
        this.jugadorActual = 0;
        this.finalizada = false;
        this.idPartida = "PARTIDA_NEW";
    }

    // Crea los pinguinos a partir de la lista de nombres y colores
    public void inicializarPartida(ArrayList<String[]> jugadoresInfo) {
        for (String[] info : jugadoresInfo) {
            jugadores.add(new pingino(info[0], info[1]));
        }
    }

    // Mueve un jugador las casillas que le tocan y mira que pasa en la casilla donde cae
    public void moverJugador(jugador jugador, int pasos) {
        int nuevaPosicion = jugador.getPosicion() + pasos;

        if (nuevaPosicion >= tablero.getNumCasillas() - 1) {
            // Si llega al final o se pasa, ha ganado
            jugador.setPosicion(tablero.getNumCasillas() - 1);
            this.finalizada = true;
            this.ganador = jugador;
        } else {
            // Si no, cae en la casilla y se activa lo que toque
            jugador.setPosicion(nuevaPosicion);
            casilla casilla = tablero.getCasilla(jugador.getPosicion());
            casilla.realizarAccion(this, jugador);
        }
    }

    // Pasa al siguiente jugador. Si le toca perder turno, se lo salta
    public void siguienteTurno() {
        turnos++;

        boolean turnoEncontrado = false;
        while (!turnoEncontrado) {
            jugadorActual = (jugadorActual + 1) % jugadores.size();
            
            if (jugadores.get(jugadorActual).isPierdeTurno()) {
                // Este jugador pierde turno, se lo salta
                jugadores.get(jugadorActual).setPierdeTurno(false);
                mostrarMensaje(jugadores.get(jugadorActual).getNombre() + " no tira porque pierde el turno.");
            } else {
                turnoEncontrado = true;
            }
        }
    }

    // Conecta la partida con la pantalla grafica para mostrar mensajes
    public static void setVistaActual(partidaView vista) {
        vistaActual = vista;
    }

    // Muestra un mensaje en la pantalla y en la consola
    public void mostrarMensaje(String mensaje) {
        if (vistaActual != null) {
            vistaActual.agregarMensaje(mensaje);
        }
        System.out.println(mensaje);
    }

    // Para obtener y cambiar los datos de la partida
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
}