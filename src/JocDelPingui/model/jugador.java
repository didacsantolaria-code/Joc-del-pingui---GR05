package JocDelPingui.model;

// Clase base de todos los jugadores (todos tienen nombre, color y posicion)
public abstract class jugador {
    protected int posicion;        // en que casilla esta
    protected String nombre;       // como se llama
    protected String color;        // de que color es su ficha
    protected boolean pierdeTurno; // si tiene que saltarse el proximo turno
    
    // Crea un jugador con su nombre y color, empezando en la casilla 0
    public jugador(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = 0;
        this.pierdeTurno = false;
    }
    
    // Cada tipo de jugador puede hacer cosas distintas en su turno
    public abstract void realizarAccion(partida partida);
    
    // Mueve al jugador a una posicion
    public void moverPosicion(int p) {
        this.posicion = p;
    }
    
    // Para obtener y cambiar los datos del jugador
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public boolean isPierdeTurno() { return pierdeTurno; }
    public void setPierdeTurno(boolean pierdeTurno) { this.pierdeTurno = pierdeTurno; }
}