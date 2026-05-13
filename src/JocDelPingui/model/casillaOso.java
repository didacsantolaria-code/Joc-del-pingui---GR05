package JocDelPingui.model;

// Casilla de oso: si caes aqui el oso te ataca y vuelves al inicio (a no ser que tengas un pez)
public class casillaOso extends casilla {
    
    public casillaOso(int posicion) {
        super(posicion, "¡Un oso te ataca!");  
    }
    
    // Si tienes un pez, lo usas para sobornar al oso. Si no, vuelves a la casilla 0
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        if (jugador instanceof pingino) {
            pingino p = (pingino) jugador;
            if (!p.tieneSoborno()) {
                // No tienes pez, el oso te manda al inicio
                partida.mostrarMensaje("¡Un oso te ataca! Vuelves al inicio.");
                jugador.setPosicion(0);
            } else {
                // Usas un pez para que el oso te deje en paz
                partida.mostrarMensaje("Usas un pez para sobornar al oso.");
                p.usarPez();
            }
        } else {
            jugador.setPosicion(0);
        }
    }
}