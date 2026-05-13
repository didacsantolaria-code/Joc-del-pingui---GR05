package JocDelPingui.model;

// El pinguino es el jugador del juego, tiene inventario y un dado
public class pingino extends jugador {
    private inventario inventario; // su mochila con objetos
    private dado dadoActual;       // el dado que esta usando ahora
    
    // Crea un pinguino con nombre, color, inventario vacio y un dado normal
    public pingino(String nombre, String color) {
        super(nombre, color);
        this.inventario = new inventario();
        this.dadoActual = new dado("normal");
        this.inventario.agregarDado("normal");
    }
    
    // No hace nada especial por ahora
    @Override
    public void realizarAccion(partida partida) {
    }
    
    // Mira si tiene algun pez para sobornar al oso
    public boolean tieneSoborno() {
        return inventario.getPeces() > 0;
    }
    
    // Usa un pez del inventario
    public void usarPez() {
        inventario.usarPez();
    }
    
    // Lanza una bola de nieve a otro jugador y lo hace retroceder 3 casillas
    public void usarBolaNieve(jugador objetivo) {
        if (inventario.getBolasNieve() > 0) {
            inventario.usarBolaNieve();
            objetivo.setPosicion(Math.max(0, objetivo.getPosicion() - 3));
        }
    }
    
    // Para obtener y cambiar el inventario y el dado
    public inventario getInventario() { return inventario; }
    public void setInventario(inventario inventario) { this.inventario = inventario; }
    public dado getDadoActual() { return dadoActual; }
    public void setDadoActual(dado dadoActual) { this.dadoActual = dadoActual; }
}