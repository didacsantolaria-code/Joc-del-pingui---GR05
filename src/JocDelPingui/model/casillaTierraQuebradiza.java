package JocDelPingui.model;

// Casilla de tierra quebradiza: si llevas muchos objetos, el suelo se rompe
public class casillaTierraQuebradiza extends casilla {
    
    public casillaTierraQuebradiza(int posicion) {
        super(posicion, "¡Tierra quebradiza!");
    }
    
    // Mira cuantos objetos llevas y decide que te pasa
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        if (jugador instanceof pingino) {
            pingino p = (pingino) jugador;
            int totalObjetos = p.getInventario().totalObjetos();
            
            if (totalObjetos > 5) {
                // Demasiados objetos: el suelo se rompe y caes al inicio
                partida.mostrarMensaje("¡Llevas demasiados objetos! La tierra se rompe y caes al inicio.");
                jugador.setPosicion(0);
            } else if (totalObjetos > 0) {
                // Algunos objetos: el suelo tiembla y pierdes un turno
                partida.mostrarMensaje("La tierra tiembla bajo tus pies... ¡Pierdes un turno!");
                jugador.setPierdeTurno(true);
            } else {
                // Sin objetos: no pasa nada
                partida.mostrarMensaje("La tierra cruje pero aguanta tu peso.");
            }
        }
    }
}