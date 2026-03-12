package JocDelPingui.model;

import java.util.ArrayList;
import java.util.Random;

public class tablero {
    private ArrayList<casilla> casella; // Nom modificat
    private String nombre;
    private Random random;
    private static final int NUM_CASILLAS = 50;
    
    public tablero() {
        this.nombre = "Tablero Principal";
        this.casella = new ArrayList<>();
        this.random = new Random();
        generarTablero();
    }
    
    public tablero(String nombre) {
        this.nombre = nombre;
        this.casella = new ArrayList<>();
        this.random = new Random();
        generarTablero();
    }
    
    private void generarTablero() {
        for (int i = 0; i < NUM_CASILLAS; i++) {
            int tipo = random.nextInt(100);
            casilla casilla;
            
            if (i == 0 || i == NUM_CASILLAS - 1) {
                // Casillas especiales de inicio y fin
                casilla = new casilla(i, "NORMAL", "Casilla normal");
            } else if (tipo < 10) { // 10% Oso
                casilla = new casilla(i, "OSO", "¡Un oso te ataca! Vuelves al inicio");
            } else if (tipo < 25) { // 15% Forato
                casilla = new casilla(i, "AGUJERO", "¡Caes por un agujero! Retrocedes");
            } else if (tipo < 40) { // 15% Trineu
                casilla = new casilla(i, "TRINEO", "¡Trineo! Avanzas al siguiente trineo");
            } else if (tipo < 55) { // 15% Interrogant
                casilla = new casilla(i, "INTERROGANTE", "¿? Evento aleatorio");
            } else if (tipo < 70) { // 15% Terra Trencadís (nivel intermedio)
                casilla = new casilla(i, "TIERRA_QUEBRADIZA", "¡Tierra quebradiza!");
            } else { // 30% Normal
                casilla = new casilla(i, "NORMAL", "Casilla normal");
            }
            
            casella.add(casilla);
        }
    }
    
    public void actualizarTablero() {
        // Actualitze el tablero si es necesari
    }
    
    public casilla getCasilla(int posicion) {
        if (posicion >= 0 && posicion < casella.size()) {
            return casella.get(posicion);
        }
        return null;
    }
    
    public int getNumCasillas() {
        return casella.size();
    }
    
    // Getters y Setters
    public ArrayList<casilla> getcasella() {
    	return casella;
    }
    
    public void setcasella(ArrayList<casilla> casella) {
    	this.casella = casella;
    }
    
    public String getNombre() {
    	return nombre;
    }
    
    public void setNombre(String nombre) {
    	this.nombre = nombre;
    }
}