package JocDelPingui.model;

public class inventario {
    private int dausRapidos;
    private int dausLentos;
    private int peces;
    private int bolasNieve;
    
    public inventario() {
        this.dausRapidos = 0;
        this.dausLentos = 0;
        this.peces = 0;
        this.bolasNieve = 0;
    }
    
    public void inicializarInventario() {
        this.dausRapidos = 1;
        this.dausLentos = 0;
        this.peces = 0;
        this.bolasNieve = 0;
    }
    
    public void agregarPez() {
        if (peces < 2) peces++;
    }
    
    public void usarPez() {
        if (peces > 0) peces--;
    }
    
    public void agregarBolaNieve(int cantidad) {
        bolasNieve = Math.min(bolasNieve + cantidad, 6);
    }
    
    public void usarBolaNieve() {
        if (bolasNieve > 0) bolasNieve--;
    }
    
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
    
    public int totalObjetos() {
        return dausRapidos + dausLentos + peces + bolasNieve;
    }
    
    public int getDaus() { return dausRapidos + dausLentos; }
    public int getDausRapidos() { return dausRapidos; }
    public int getDausLentos() { return dausLentos; }
    public void setDaus(int daus) { 
        this.dausRapidos = Math.min(daus, 3); 
        this.dausLentos = 0;
    }
    
    public int getPeces() { return peces; }
    public void setPeces(int peces) { this.peces = Math.min(peces, 2); }
    
    public int getBolasNieve() { return bolasNieve; }
    public void setBolasNieve(int bolasNieve) { this.bolasNieve = Math.min(bolasNieve, 6); }
    
    public String mostrar() {
        return "🎲=" + (dausRapidos + dausLentos) + " 🐟=" + peces + " ❄️=" + bolasNieve;
    }
}