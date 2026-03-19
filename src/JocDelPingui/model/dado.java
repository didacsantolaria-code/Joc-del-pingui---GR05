package JocDelPingui.model;

import java.util.Random;

public class dado {
    private Random random;
    private int ultimoResultado;
    private int tiradasRealizadas;
    private String tipo;
    
    public dado() {
        this.random = new Random();
        this.ultimoResultado = 0;
        this.tiradasRealizadas = 0;
        this.tipo = "normal";
    }
    
    public dado(String tipo) {
        this();
        this.tipo = tipo;
    }
    
    public int tirar() {
        tiradasRealizadas++;
        
        switch (tipo) {
            case "rapido":
                ultimoResultado = 5 + random.nextInt(6);
                break;
            case "lento":
                ultimoResultado = 1 + random.nextInt(3);
                break;
            default:
                ultimoResultado = 1 + random.nextInt(6);
                break;
        }
        
        return ultimoResultado;
    }
    
    public int getUltimoResultado() { return ultimoResultado; }
    public int getTiradasRealizadas() { return tiradasRealizadas; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}