package JocDelPingui;

import java.util.Random;

public class dado {
    private Random random;
    private int ultimoResultado;
    private int tiradasRealizadas;
    
    // Constructor
    public dado() {
        this.random = new Random();
        this.ultimoResultado = 0;
        this.tiradasRealizadas = 0;
    }
    
    // GETTERS
    public int getUltimoResultado() {
        return ultimoResultado;
    }
    
    public int getTiradasRealizadas() {
        return tiradasRealizadas;
    }
}