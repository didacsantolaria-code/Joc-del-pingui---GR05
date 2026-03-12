package JocDelPingui;

public class jugador {
	 // Getters y Setters
    public int getPosicion() { 
    	return posicion; 
    }
    
    public void setPosicion(int posicion) { 
    	this.posicion = posicion; 
    }
    
    public String getNombre() { 
    	return nombre; 
    }
    
    public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }
    
    public String getColor() { 
    	return color; 
    }
    
    public void setColor(String color) { 
    	this.color = color; 
    }
    
    public inventario getInventario() { 
    	return inventario; 
    }
    
    public void setInventario(inventario inventario) { 
    	this.inventario = inventario; 
    }
    
    public dado getDado() { 
    	return dado; 
    }
    
    public void setDado(dado dado) { 
    	this.dado = dado; 
    }
    
    public boolean isPierdeTurno() { 
    	return pierdeTurno; 
    }
    
    public void setPierdeTurno(boolean pierdeTurno) { 
    	this.pierdeTurno = pierdeTurno; 
    }
    
    public String mostrarInventario() {
        return inventario.mostrar(); 
    }
    
    private int posicion;
    private String nombre;
    private String color;
    private inventario inventario;
    private dado dado;
    private boolean pierdeTurno;
    
    public jugador(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = 0;
        this.inventario = new inventario();
        this.dado = new dado();
        this.pierdeTurno = false;
    }
    
    public void moverPosicion(int p) {
        this.posicion = p;
    }
    
    public boolean tieneSoborno() {
        return inventario.getPeces() > 0;
    }
    
    public void usarPez() {
        inventario.usarPez();
    }
    
    public void usarBolaNieve(jugador objetivo) {
        if (inventario.getBolasNieve() > 0) {
            inventario.usarBolaNieve();
            objetivo.setPosicion(Math.max(0, objetivo.getPosicion() - 3));
        }
        
    }
}