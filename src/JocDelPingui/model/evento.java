package JocDelPingui.model;

import java.util.Random;

public class evento {
    private Random random;
    
    public evento() {
        this.random = new Random();
    }
    
    public void activarEvento(jugador jugador, partida partida) {
        int tipo = random.nextInt(100);
        
        if (tipo < 20) {
            ((pingino)jugador).getInventario().agregarPez();
            partida.mostrarMensaje("¡Has encontrado un pez! 🐟");
            
        } else if (tipo < 45) {
            int cantidad = 1 + random.nextInt(3);
            ((pingino)jugador).getInventario().agregarBolaNieve(cantidad);
            partida.mostrarMensaje("¡Has encontrado " + cantidad + " bola(s) de nieve! ❄️");
            
        } else if (tipo < 60) {
            if (random.nextInt(100) < 30) {
                ((pingino)jugador).getInventario().agregarDado("rapido");
                partida.mostrarMensaje("¡Has conseguido un DADO RÁPIDO! (5-10 casillas) ⚡");
            }
        } else if (tipo < 75) {
            ((pingino)jugador).getInventario().agregarDado("lento");
            partida.mostrarMensaje("Has obtenido un dado lento (1-3 casillas)");
            
        } else if (tipo < 85) {
            jugador.setPierdeTurno(true);
            partida.mostrarMensaje("¡Te has resbalado! Pierdes un turno");
            
        } else {
            ((pingino)jugador).getInventario().perderObjetoAleatorio();
            partida.mostrarMensaje("¡Has perdido un objeto de tu inventario!");
        }
    }
}