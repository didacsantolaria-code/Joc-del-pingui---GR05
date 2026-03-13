package JocDelPingui.model;

public class casillaAgujero extends casilla {
    
    public casillaAgujero(int posicion) {
        super(posicion, "🕳️ ¡Caes por un agujero!");
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        partida.mostrarMensaje("🕳️ ¡Caes por un agujero! Retrocedes.");
        int nuevaPos = buscarAgujeroAnterior(posicion);
        jugador.setPosicion(nuevaPos);
    }
    
    private int buscarAgujeroAnterior(int posActual) {
        for (int i = posActual - 1; i >= 0; i--) {
            if (i == 0) return 0;
            if (i % 7 == 0) return i;
        }
        return 0;
    }
}