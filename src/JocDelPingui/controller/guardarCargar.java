package JocDelPingui.controller;

import java.io.*;
import JocDelPingui.model.partida;
import JocDelPingui.model.jugador;
import JocDelPingui.model.pingino;

public class guardarCargar {
    
    public guardarCargar(String urlBBDD, String username, String password) {
    }
    
    public boolean guardarPartida(partida partida, String rutaArchivo) {
        try {
            FileWriter writer = new FileWriter(rutaArchivo);
            
            writer.write(partida.getIdPartida() + "\n");
            writer.write(partida.getTurnos() + "\n");
            writer.write(partida.getJugadorActual() + "\n");
            writer.write(partida.isFinalizada() + "\n");
            
            int numJugadores = partida.getJugadores().size();
            writer.write(numJugadores + "\n");
            
            for (jugador j : partida.getJugadores()) {
                writer.write(j.getNombre() + "\n");
                writer.write(j.getColor() + "\n");
                writer.write(j.getPosicion() + "\n");
                writer.write(j.isPierdeTurno() + "\n");
                writer.write(((pingino)j).getInventario().getDaus() + "\n");
                writer.write(((pingino)j).getInventario().getPeces() + "\n");
                writer.write(((pingino)j).getInventario().getBolasNieve() + "\n");
            }
            
            writer.close();
            System.out.println("✅ Partida guardada en: " + rutaArchivo);
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ Error al guardar: " + e.getMessage());
            return false;
        }
    }
    
    public partida cargarPartida(String rutaArchivo) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
            
            String idPartida = reader.readLine();
            int turnos = Integer.parseInt(reader.readLine());
            int jugadorActual = Integer.parseInt(reader.readLine());
            boolean finalizada = Boolean.parseBoolean(reader.readLine());
            
            int numJugadores = Integer.parseInt(reader.readLine());
            
            partida partida = new partida();
            partida.setIdPartida(idPartida);
            partida.setTurnos(turnos);
            partida.setJugadorActual(jugadorActual);
            partida.setFinalizada(finalizada);
            
            for (int i = 0; i < numJugadores; i++) {
                String nombre = reader.readLine();
                String color = reader.readLine();
                int posicion = Integer.parseInt(reader.readLine());
                boolean pierdeTurno = Boolean.parseBoolean(reader.readLine());
                int dados = Integer.parseInt(reader.readLine());
                int peces = Integer.parseInt(reader.readLine());
                int bolas = Integer.parseInt(reader.readLine());
                
                pingino j = new pingino(nombre, color);
                j.setPosicion(posicion);
                j.setPierdeTurno(pierdeTurno);
                j.getInventario().setDaus(dados);
                j.getInventario().setPeces(peces);
                j.getInventario().setBolasNieve(bolas);
                
                partida.getJugadores().add(j);
            }
            
            reader.close();
            System.out.println("✅ Partida cargada desde: " + rutaArchivo);
            return partida;
            
        } catch (Exception e) {
            System.out.println("❌ Error al cargar: " + e.getMessage());
            return null;
        }
    }
}