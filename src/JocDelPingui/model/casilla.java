package JocDelPingui.model;

public abstract class casilla {
    protected int posicion;
    protected String descripcion;
    
    public casilla(int posicion, String descripcion) {
        this.posicion = posicion;
        this.descripcion = descripcion;
    }
    
    public abstract void realizarAccion(partida partida, jugador jugador);
    
    public int getPosicion() { return posicion; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return this.getClass().getSimpleName(); }
}