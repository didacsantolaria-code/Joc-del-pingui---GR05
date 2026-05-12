package JocDelPingui.model;

// Casilla normal: no pasa nada especial cuando caes aqui
public class casillaNormal extends casilla {
    
    public casillaNormal(int posicion, String descripcion) {
        super(posicion, descripcion);
    }
    
    // No hace nada, es una casilla tranquila
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
    }
}