package JocDelPingui.model;

import java.util.Random;

// Genera eventos aleatorios cuando caes en una casilla interrogante
public class evento {
    private Random random; // para generar numeros al azar
    
    public evento() {
        this.random = new Random();
    }
    
    // Elige un evento al azar y lo aplica al jugador
    public void activarEvento(jugador jugador, partida partida) {
        int tipo = random.nextInt(100);
        
        if (tipo < 20) {
            // 20% de probabilidad: encuentras un pez
            ((pingino)jugador).getInventario().agregarPez();
            partida.mostrarMensaje("¡Has encontrado un pez!");
            
        } else if (tipo < 45) {
            // 25% de probabilidad: encuentras bolas de nieve
            int cantidad = 1 + random.nextInt(3);
            ((pingino)jugador).getInventario().agregarBolaNieve(cantidad);
            partida.mostrarMensaje("¡Has encontrado " + cantidad + " bola(s) de nieve!");
            
        } else if (tipo < 60) {
            // 15% de probabilidad: consigues un dado rapido (con suerte)
            if (random.nextInt(100) < 30) {
                ((pingino)jugador).getInventario().agregarDado("rapido");
                partida.mostrarMensaje("¡Has conseguido un DADO RÁPIDO! (5-10 casillas)");
            }
        } else if (tipo < 75) {
            // 15% de probabilidad: te dan un dado lento
            ((pingino)jugador).getInventario().agregarDado("lento");
            partida.mostrarMensaje("Has obtenido un dado lento (1-3 casillas)");
            
        } else if (tipo < 85) {
            // 10% de probabilidad: te resbalas y pierdes un turno
            jugador.setPierdeTurno(true);
            partida.mostrarMensaje("¡Te has resbalado! Pierdes un turno");
            
        } else {
            // 15% de probabilidad: pierdes un objeto del inventario
            ((pingino)jugador).getInventario().perderObjetoAleatorio();
            partida.mostrarMensaje("¡Has perdido un objeto de tu inventario!");
        }
    }
}