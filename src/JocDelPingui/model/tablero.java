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
        this.random = new Random();   

        
        casillas.add(new casillaNormal(0, "Salida"));

        
        for (int i = 1; i < NUM_CASILLAS - 1; i++) {
            casilla nuevaCasilla;

            while (true) {  
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
                    break;   
                }
                
            }

            casillas.add(nuevaCasilla);
        }

        
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
    
    public void inicializarDesdeString(String taulellStr) {
        this.casillas = new ArrayList<>();
        String[] tipusCaselles = taulellStr.split("\\|");
        
        for (int i = 0; i < tipusCaselles.length; i++) {
            String tipus = tipusCaselles[i];
            if (tipus.isEmpty()) continue;
            
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
                    String desc = (i == 0) ? "Salida" : (i == tipusCaselles.length - 1) ? "🏆 Meta" : "Normal";
                    nuevaCasilla = new casillaNormal(i, desc); 
                    break;
            }
            casillas.add(nuevaCasilla);
        }
    }
}