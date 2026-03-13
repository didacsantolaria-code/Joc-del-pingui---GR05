package JocDelPingui.model;

public class pingino extends jugador {
    private inventario inventario;
    private dado dadoActual;
    
    public pingino(String nombre, String color) {
        super(nombre, color);
        this.inventario = new inventario();
        this.dadoActual = new dado("normal");
        this.inventario.agregarDado("normal");
    }
    
    @Override
    public void realizarAccion(partida partida) {
        // Acción específica del pingüino
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
    
    // Getters y Setters
    public inventario getInventario() { return inventario; }
    public void setInventario(inventario inventario) { this.inventario = inventario; }
    public dado getDadoActual() { return dadoActual; }
    public void setDadoActual(dado dadoActual) { this.dadoActual = dadoActual; }
}