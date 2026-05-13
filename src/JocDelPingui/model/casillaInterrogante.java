package JocDelPingui.model;

// Casilla misteriosa: te pasa algo aleatorio (bueno o malo)
public class casillaInterrogante extends casilla {
    private evento evento; // el generador de eventos aleatorios
    
    public casillaInterrogante(int posicion) {
        super(posicion, "Casilla misteriosa");
        this.evento = new evento();
    }
    
    // Al caer aqui, se activa un evento al azar
    @Override
    public void realizarAccion(partida partida, jugador jugador) {
        partida.mostrarMensaje("Casilla misteriosa...");
        evento.activarEvento(jugador, partida);
    }
}