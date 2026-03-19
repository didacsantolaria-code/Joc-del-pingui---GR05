package JocDelPingui.model;

public class casillaInterrogante extends casilla {
    private evento evento;
    
    public casillaInterrogante(int posicion) {
        super(posicion, "❓ Casilla misteriosa");
        this.evento = new evento();
    }
    
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        partida.mostrarMensaje("❓ Casilla misteriosa...");
        evento.activarEvento(jugador, partida);
    }
}