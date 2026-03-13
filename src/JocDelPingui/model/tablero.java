package JocDelPingui.model;

import java.util.ArrayList;
import java.util.Random;

public class tablero {
    private ArrayList<casilla> casillas;
    private Random random;
    private static final int NUM_CASILLAS = 50;
    
    public tablero() {
        this.casillas = new ArrayList<>();
        this.random = new Random();
        generarCasillasAleatorias();
    }
    
    private void generarCasillasAleatorias() {
        for (int i = 0; i < NUM_CASILLAS; i++) {
            int tipo = random.nextInt(100);
            
            if (i == 0) {
                casillas.add(new casillaNormal(i, "🏁 Salida"));
            } else if (i == NUM_CASILLAS - 1) {
                casillas.add(new casillaNormal(i, "🏆 Meta"));
            } else if (tipo < 10) {
                casillas.add(new casillaOso(i));
            } else if (tipo < 25) {
                casillas.add(new casillaAgujero(i));
            } else if (tipo < 40) {
                casillas.add(new casillaTrineo(i));
            } else if (tipo < 55) {
                casillas.add(new casillaInterrogante(i));
            } else if (tipo < 70) {
                casillas.add(new casillaTierraQuebradiza(i));
            } else {
                casillas.add(new casillaNormal(i, "⬜ Normal"));
            }
        }
    }
    
    public casilla getCasilla(int posicion) {
        if (posicion >= 0 && posicion < casillas.size()) {
            return casillas.get(posicion);
        }
        return null;
    }
    
    public int getNumCasillas() { return casillas.size(); }
    public ArrayList<casilla> getCasillas() { return casillas; }
}