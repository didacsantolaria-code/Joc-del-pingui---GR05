package JocDelPingui.model;

public class casillaTierraQuebradiza extends casilla {
    
    public casillaTierraQuebradiza(int posicion) {
        super(posicion, "⚠️ ¡Tierra quebradiza!", "/JocDelPingui/images/tierra.png");
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        if (jugador instanceof pingino) {
            pingino p = (pingino) jugador;
            int totalObjetos = p.getInventario().totalObjetos();
            
            if (totalObjetos > 5) {
                partida.mostrarMensaje("¡Llevas demasiados objetos! La tierra se rompe y caes al inicio.");
                jugador.setPosicion(0);
            } else if (totalObjetos > 0) {
                partida.mostrarMensaje("La tierra tiembla bajo tus pies... ¡Pierdes un turno!");
                jugador.setPierdeTurno(true);
            } else {
                partida.mostrarMensaje("La tierra cruje pero aguanta tu peso.");
            }
        }
    }
}