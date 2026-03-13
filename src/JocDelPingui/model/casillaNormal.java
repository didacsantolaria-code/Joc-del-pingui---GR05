package JocDelPingui.model;

public class casillaNormal extends casilla {
    
    public casillaNormal(int posicion, String descripcion) {
        super(posicion, descripcion, "/JocDelPingui/images/normal.png");
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
    }
}