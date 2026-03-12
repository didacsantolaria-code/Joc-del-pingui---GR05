package JocDelPingui.controller;

import JocDelPingui.model.partida;

public class gestionBBD {
    
    public void guardarBDD(partida p) {
        // Versión sin base de datos
        System.out.println("📁 Partida guardada localmente: " + p.getIdPartida());
    }
    
    public partida cargarBDD(String id) {
        // Versión sin base de datos
        System.out.println("No hay partidas guardadas (modo sin BD)");
        return null;
    }
}