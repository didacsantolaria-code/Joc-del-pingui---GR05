package JocDelPingui.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import JocDelPingui.model.partida;
import JocDelPingui.model.pingino;

public class partidaView {
    
    @FXML private GridPane tablero;
    @FXML private Circle P1, P2, P3, P4;
    @FXML private Text dadoResultText;
    @FXML private Text rapido_t, lento_t, peces_t, nieve_t;
    @FXML private Text eventos;
    @FXML private Button dado, rapido, lento, peces, nieve;
    
    private partida partida;
    private static final int COLUMNS = 5;
    
    // Constructor vacío para FXML
    public partidaView() {
    }
    
    @FXML
    private void initialize() {
        eventos.setText("🎮 ¡El juego ha comenzado!");
    }
    
    public void setPartida(partida partida) {
        this.partida = partida;
        partida.setVistaActual(this);
        actualizarInventarios();
        actualizarPosicionesFichas();
    }
    
    public void agregarMensaje(String mensaje) {
        eventos.setText(mensaje);
    }
    
    @FXML
    private void handleDado() {
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);
        int resultado = p.getDadoActual().tirar();
        dadoResultText.setText("Ha salido: " + resultado);
        partida.moverJugador(p, resultado);
        
        partida.siguienteTurno(); 
        
        actualizarInventarios();
        actualizarPosicionesFichas();
        actualizarTurnoVisual();
        
        int nuevoIdx = partida.getJugadorActual();
        agregarMensaje("🎮 Turno de: " + partida.getJugadores().get(nuevoIdx).getNombre());
    }
    
    @FXML
    private void handleRapido() {
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);
        
        if (p.getInventario().getDausRapidos() > 0) {
            p.setDadoActual(new JocDelPingui.model.dado("rapido"));
            agregarMensaje("⚡ Activado dado rápido (5-10 casillas)");
            // Usar un dado rápido del inventario
            // Nota: Necesitarías llevar la cuenta de dados rápidos disponibles
        } else {
            agregarMensaje("❌ No tienes dados rápidos");
        }
        actualizarInventarios();
    }

    @FXML
    private void handleLento() {
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);
        
        if (p.getInventario().getDausLentos() > 0) {
            p.setDadoActual(new JocDelPingui.model.dado("lento"));
            agregarMensaje("🐢 Activado dado lento (1-3 casillas)");
            // Usar un dado lento del inventario
        } else {
            agregarMensaje("❌ No tienes dados lentos");
        }
        actualizarInventarios();
    }

    @FXML
    private void handlePeces() {
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);
        
        if (p.getInventario().getPeces() > 0) {
            agregarMensaje("🐟 Tienes " + p.getInventario().getPeces() + " peces");
            // Aquí puedes implementar qué hacen los peces además de sobornar
        } else {
            agregarMensaje("❌ No tienes peces");
        }
    }
    
    @FXML
    private void handleNieve() {
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);
        
        if (p.getInventario().getBolasNieve() > 0) {
            // Por ahora ataca al siguiente jugador
            int objetivo = (idxActual + 1) % partida.getJugadores().size();
            p.usarBolaNieve(partida.getJugadores().get(objetivo));
            agregarMensaje("❄️ " + p.getNombre() + " usó una bola de nieve");
            actualizarInventarios();
            actualizarPosicionesFichas();
        } else {
            agregarMensaje("❌ No tienes bolas de nieve");
        }
    }
    
    @FXML
    private void handleNewGame() {
        System.out.println("New Game");
        // Volver al menú o reiniciar
    }
    
    @FXML
    private void handleSaveGame() {
        System.out.println("Save Game");
        agregarMensaje("💾 Partida guardada");
    }
    
    @FXML
    private void handleLoadGame() {
        System.out.println("Load Game");
        agregarMensaje("📂 Cargar partida - Pendiente");
    }
    
    @FXML
    private void handleQuitGame() {
        System.out.println("Quit Game");
        System.exit(0);
    }

    private void actualizarPosicionesFichas() {
        Circle[] fichas = {P1, P2, P3, P4};
        for (int i = 0; i < partida.getJugadores().size() && i < fichas.length; i++) {
            if (fichas[i] != null) {
                int pos = partida.getJugadores().get(i).getPosicion();
                int fila = pos / 10;
                int columna = pos % 10;
                GridPane.setRowIndex(fichas[i], fila);
                GridPane.setColumnIndex(fichas[i], columna);
            }
        }
    }

    private void actualizarInventarios() {
        int idxActual = partida.getJugadorActual();
        pingino p = (pingino) partida.getJugadores().get(idxActual);
        rapido_t.setText("Dado rápido: " + p.getInventario().getDausRapidos());
        lento_t.setText("Dado lento: " + p.getInventario().getDausLentos());
        peces_t.setText("Peces: " + p.getInventario().getPeces());
        nieve_t.setText("Bolas de nieve: " + p.getInventario().getBolasNieve());
    }
    
    private void actualizarTurnoVisual() {
        Circle[] fichas = {P1, P2, P3, P4};
        
        for (Circle ficha : fichas) {
            if (ficha != null) {
                ficha.getStyleClass().remove("current-player");
            }
        }
        
        int idxActual = partida.getJugadorActual();
        if (idxActual < fichas.length && fichas[idxActual] != null) {
            fichas[idxActual].getStyleClass().add("current-player");
        }
    }
}