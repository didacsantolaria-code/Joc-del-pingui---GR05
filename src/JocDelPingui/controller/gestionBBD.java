package JocDelPingui.controller;

import JocDelPingui.model.partida;

public class gestionBBD {
    
    public void guardarBDD(partida p) {
        System.out.println("📁 Partida guardada localmente: " + p.getIdPartida());
    }
    
    public partida cargarBDD(String id) {
        System.out.println("No hay partidas guardadas (modo sin BD)");
        return null;
    }
}