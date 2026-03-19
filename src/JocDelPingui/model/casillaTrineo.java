package JocDelPingui.model;

public class casillaTrineo extends casilla {
    
    public casillaTrineo(int posicion) {
        super(posicion, "🛷 ¡Trineo! Avanzas.", "/JocDelPingui/images/trineo.png");
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        partida.mostrarMensaje("🛷 ¡Trineo! Avanzas al siguiente trineo.");
        int sigTrineo = buscarSiguienteTrineo(posicion);
        if (sigTrineo > posicion) {
            jugador.setPosicion(sigTrineo);
        }
    }
    
    private int buscarSiguienteTrineo(int posActual) {
        for (int i = posActual + 1; i < 50; i++) {
            if (i % 5 == 0) return i;
        }
        return posActual;
    }
}