package JocDelPingui.model;

import java.util.ArrayList;
import java.util.Random;

// El tablero del juego: tiene 50 casillas de diferentes tipos
public class tablero {
    private ArrayList<casilla> casillas; // la lista de todas las casillas
    private Random random;               // para generar casillas al azar
    private static final int NUM_CASILLAS = 50; // siempre hay 50 casillas
    
    // Al crear el tablero, genera las casillas automaticamente
    public tablero() {
        this.casillas = new ArrayList<>();
        this.random = new Random();
        generarCasillasAleatorias();
    }
    
    // Crea las 50 casillas al azar, la primera es la salida y la ultima es la meta
    private void generarCasillasAleatorias() {
        this.casillas = new ArrayList<>();
        this.random = new Random();   

        // La primera casilla siempre es la salida
        casillas.add(new casillaNormal(0, "Salida"));

        // Las casillas del medio se generan al azar
        for (int i = 1; i < NUM_CASILLAS - 1; i++) {
            casilla nuevaCasilla = null;
            boolean esValida = false;

            // Sigue intentando hasta que la casilla sea valida
            while (!esValida) {
                int tipo = random.nextInt(100);

                // Elige el tipo de casilla segun la probabilidad
                if (tipo < 10) {
                    nuevaCasilla = new casillaOso(i);           // 10% oso
                } else if (tipo < 25) {
                    nuevaCasilla = new casillaAgujero(i);       // 15% agujero
                } else if (tipo < 40) {
                    nuevaCasilla = new casillaTrineo(i);        // 15% trineo
                } else if (tipo < 55) {
                    nuevaCasilla = new casillaInterrogante(i);  // 15% interrogante
                } else if (tipo < 70) {
                    nuevaCasilla = new casillaTierraQuebradiza(i); // 15% tierra rota
                } else {
                    nuevaCasilla = new casillaNormal(i, "Normal"); // 30% normal
                }

                esValida = true;

                // No deja que haya dos agujeros o dos trineos seguidos
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
            }

            casillas.add(nuevaCasilla);
        }

        // La ultima casilla siempre es la meta
        casillas.add(new casillaNormal(NUM_CASILLAS - 1, "🏆 Meta"));
    }
    
    // Devuelve la casilla que hay en una posicion del tablero
    public casilla getCasilla(int posicion) {
        if (posicion >= 0 && posicion < casillas.size()) {
            return casillas.get(posicion);
        }
        return null;
    }
    
    // Devuelve cuantas casillas tiene el tablero y la lista entera
    public int getNumCasillas() { return casillas.size(); }
    public ArrayList<casilla> getCasillas() { return casillas; }
    
    // Recrea el tablero a partir de un texto guardado (para cargar partidas)
    public void inicializarDesdeString(String taulellStr) {
        this.casillas = new ArrayList<>();
        String[] tipusCaselles = taulellStr.split("\\|");
        
        // Lee cada tipo de casilla y la crea
        for (int i = 0; i < tipusCaselles.length; i++) {
            String tipus = tipusCaselles[i];
            if (!tipus.isEmpty()) {
                casilla nuevaCasilla;
                switch (tipus) {
                    case "casillaOso": 
                        nuevaCasilla = new casillaOso(i); 
                        break;
                    case "casillaAgujero": 
                        nuevaCasilla = new casillaAgujero(i); 
                        break;
                    case "casillaTrineo": 
                        nuevaCasilla = new casillaTrineo(i); 
                        break;
                    case "casillaInterrogante": 
                        nuevaCasilla = new casillaInterrogante(i); 
                        break;
                    case "casillaTierraQuebradiza": 
                        nuevaCasilla = new casillaTierraQuebradiza(i); 
                        break;
                    default: 
                        // Si no reconoce el tipo, la pone como normal
                        String desc = (i == 0) ? "Salida" : (i == tipusCaselles.length - 1) ? "🏆 Meta" : "Normal";
                        nuevaCasilla = new casillaNormal(i, desc); 
                        break;
                }
                casillas.add(nuevaCasilla);
            }
        }
    }
}