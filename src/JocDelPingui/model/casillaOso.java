package JocDelPingui.model;

public class casillaOso extends casilla {
    
    public casillaOso(int posicion) {
        super(posicion, "🐻 ¡Un oso te ataca!", "/JocDelPingui/images/oso.png");
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        if (jugador instanceof pingino) {
            pingino p = (pingino) jugador;
            if (!p.tieneSoborno()) {
                partida.mostrarMensaje("🐻 ¡Un oso te ataca! Vuelves al inicio.");
                jugador.setPosicion(0);
            } else {
                partida.mostrarMensaje("🐟 Usas un pez para sobornar al oso.");
                p.usarPez();
            }
        } else {
            jugador.setPosicion(0);
        }
    }
}