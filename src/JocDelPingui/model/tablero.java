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
        this.casillas = new ArrayList<>();
        this.random = new Random();   // por si acaso

        // Casilla 0 - Salida (siempre normal)
        casillas.add(new casillaNormal(0, "Salida"));

        // Casillas 1 a 48 - Generación con control de adyacencia
        for (int i = 1; i < NUM_CASILLAS - 1; i++) {
            casilla nuevaCasilla;

            while (true) {  // re-intentamos hasta que sea válida
                int tipo = random.nextInt(100);

                if (tipo < 10) {
                    nuevaCasilla = new casillaOso(i);
                } else if (tipo < 25) {
                    nuevaCasilla = new casillaAgujero(i);
                } else if (tipo < 40) {
                    nuevaCasilla = new casillaTrineo(i);
                } else if (tipo < 55) {
                    nuevaCasilla = new casillaInterrogante(i);
                } else if (tipo < 70) {
                    nuevaCasilla = new casillaTierraQuebradiza(i);
                } else {
                    nuevaCasilla = new casillaNormal(i, "Normal");
                }

                // === VERIFICACIÓN DE REGLA ===
                boolean esValida = true;

                if (i > 0) {
                    casilla anterior = casillas.get(i - 1);
                    String claseAnterior = anterior.getClass().getSimpleName();
                    String claseActual = nuevaCasilla.getClass().getSimpleName();

                    if (claseActual.equals("casillaAgujero") && claseAnterior.equals("casillaAgujero")) {
                        esValida = false;
                    }
                    if (claseActual.equals("casillaTrineo") && claseAnterior.equals("casillaTrineo")) {
                        esValida = false;
                    }
                }

                if (esValida) {
                    break;   // ¡válido! salimos del while
                }
                // Si no es válida → volvemos a tirar el dado (re-roll)
            }

            casillas.add(nuevaCasilla);
        }

        // Casilla 49 - Meta (siempre normal)
        casillas.add(new casillaNormal(NUM_CASILLAS - 1, "🏆 Meta"));
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