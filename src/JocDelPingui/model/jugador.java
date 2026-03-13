package JocDelPingui.model;

public abstract class jugador {
    protected int posicion;
    protected String nombre;
    protected String color;
    protected boolean pierdeTurno;
    
    public jugador(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = 0;
        this.pierdeTurno = false;
    }
    
    public abstract void realizarAccion(partida partida);
    
    public void moverPosicion(int p) {
        this.posicion = p;
    }
    
    // Getters y Setters
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean isPierdeTurno() { return pierdeTurno; }
    public void setPierdeTurno(boolean pierdeTurno) { this.pierdeTurno = pierdeTurno; }
}