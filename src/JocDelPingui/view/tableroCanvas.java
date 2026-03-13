package JocDelPingui.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import JocDelPingui.model.tablero;
import JocDelPingui.model.casilla;
import JocDelPingui.model.partida;
import JocDelPingui.model.jugador;
import java.util.ArrayList;

public class tableroCanvas extends Canvas {
    
    private tablero tablero;
    private partida partida;
    private double anchoCasilla;
    private double altoCasilla;
    
    public tableroCanvas(partida partida) {
        super(800, 400);
        this.partida = partida;
        this.tablero = partida.getTablero();
        
        widthProperty().addListener(e -> dibujar());
        heightProperty().addListener(e -> dibujar());
        
        dibujar();
    }
    
    private void dibujar() {
        double ancho = getWidth();
        double alto = getHeight();
        
        anchoCasilla = ancho / 10;
        altoCasilla = alto / 5;
        
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, ancho, alto);
        
        gc.setFill(Color.web("#e0f2f1"));
        gc.fillRect(0, 0, ancho, alto);
        
        gc.setStroke(Color.web("#b0bec5"));
        gc.setLineWidth(1);
        for (int i = 0; i <= 10; i++) {
            gc.strokeLine(i * anchoCasilla, 0, i * anchoCasilla, alto);
        }
        for (int i = 0; i <= 5; i++) {
            gc.strokeLine(0, i * altoCasilla, ancho, i * altoCasilla);
        }
        
        for (int i = 0; i < 50; i++) {
            int fila = i / 10;
            int columna = i % 10;
            
            double x = columna * anchoCasilla;
            double y = fila * altoCasilla;
            
            casilla c = tablero.getCasilla(i);
            dibujarCasilla(gc, c, x, y, anchoCasilla, altoCasilla, i);
        }
        
        dibujarFichas(gc);
    }
    
    private void dibujarCasilla(GraphicsContext gc, casilla c, double x, double y, double w, double h, int posicion) {
        switch (c.getTipo()) {
            case "OSO": gc.setFill(Color.web("#ffcdd2")); break;
            case "AGUJERO": gc.setFill(Color.web("#d1c4e9")); break;
            case "TRINEO": gc.setFill(Color.web("#fff9c4")); break;
            case "INTERROGANTE": gc.setFill(Color.web("#e1bee7")); break;
            case "TIERRA_QUEBRADIZA": gc.setFill(Color.web("#ffe0b2")); break;
            default: gc.setFill(Color.web("#ffffff"));
        }
        
        gc.fillRect(x + 1, y + 1, w - 2, h - 2);
        
        gc.setFill(Color.web("#546e7a"));
        gc.setFont(Font.font("Segoe UI", 10));
        gc.fillText(String.valueOf(posicion), x + 5, y + 15);
        
        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        gc.setFill(Color.web("#37474f"));
        
        String simbolo = "";
        switch (c.getTipo()) {
            case "OSO": simbolo = "🐻"; break;
            case "AGUJERO": simbolo = "⭕"; break;
            case "TRINEO": simbolo = "⛷️"; break;
            case "INTERROGANTE": simbolo = "?"; gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28)); break;
            case "TIERRA_QUEBRADIZA": simbolo = "⚠️"; break;
            default: simbolo = "·";
        }
        
        gc.fillText(simbolo, x + w/2 - 10, y + h/2 + 8);
    }
    
    private void dibujarFichas(GraphicsContext gc) {
        ArrayList<jugador> jugadores = partida.getJugadores();
        
        for (int i = 0; i < jugadores.size(); i++) {
            jugador j = jugadores.get(i);
            int pos = j.getPosicion();
            
            int fila = pos / 10;
            int columna = pos % 10;
            
            double x = columna * anchoCasilla + anchoCasilla / 2;
            double y = fila * altoCasilla + altoCasilla / 2;
            
            double offsetX = 0, offsetY = 0;
            switch (i) {
                case 0: offsetX = -12; offsetY = -12; break;
                case 1: offsetX = 12; offsetY = -12; break;
                case 2: offsetX = -12; offsetY = 12; break;
                case 3: offsetX = 12; offsetY = 12; break;
            }
            
            switch (j.getColor()) {
                case "Rojo": gc.setFill(Color.web("#ef5350")); break;
                case "Azul": gc.setFill(Color.web("#42a5f5")); break;
                case "Verde": gc.setFill(Color.web("#66bb6a")); break;
                default: gc.setFill(Color.web("#ffa726")); break;
            }
            
            gc.fillOval(x - 15 + offsetX, y - 15 + offsetY, 30, 30);
            
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeOval(x - 15 + offsetX, y - 15 + offsetY, 30, 30);
            
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
            gc.fillText(j.getNombre().substring(0, 1), x - 6 + offsetX, y + 6 + offsetY);
        }
    }
    
    public void actualizar() {
        dibujar();
    }
}