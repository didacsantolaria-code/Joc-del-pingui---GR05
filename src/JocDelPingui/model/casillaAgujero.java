package JocDelPingui.model;

public class casillaAgujero extends casilla {
    
    public casillaAgujero(int posicion) {
        super(posicion, "¡Caes por un agujero!");
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        partida.mostrarMensaje("¡Caes por un agujero! Retrocedes.");
        int nuevaPos = buscarAgujeroAnterior(posicion, partida);
        jugador.setPosicion(nuevaPos);
    }
    
    private int buscarAgujeroAnterior(int posActual, partida partida) {
        tablero tablero = partida.getTablero();
        for (int i = posActual - 1; i > 0; i--) {
            casilla c = tablero.getCasilla(i);
            if (c instanceof casillaAgujero) {
                return i;
            }
        }
        return 0;
    }
}