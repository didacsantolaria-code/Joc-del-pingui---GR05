package JocDelPingui.controller;

import JocDelPingui.model.partida;
import JocDelPingui.model.jugador;
import JocDelPingui.model.pingino;
import JocDelPingui.model.casilla;
import JocDelPingui.model.tablero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class gestionBBD {
    
    private Connection connection;
    
    // Dades de connexió a la base de dades
    private static final String URL = "jdbc:oracle:thin:@192.168.3.26:1521/XEPDB2";
    private static final String USUARI = "DW2526_GR05_PINGU";
    private static final String CONTRASENYA = "AAPCSDS";
    
    // Constructor
    public gestionBBD() {
        connectar();
    }
    
    // Connectar a la base de dades Oracle
    private void connectar() {
        try {
            // Carregar el driver d'Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Establir la connexió
            connection = DriverManager.getConnection(URL, USUARI, CONTRASENYA);
            System.out.println("✅ Connectat a la base de dades!");
            System.out.println("   Usuari: " + USUARI);
            System.out.println("   Servidor: " + URL);
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver d'Oracle no trobat: " + e.getMessage());
            System.out.println("   Has afegit el ojdbc8.jar al projecte?");
        } catch (SQLException e) {
            System.out.println("❌ Error de connexió: " + e.getMessage());
            System.out.println("   Comprova que el servidor estigui accessible.");
        }
    }
    
    // Tancar connexió
    public void tancar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Connexió tancada.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en tancar: " + e.getMessage());
        }
    }
    
    // Comprovar si la connexió està activa
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    // ============= MÈTODES PER AL JOC =============
    
    // Obtenir el següent número de partida (NEXTVAL)
    public int obtenirSeguentNumPartida() throws SQLException {
        String sql = "SELECT seq_num_partida.NEXTVAL FROM dual";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt(1);
    }
    
    // Guardar l'estat de la partida (metadades + taulell)
    private String guardarTaulell(partida p) {
        StringBuilder sb = new StringBuilder();
        // Pack: turnos|jugadorActual|casillas...
        sb.append(p.getTurnos()).append("|");
        sb.append(p.getJugadorActual()).append("|");
        
        for (casilla c : p.getTablero().getCasillas()) {
            sb.append(c.getTipo()).append("|");
        }
        return sb.toString();
    }
    
    // Guardar inventari ampliat (posicion|color|dausRapids|dausLents|peces|bolesNieve)
    private String guardarInventari(pingino p) {
        return p.getPosicion() + "|" +
               p.getColor() + "|" +
               p.getInventario().getDausRapidos() + "|" +
               p.getInventario().getDausLentos() + "|" +
               p.getInventario().getPeces() + "|" +
               p.getInventario().getBolasNieve();
    }
    
    // Guardar una partida completa (INSERT si és nova, UPDATE si ja existeix)
    public boolean guardarPartida(partida partida, String nickname) {
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            return false;
        }
        
        try {
            // Desactivar auto-commit per a transacció
            connection.setAutoCommit(false);

            // 1. Determinar si la partida ja existeix a la BD
            int numPartida = -1;
            String id = partida.getIdPartida();
            if (id != null && id.startsWith("PARTIDA_")) {
                try {
                    numPartida = Integer.parseInt(id.replace("PARTIDA_", ""));
                } catch (NumberFormatException e) {
                    numPartida = -1;
                }
            }

            boolean esNova = (numPartida <= 0);

            if (esNova) {
                // Partida nova: obtenir el següent número de seqüència
                numPartida = obtenirSeguentNumPartida();
            }
            
            if (esNova) {
                // ---- INSERT: partida nova ----
                String sqlPartida = "INSERT INTO PARTIDES (num_partida, data, hora, taulell) VALUES (?, SYSDATE, SYSDATE, ?)";
                try (PreparedStatement pstmtPartida = connection.prepareStatement(sqlPartida)) {
                    pstmtPartida.setInt(1, numPartida);
                    pstmtPartida.setString(2, guardarTaulell(partida));
                    pstmtPartida.executeUpdate();
                }
                
                String sqlJugador = "INSERT INTO PARTIDA_JUGADORS (num_partida, nickname, inventari) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtJugador = connection.prepareStatement(sqlJugador)) {
                    for (jugador j : partida.getJugadores()) {
                        pstmtJugador.setInt(1, numPartida);
                        pstmtJugador.setString(2, j.getNombre());
                        pstmtJugador.setString(3, guardarInventari((pingino) j));
                        pstmtJugador.executeUpdate();
                    }
                }
            } else {
                // ---- UPDATE: partida existent ----
                // Actualitzar el taulell a PARTIDES
                String sqlUpdatePartida = "UPDATE PARTIDES SET taulell = ?, data = SYSDATE, hora = SYSDATE WHERE num_partida = ?";
                try (PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdatePartida)) {
                    pstmtUpdate.setString(1, guardarTaulell(partida));
                    pstmtUpdate.setInt(2, numPartida);
                    pstmtUpdate.executeUpdate();
                }
                
                // Esborrar jugadors antics i tornar-los a insertar amb les dades actualitzades
                String sqlDeleteJugadors = "DELETE FROM PARTIDA_JUGADORS WHERE num_partida = ?";
                try (PreparedStatement pstmtDelete = connection.prepareStatement(sqlDeleteJugadors)) {
                    pstmtDelete.setInt(1, numPartida);
                    pstmtDelete.executeUpdate();
                }
                
                String sqlJugador = "INSERT INTO PARTIDA_JUGADORS (num_partida, nickname, inventari) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtJugador = connection.prepareStatement(sqlJugador)) {
                    for (jugador j : partida.getJugadores()) {
                        pstmtJugador.setInt(1, numPartida);
                        pstmtJugador.setString(2, j.getNombre());
                        pstmtJugador.setString(3, guardarInventari((pingino) j));
                        pstmtJugador.executeUpdate();
                    }
                }
            }
            
            // Assignar l'idPartida perquè futures guardades facin UPDATE
            partida.setIdPartida("PARTIDA_" + numPartida);
            
            // Confirmar transacció
            connection.commit();
            System.out.println("✅ Partida guardada amb número: " + numPartida + (esNova ? " (nova)" : " (actualitzada)"));
            return true;
            
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                System.out.println("❌ Error en rollback: " + ex.getMessage());
            }
            System.out.println("❌ Error al guardar: " + e.getMessage());
            return false;
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("❌ Error en restablir auto-commit: " + e.getMessage());
            }
        }
    }
    
    // Carregar llista de partides pendents d'un jugador
    public List<String> obtenirPartidesPendents(String nickname) {
        List<String> partides = new ArrayList<>();
        
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            partides.add("❌ Sense connexió a la BD");
            return partides;
        }
        
        try {
            String sql = "SELECT DISTINCT p.num_partida, p.data " +
                         "FROM PARTIDES p " +
                         "JOIN PARTIDA_JUGADORS pj ON p.num_partida = pj.num_partida " +
                         "WHERE pj.nickname = ? " +
                         "ORDER BY p.data DESC";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nickname);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                partides.add("Partida " + rs.getInt("num_partida") + " - " + rs.getDate("data"));
            }
            
            if (partides.isEmpty()) {
                partides.add("No hi ha partides pendents");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al carregar partides: " + e.getMessage());
            partides.add("❌ Error al carregar");
        }
        return partides;
    }
    
    // Carregar una partida completa des de la base de dades
    public partida carregarPartidaCompleta(int numPartida) {
        if (!isConnected()) return null;
        
        try {
            partida p = new partida();
            
            // 1. Carregar dades de la partida (taulell + metadades)
            String sqlPartida = "SELECT taulell FROM PARTIDES WHERE num_partida = ?";
            PreparedStatement pstmtPartida = connection.prepareStatement(sqlPartida);
            pstmtPartida.setInt(1, numPartida);
            ResultSet rsPartida = pstmtPartida.executeQuery();
            
            if (rsPartida.next()) {
                String taulellStr = rsPartida.getString("taulell");
                String[] parts = taulellStr.split("\\|");
                
                // Detectar format: El format nou té turnos|jugadorActual al principi
                boolean formatNou = false;
                try {
                    if (parts.length >= 2) {
                        Integer.parseInt(parts[0]); // Provem si el primer és un número
                        formatNou = true;
                    }
                } catch (NumberFormatException e) {
                    formatNou = false;
                }

                if (formatNou) {
                    p.setTurnos(Integer.parseInt(parts[0]));
                    p.setJugadorActual(Integer.parseInt(parts[1]));
                    
                    // Reconstruir taulell (resta de parts)
                    StringBuilder layout = new StringBuilder();
                    for (int i = 2; i < parts.length; i++) {
                        layout.append(parts[i]).append("|");
                    }
                    p.getTablero().inicializarDesdeString(layout.toString());
                } else {
                    // Format antic: tot són caselles, assumim valors per defecte per a turnos i jugadorActual
                    p.setTurnos(0);
                    p.setJugadorActual(0);
                    p.getTablero().inicializarDesdeString(taulellStr);
                }
            } else {
                return null;
            }
            
            // 2. Carregar jugadors
            String sqlJugadors = "SELECT nickname, inventari FROM PARTIDA_JUGADORS WHERE num_partida = ?";
            ArrayList<jugador> llistaJugadors = new ArrayList<>();
            
            try (PreparedStatement pstmtJugadors = connection.prepareStatement(sqlJugadors)) {
                pstmtJugadors.setInt(1, numPartida);
                try (ResultSet rsJugadors = pstmtJugadors.executeQuery()) {
                    while (rsJugadors.next()) {
                        String nickname = rsJugadors.getString("nickname");
                        String invStr = rsJugadors.getString("inventari");
                        if (invStr == null) continue;

                        String[] partsInv = invStr.split("\\|");
                        
                        // Compatibilitat amb formats antics:
                        if (partsInv.length == 4) {
                            // Format llegat: dausRapids|dausLents|peces|boles
                            pingino pin = new pingino(nickname, "Azul");
                            pin.getInventario().setDausRapidos(Integer.parseInt(partsInv[0]));
                            pin.getInventario().setDausLentos(Integer.parseInt(partsInv[1]));
                            pin.getInventario().setPeces(Integer.parseInt(partsInv[2]));
                            pin.getInventario().setBolasNieve(Integer.parseInt(partsInv[3]));
                            llistaJugadors.add(pin);
                        } else if (partsInv.length >= 6) {
                            // Format actual: posicion|color|dausRapids|dausLents|peces|boles
                            pingino pin = new pingino(nickname, partsInv[1]);
                            pin.setPosicion(Integer.parseInt(partsInv[0]));
                            pin.getInventario().setDausRapidos(Integer.parseInt(partsInv[2]));
                            pin.getInventario().setDausLentos(Integer.parseInt(partsInv[3]));
                            pin.getInventario().setPeces(Integer.parseInt(partsInv[4]));
                            pin.getInventario().setBolasNieve(Integer.parseInt(partsInv[5]));
                            llistaJugadors.add(pin);
                        }
                    }
                }
            }

            if (llistaJugadors.isEmpty()) {
                System.out.println("⚠️ Partida " + numPartida + " no té jugadors registrats.");
                return null;
            }

            p.setJugadores(llistaJugadors);
            p.setIdPartida("PARTIDA_" + numPartida);
            
            return p;
            
        } catch (Exception e) {
            System.out.println("❌ Error al carregar partida completa: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Carregar ranking de jugadors (els que més partides han jugat)
    public List<String> obtenirRanking() {
        List<String> ranking = new ArrayList<>();
        
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            ranking.add("❌ Sense connexió a la BD");
            return ranking;
        }
        
        try {
            String sql = "SELECT nickname, partides_jugades " +
                         "FROM JUGADORS " +
                         "ORDER BY partides_jugades DESC " +
                         "FETCH FIRST 5 ROWS ONLY";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            int pos = 1;
            while (rs.next()) {
                String medalla = pos == 1 ? "🥇" : pos == 2 ? "🥈" : pos == 3 ? "🥉" : "  ";
                ranking.add(medalla + " " + rs.getString("nickname") + " - " + rs.getInt("partides_jugades") + " partides");
                pos++;
            }
            
            if (ranking.isEmpty()) {
                ranking.add("🥇 -");
                ranking.add("🥈 -");
                ranking.add("🥉 -");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error al carregar ranking: " + e.getMessage());
            ranking.add("❌ Error al carregar");
        }
        return ranking;
    }
    
    // Registrar un nou jugador
    public boolean registrarJugador(String nickname, String contrasenya) {
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            return false;
        }
        
        try {
            String sql = "INSERT INTO JUGADORS (nickname, contrasenya) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nickname);
            pstmt.setString(2, contrasenya);
            pstmt.executeUpdate();
            System.out.println("✅ Jugador " + nickname + " registrat correctament!");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar: " + e.getMessage());
            return false;
        }
    }
    
    // Validar login d'un jugador
    public boolean validarLogin(String nickname, String contrasenya) {
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            return false;
        }
        
        try {
            String sql = "SELECT * FROM JUGADORS WHERE nickname = ? AND contrasenya = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nickname);
            pstmt.setString(2, contrasenya);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("❌ Error al validar: " + e.getMessage());
            return false;
        }
    }
    
    // Comprovar si un jugador existeix
    public boolean existeixJugador(String nickname) {
        if (!isConnected()) return false;
        
        try {
            String sql = "SELECT * FROM JUGADORS WHERE nickname = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nickname);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}