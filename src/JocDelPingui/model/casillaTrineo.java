package JocDelPingui.model;

public class casillaTrineo extends casilla {
    
    public casillaTrineo(int posicion) {
        super(posicion, "¡Trineo! Avanzas.");
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        partida.mostrarMensaje("¡Trineo! Avanzas al siguiente trineo.");
        int sigTrineo = buscarSiguienteTrineo(posicion, partida);
        if (sigTrineo > posicion) {
            jugador.setPosicion(sigTrineo);
        }
    }
    
    private int buscarSiguienteTrineo(int posActual, partida partida) {
        tablero tablero = partida.getTablero();
        for (int i = posActual + 1; i < tablero.getNumCasillas(); i++) {
            casilla c = tablero.getCasilla(i);
            if (c instanceof casillaTrineo) {
                return i;
            }
        }
        return posActual;
    }
}