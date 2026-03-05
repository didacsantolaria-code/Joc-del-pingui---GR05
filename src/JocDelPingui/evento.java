package JocDelPingui;

import java.util.Random;

public class evento {
	 // Getters y Setters
    public String getTipoEvento() { 
    	return tipoEvento; 
    }
    
    public void setTipoEvento(String tipoEvento) { 
    	this.tipoEvento = tipoEvento; 
    }
    
    public String getDescripcion() { 
    	return descripcion;
    }
    
    public void setDescripcion(String descripcion) { 
    	this.descripcion = descripcion; 
    }
    
    public int getEfecto() { 
    	return efecto; 
    }
    
    public void setEfecto(int efecto) { 
    	this.efecto = efecto; 
    }
    
    private String tipoEvento;
    private String descripcion;
    private int efecto;
    private Random random;
    
    public evento() {
        this.random = new Random();
    }
    
    public void activarEvento(jugador jugador, partida partida) {
        int tipo = random.nextInt(100);
        
        if (tipo < 20) { // 20% Obtener pez
            jugador.getInventario().agregarPez();
            System.out.println("¡Has encontrado un pez! 🐟");
            
        } else if (tipo < 45) { // 25% Obtener bolas de nieve (1-3)
            int cantidad = 1 + random.nextInt(3);
            jugador.getInventario().agregarBolaNieve(cantidad);
            System.out.println("¡Has encontrado " + cantidad + " bola(s) de nieve! ❄️");
            
        } else if (tipo < 60) { // 15% Obtener dado rápido (probabilidad baja)
            if (random.nextInt(100) < 30) { // 30% de probabilidad dentro de este 15%
                jugador.getInventario().agregarDado();
                dado dadoRapido = new dado("rapido");
                System.out.println("¡Has conseguido un DADO RÁPIDO! (5-10 casillas) ⚡");
            }
            
        } else if (tipo < 75) { // 15% Obtener dado lento (probabilidad alta)
            jugador.getInventario().agregarDado();
            dado dadoLento = new dado("lento");
            System.out.println("Has obtenido un dado lento (1-3 casillas)");
            
        } else if (tipo < 85) { // 10% Perder un turno
            jugador.setPierdeTurno(true);
            System.out.println("¡Te has resbalado! Pierdes un turno");
            
        } else { // 15% Perder un objeto aleatorio
            jugador.getInventario().perderObjetoAleatorio();
            System.out.println("¡Has perdido un objeto de tu inventario!");
        }
    }
}