package JocDelPingui.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import JocDelPingui.model.partida;
import JocDelPingui.model.jugador;
import JocDelPingui.model.casilla;
import java.util.ArrayList;

// Dibuja el tablero y los pinguinos en un lienzo (version alternativa al GridPane)
public class tableroCanvas extends Canvas {
    
    private partida partida;
    private double anchoCasilla;
    private double altoCasilla;
    
    // Crea el lienzo con la partida y lo dibuja
    public tableroCanvas(partida partida) {
        super(760, 360);
        this.partida = partida;
        
        // Se redibuja cuando cambia el tamaño
        widthProperty().addListener(e -> dibujar());
        heightProperty().addListener(e -> dibujar());
        
        dibujar();
    }
    
    // Dibuja todo el tablero: fondo, casillas y pinguinos
    private void dibujar() {
        double ancho = getWidth();
        double alto = getHeight();
        
        anchoCasilla = ancho / 10;
        altoCasilla = alto / 5;
        
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, ancho, alto);
        
        // Pinta el fondo de color clarito
        gc.setFill(Color.web("#e0f2f1"));
        gc.fillRect(0, 0, ancho, alto);
        
        // Dibuja las lineas de la cuadricula
        gc.setStroke(Color.web("#b0e0e6"));
        gc.setLineWidth(2);
        for (int i = 0; i <= 10; i++) {
            gc.strokeLine(i * anchoCasilla, 0, i * anchoCasilla, alto);
        }
        for (int i = 0; i <= 5; i++) {
            gc.strokeLine(0, i * altoCasilla, ancho, i * altoCasilla);
        }
        
        // Dibuja cada casilla con su numero y su icono
        for (int i = 0; i < 50; i++) {
            int fila = i / 10;
            int columna = i % 10;
            double x = columna * anchoCasilla;
            double y = fila * altoCasilla;
            
            casilla c = partida.getTablero().getCasilla(i);
            String icono = getIconoCasilla(c.getTipo());
            
            // Fondo blanco de la casilla
            gc.setFill(Color.WHITE);
            gc.fillRect(x + 2, y + 2, anchoCasilla - 4, altoCasilla - 4);
            
            // Numero de la casilla arriba a la izquierda
            gc.setFill(Color.web("#7f8c8d"));
            gc.setFont(Font.font(10));
            gc.fillText(String.valueOf(i), x + 5, y + 15);
            
            // Icono de la casilla en el centro
            gc.setFill(Color.web("#2c3e50"));
            gc.setFont(Font.font(24));
            gc.fillText(icono, x + anchoCasilla/2 - 12, y + altoCasilla/2 + 8);
        }
        
        // Dibuja los pinguinos encima del tablero
        dibujarPinguinos(gc);
    }
    
    // Devuelve el emoji que corresponde a cada tipo de casilla
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
    
    // Dibuja cada pinguino como un circulo de color con ojos y pico
    private void dibujarPinguinos(GraphicsContext gc) {
        ArrayList<jugador> jugadores = partida.getJugadores();
        
        for (int i = 0; i < jugadores.size(); i++) {
            jugador j = jugadores.get(i);
            int pos = j.getPosicion();
            int fila = pos / 10;
            int columna = pos % 10;
            
            double x = columna * anchoCasilla + anchoCasilla / 2;
            double y = fila * altoCasilla + altoCasilla / 2;
            
            // Mueve cada pinguino un poco para que no se tapen entre ellos
            double offsetX = i == 0 ? -12 : i == 1 ? 12 : i == 2 ? -12 : 12;
            double offsetY = i < 2 ? -12 : 12;
            
            // Elige el color del pinguino
            String color = switch(j.getColor()) {
                case "Rojo" -> "#e74c3c";
                case "Azul" -> "#3498db";
                case "Verde" -> "#2ecc71";
                default -> "#f1c40f";
            };
            
            // Dibuja el cuerpo (circulo de color)
            gc.setFill(Color.web(color));
            gc.fillOval(x - 15 + offsetX, y - 15 + offsetY, 30, 30);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeOval(x - 15 + offsetX, y - 15 + offsetY, 30, 30);
            
            // Dibuja los ojos
            gc.setFill(Color.WHITE);
            gc.fillOval(x - 8 + offsetX, y - 8 + offsetY, 6, 6);
            gc.fillOval(x + 2 + offsetX, y - 8 + offsetY, 6, 6);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 6 + offsetX, y - 6 + offsetY, 3, 3);
            gc.fillOval(x + 4 + offsetX, y - 6 + offsetY, 3, 3);
            
            // Dibuja el pico (triangulo naranja)
            gc.setFill(Color.ORANGE);
            double[] xPoints = {x + offsetX, x + 5 + offsetX, x - 5 + offsetX};
            double[] yPoints = {y + 2 + offsetY, y + 8 + offsetY, y + 8 + offsetY};
            gc.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    // Redibuja todo el tablero (se llama cuando algo cambia)
    public void actualizar() {
        dibujar();
    }
}