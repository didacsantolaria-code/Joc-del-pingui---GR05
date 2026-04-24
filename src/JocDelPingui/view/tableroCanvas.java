package JocDelPingui.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import JocDelPingui.model.partida;
import JocDelPingui.model.jugador;
import JocDelPingui.model.casilla;
import java.util.ArrayList;

public class tableroCanvas extends Canvas {
    
    private partida partida;
    private double anchoCasilla;
    private double altoCasilla;
    
    public tableroCanvas(partida partida) {
        super(760, 360);
        this.partida = partida;
        
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
        
        
        gc.setStroke(Color.web("#b0e0e6"));
        gc.setLineWidth(2);
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
            
            casilla c = partida.getTablero().getCasilla(i);
            String icono = getIconoCasilla(c.getTipo());
            
            
            gc.setFill(Color.WHITE);
            gc.fillRect(x + 2, y + 2, anchoCasilla - 4, altoCasilla - 4);
            
            
            gc.setFill(Color.web("#7f8c8d"));
            gc.setFont(Font.font(10));
            gc.fillText(String.valueOf(i), x + 5, y + 15);
            
            
            gc.setFill(Color.web("#2c3e50"));
            gc.setFont(Font.font(24));
            gc.fillText(icono, x + anchoCasilla/2 - 12, y + altoCasilla/2 + 8);
        }
        
        
        dibujarPinguinos(gc);
    }
    
    private String getIconoCasilla(String tipo) {
        return switch(tipo) {
            case "casillaOso" -> "🐻";
            case "casillaAgujero" -> "🕳️";
            case "casillaTrineo" -> "⛷️";
            case "casillaInterrogante" -> "❓";
            case "casillaTierraQuebradiza" -> "⚠️";
            default -> "❄️";
        };
    }
    
    private void dibujarPinguinos(GraphicsContext gc) {
        ArrayList<jugador> jugadores = partida.getJugadores();
        
        for (int i = 0; i < jugadores.size(); i++) {
            jugador j = jugadores.get(i);
            int pos = j.getPosicion();
            int fila = pos / 10;
            int columna = pos % 10;
            
            double x = columna * anchoCasilla + anchoCasilla / 2;
            double y = fila * altoCasilla + altoCasilla / 2;
            
            
            double offsetX = i == 0 ? -12 : i == 1 ? 12 : i == 2 ? -12 : 12;
            double offsetY = i < 2 ? -12 : 12;
            
            
            String color = switch(j.getColor()) {
                case "Rojo" -> "#e74c3c";
                case "Azul" -> "#3498db";
                case "Verde" -> "#2ecc71";
                default -> "#f1c40f";
            };
            
            
            gc.setFill(Color.web(color));
            gc.fillOval(x - 15 + offsetX, y - 15 + offsetY, 30, 30);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeOval(x - 15 + offsetX, y - 15 + offsetY, 30, 30);
            
            
            gc.setFill(Color.WHITE);
            gc.fillOval(x - 8 + offsetX, y - 8 + offsetY, 6, 6);
            gc.fillOval(x + 2 + offsetX, y - 8 + offsetY, 6, 6);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 6 + offsetX, y - 6 + offsetY, 3, 3);
            gc.fillOval(x + 4 + offsetX, y - 6 + offsetY, 3, 3);
            
            
            gc.setFill(Color.ORANGE);
            double[] xPoints = {x + offsetX, x + 5 + offsetX, x - 5 + offsetX};
            double[] yPoints = {y + 2 + offsetY, y + 8 + offsetY, y + 8 + offsetY};
            gc.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    public void actualizar() {
        dibujar();
    }
}