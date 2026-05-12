package JocDelPingui.model;

import java.util.Random;

// El dado del juego: se tira y da un numero al azar
public class dado {
    private Random random;           // generador de numeros al azar
    private int ultimoResultado;     // lo ultimo que salio al tirar
    private int tiradasRealizadas;   // cuantas veces se ha tirado
    private String tipo;             // tipo de dado: normal, rapido o lento
    
    // Crea un dado normal por defecto
    public dado() {
        this.random = new Random();
        this.ultimoResultado = 0;
        this.tiradasRealizadas = 0;
        this.tipo = "normal";
    }
    
    // Crea un dado del tipo que quieras (rapido, lento, normal)
    public dado(String tipo) {
        this();
        this.tipo = tipo;
    }
    
    // Tira el dado y devuelve el resultado segun el tipo
    public int tirar() {
        tiradasRealizadas++;
        
        switch (tipo) {
            case "rapido":
                // Dado rapido: sale entre 5 y 10
                ultimoResultado = 5 + random.nextInt(6);
                break;
            case "lento":
                // Dado lento: sale entre 1 y 3
                ultimoResultado = 1 + random.nextInt(3);
                break;
            default:
                // Dado normal: sale entre 1 y 6
                ultimoResultado = 1 + random.nextInt(6);
                break;
        }
        
        return ultimoResultado;
    }
    
    // Para obtener y cambiar los datos del dado
    public int getUltimoResultado() { return ultimoResultado; }
    public int getTiradasRealizadas() { return tiradasRealizadas; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}