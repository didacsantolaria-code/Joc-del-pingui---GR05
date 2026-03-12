package JocDelPingui.model;

public class pingino {
    private String nombre;
    private jugador jugadorAsociado;
    
    public pingino(String nombre) {
        this.nombre = nombre;
    }
    
    public void ajustarJugador(jugador j) {
        this.jugadorAsociado = j;
    }
    
    // Getters y setters (opcional)
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public jugador getJugadorAsociado() {
        return jugadorAsociado;
    }
    
    public void setJugadorAsociado(jugador jugadorAsociado) {
        this.jugadorAsociado = jugadorAsociado;
    }
}