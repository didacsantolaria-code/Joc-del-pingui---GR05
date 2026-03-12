package JocDelPingui.model;

public class casilla {
	
	// Getters y Setters
    public int getPosicion() { 
    	return posicion; 
    }
    
    public void setPosicion(int posicion) { 
    	this.posicion = posicion; 
    }
    
    public String getTipo() { 
    	return tipo; 
    }
    
    public void setTipo(String tipo) { 
    	this.tipo = tipo;
    }
    
    public String getDescripcion() { 
    	return descripcion; 
    }
    
    public void setDescripcion(String descripcion) { 
    	this.descripcion = descripcion; 
    }
    
    public boolean isSoborno() { 
    	return soborno; 
    }
    
    public void setSoborno(boolean soborno) { 
    	this.soborno = soborno; 
    }
	
	
    private int posicion;
    private String tipo;
    private String descripcion;
    private boolean soborno; // para la foca (pingüino)
    
    public casilla(int posicion, String tipo, String descripcion) {
        this.posicion = posicion;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.soborno = false;
    }
    
    public void aplicarEfecto(jugador jugador, partida partida) {
        switch (tipo) {
            case "OSO":
                if (!jugador.tieneSoborno()) {
                    System.out.println(descripcion);
                    jugador.setPosicion(0);
                } else {
                    System.out.println("¡Usas un pez para sobornar al oso! No vuelves al inicio.");
                    jugador.usarPez();
                }
                break;
                
            case "AGUJERO":
                System.out.println(descripcion);
                int nuevaPos = buscarAgujeroAnterior(posicion);
                jugador.setPosicion(nuevaPos);
                break;
                
            case "TRINEO":
                System.out.println(descripcion);
                int sigTrineo = buscarSiguienteTrineo(posicion);
                if (sigTrineo > posicion) {
                    jugador.setPosicion(sigTrineo);
                }
                break;
                
            case "INTERROGANTE":
                System.out.println(descripcion);
                evento evento = new evento();
                evento.activarEvento(jugador, partida);
                break;
                
            case "TIERRA_QUEBRADIZA":
            	aplicarTierraQuebradiza(jugador, partida);
                break;
                
            default:
                // Casilla normal, no hace nada
                break;
        }
    }
    
    private int buscarAgujeroAnterior(int posActual) {
        for (int i = posActual - 1; i >= 0; i--) {
            if (i == 0) return 0;
            // Simulación: foratos en posiciones múltiplo de 7
            if (i % 7 == 0) return i;
        }
        return 0;
    }
    
    private int buscarSiguienteTrineo(int posActual) {
        for (int i = posActual + 1; i < 50; i++) {
            // Simulación: trineos en posiciones múltiplo de 5
            if (i % 5 == 0) return i;
        }
        return posActual;
    }
    
    private void aplicarTierraQuebradiza(jugador jugador, partida partida) {
        int totalObjetos = jugador.getInventario().totalObjetos();
        
        if (totalObjetos > 5) {
            System.out.println("¡Llevas demasiados objetos! La tierra se rompe y caes al inicio.");
            jugador.setPosicion(0);
        } else if (totalObjetos > 0) {
            System.out.println("La tierra tiembla bajo tus pies... ¡Pierdes un turno!");
            // El efecto de perder turno se maneja en GestionJugador
            jugador.setPierdeTurno(true);
        } else {
            System.out.println("La tierra cruje pero aguanta tu peso.");
        }
    }
}