package JocDelPingui.model;

// La mochila del pinguino: guarda dados, peces y bolas de nieve
public class inventario {
    private int dausRapidos;   // cuantos dados rapidos tienes
    private int dausLentos;    // cuantos dados lentos tienes
    private int peces;         // cuantos peces tienes (para sobornar al oso)
    private int bolasNieve;    // cuantas bolas de nieve tienes (para atacar a otros)
    
    // Empieza con el inventario vacio
    public inventario() {
        this.dausRapidos = 0;
        this.dausLentos = 0;
        this.peces = 0;
        this.bolasNieve = 0;
    }
    
    // Pone el inventario inicial (1 dado rapido y nada mas)
    public void inicializarInventario() {
        this.dausRapidos = 1;
        this.dausLentos = 0;
        this.peces = 0;
        this.bolasNieve = 0;
    }
    
    // Añade un pez (maximo 2)
    public void agregarPez() {
        if (peces < 2) peces++;
    }
    
    // Usa un pez (si tienes alguno)
    public void usarPez() {
        if (peces > 0) peces--;
    }
    
    // Añade bolas de nieve (maximo 6 en total)
    public void agregarBolaNieve(int cantidad) {
        bolasNieve = Math.min(bolasNieve + cantidad, 6);
    }
    
    // Usa una bola de nieve
    public void usarBolaNieve() {
        if (bolasNieve > 0) bolasNieve--;
    }
    
    // Añade un dado al inventario (maximo 3 dados en total)
    public void agregarDado(String tipo) {
        if (tipo.equals("rapido") || tipo.equals("normal")) {
            if (dausRapidos + dausLentos < 3) {
                dausRapidos++;
            }
        } else if (tipo.equals("lento")) {
            if (dausRapidos + dausLentos < 3) {
                dausLentos++;
            }
        }
    }
    
    // Pierdes un objeto al azar del inventario
    public void perderObjetoAleatorio() {
        double random = Math.random();
        if (random < 0.33 && peces > 0) {
            peces--;
        } else if (random < 0.66 && bolasNieve > 0) {
            bolasNieve--;
        } else if (dausRapidos + dausLentos > 1) {
            if (dausRapidos > 0) {
                dausRapidos--;
            } else {
                dausLentos--;
            }
        }
    }
    
    // Cuenta cuantos objetos llevas en total
    public int totalObjetos() {
        return dausRapidos + dausLentos + peces + bolasNieve;
    }
    
    // Para obtener y cambiar los datos del inventario
    public int getDaus() { return dausRapidos + dausLentos; }
    public int getDausRapidos() { return dausRapidos; }
    public int getDausLentos() { return dausLentos; }
    public void setDaus(int daus) { 
        this.dausRapidos = Math.min(daus, 3); 
        this.dausLentos = 0;
    }
    
    public void setDausRapidos(int daus) { this.dausRapidos = daus; }
    public void setDausLentos(int daus) { this.dausLentos = daus; }
    
    public int getPeces() { return peces; }
    public void setPeces(int peces) { this.peces = Math.min(peces, 2); }
    
    public int getBolasNieve() { return bolasNieve; }
    public void setBolasNieve(int bolasNieve) { this.bolasNieve = Math.min(bolasNieve, 6); }
    
    // Muestra el inventario con emojis para que quede bonito
    public String mostrar() {
        return "🎲=" + (dausRapidos + dausLentos) + " 🐟=" + peces + " ❄️=" + bolasNieve;
    }
}