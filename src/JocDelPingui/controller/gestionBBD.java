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
    
    
    private static final String URL = "jdbc:oracle:thin:@192.168.3.26:1521/XEPDB2";
    private static final String USUARI = "DW2526_GR05_PINGU";
    private static final String CONTRASENYA = "AAPCSDS";
    
    
    public gestionBBD() {
        connectar();
    }
    
    
    private void connectar() {
        try {
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            
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
    
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    
    
    
    public int obtenirSeguentNumPartida() throws SQLException {
        String sql = "SELECT seq_num_partida.NEXTVAL FROM dual";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt(1);
    }
    
    
    private String guardarTaulell(partida p) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(p.getTurnos()).append("|");
        sb.append(p.getJugadorActual()).append("|");
        
        for (casilla c : p.getTablero().getCasillas()) {
            sb.append(c.getTipo()).append("|");
        }
        return sb.toString();
    }
    
    
    private String guardarInventari(pingino p) {
        return p.getPosicion() + "|" +
               p.getColor() + "|" +
               p.getInventario().getDausRapidos() + "|" +
               p.getInventario().getDausLentos() + "|" +
               p.getInventario().getPeces() + "|" +
               p.getInventario().getBolasNieve();
    }
    
    
    public boolean guardarPartida(partida partida, String nickname) {
        System.out.println("🔵 1. Entrando a guardarPartida para: " + nickname);
        
        if (!isConnected()) {
            System.out.println("❌ 2. No hay conexión");
            return false;
        }
        System.out.println("✅ 2. Conexión OK");
        
        try {
            connection.setAutoCommit(false);
            System.out.println("✅ 3. AutoCommit false OK");
            
            String id = partida.getIdPartida();
            System.out.println("📌 4. ID Partida: " + id);
            
            int numPartida = -1;
            boolean esNova = false;
            
            if (id != null && id.startsWith("PARTIDA_")) {
                try {
                    numPartida = Integer.parseInt(id.replace("PARTIDA_", ""));
                    System.out.println("📌 5. numPartida parseado: " + numPartida);
                    if (numPartida > 0) {
                        esNova = false;
                    } else {
                        esNova = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ 5. Error parseando ID");
                    esNova = true;
                }
            } else {
                System.out.println("📌 5. ID no válido, es partida nueva");
                esNova = true;
            }
            
            if (esNova) {
                System.out.println("📌 6. Es partida NUEVA, obteniendo siguiente número");
                numPartida = obtenirSeguentNumPartida();
                System.out.println("📌 7. Nuevo numPartida: " + numPartida);
            } else {
                System.out.println("📌 6. Es partida EXISTENTE, actualizando: " + numPartida);
            }
            
            // Guardar en PARTIDES
            if (esNova) {
                String sqlPartida = "INSERT INTO PARTIDES (num_partida, data, hora, taulell) VALUES (?, SYSDATE, SYSDATE, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sqlPartida)) {
                    pstmt.setInt(1, numPartida);
                    pstmt.setString(2, guardarTaulell(partida));
                    int filas = pstmt.executeUpdate();
                    System.out.println("📌 8. INSERT en PARTIDES: " + filas + " filas afectadas");
                }
            } else {
                String sqlUpdate = "UPDATE PARTIDES SET taulell = ?, data = SYSDATE, hora = SYSDATE WHERE num_partida = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                    pstmt.setString(1, guardarTaulell(partida));
                    pstmt.setInt(2, numPartida);
                    int filas = pstmt.executeUpdate();
                    System.out.println("📌 8. UPDATE en PARTIDES: " + filas + " filas afectadas");
                }
            }
            
            // Borrar y reinsetar jugadores
            String sqlDelete = "DELETE FROM PARTIDA_JUGADORS WHERE num_partida = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
                pstmt.setInt(1, numPartida);
                int filas = pstmt.executeUpdate();
                System.out.println("📌 9. DELETE en PARTIDA_JUGADORS: " + filas + " filas afectadas");
            }
            
            String sqlInsert = "INSERT INTO PARTIDA_JUGADORS (num_partida, nickname, inventari) VALUES (?, ?, ?)";
            int totalInsert = 0;
            try (PreparedStatement pstmt = connection.prepareStatement(sqlInsert)) {
                for (jugador j : partida.getJugadores()) {
                    pstmt.setInt(1, numPartida);
                    pstmt.setString(2, j.getNombre());
                    pstmt.setString(3, guardarInventari((pingino) j));
                    totalInsert += pstmt.executeUpdate();
                }
                System.out.println("📌 10. INSERT en PARTIDA_JUGADORS: " + totalInsert + " jugadores insertados");
            }
            
            connection.commit();
            System.out.println("✅ 11. COMMIT realizado con éxito!");
            
            partida.setIdPartida("PARTIDA_" + numPartida);
            return true;
            
        } catch (SQLException e) {
            try { if (connection != null) connection.rollback(); } catch (SQLException ex) {}
            System.out.println("❌ ERROR SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try { if (connection != null) connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
    
    
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
    
    
    public partida carregarPartidaCompleta(int numPartida) {
        if (!isConnected()) return null;
        
        try {
            partida p = new partida();
            
            
            String sqlPartida = "SELECT taulell FROM PARTIDES WHERE num_partida = ?";
            PreparedStatement pstmtPartida = connection.prepareStatement(sqlPartida);
            pstmtPartida.setInt(1, numPartida);
            ResultSet rsPartida = pstmtPartida.executeQuery();
            
            if (rsPartida.next()) {
                String taulellStr = rsPartida.getString("taulell");
                String[] parts = taulellStr.split("\\|");
                
                
                boolean formatNou = false;
                try {
                    if (parts.length >= 2) {
                        Integer.parseInt(parts[0]); 
                        formatNou = true;
                    }
                } catch (NumberFormatException e) {
                    formatNou = false;
                }

                if (formatNou) {
                    p.setTurnos(Integer.parseInt(parts[0]));
                    p.setJugadorActual(Integer.parseInt(parts[1]));
                    
                    
                    StringBuilder layout = new StringBuilder();
                    for (int i = 2; i < parts.length; i++) {
                        layout.append(parts[i]).append("|");
                    }
                    p.getTablero().inicializarDesdeString(layout.toString());
                } else {
                    
                    p.setTurnos(0);
                    p.setJugadorActual(0);
                    p.getTablero().inicializarDesdeString(taulellStr);
                }
            } else {
                return null;
            }
            
            
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
                        
                        
                        if (partsInv.length == 4) {
                            
                            pingino pin = new pingino(nickname, "Azul");
                            pin.getInventario().setDausRapidos(Integer.parseInt(partsInv[0]));
                            pin.getInventario().setDausLentos(Integer.parseInt(partsInv[1]));
                            pin.getInventario().setPeces(Integer.parseInt(partsInv[2]));
                            pin.getInventario().setBolasNieve(Integer.parseInt(partsInv[3]));
                            llistaJugadors.add(pin);
                        } else if (partsInv.length >= 6) {
                            
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
    
    
    public boolean finalitzarPartida(partida partida) {
        if (!isConnected() || partida == null) return false;
        
        try {
            connection.setAutoCommit(false);
            
            String id = partida.getIdPartida();
            int numPartida = -1;
            if (id != null && id.startsWith("PARTIDA_")) {
                try {
                    numPartida = Integer.parseInt(id.replace("PARTIDA_", ""));
                } catch (NumberFormatException e) {
                    System.out.println("❌ Error al parsejar ID de partida");
                    return false;
                }
            } else {
                System.out.println("❌ ID de partida no válido");
                return false;
            }
            
            System.out.println("Finalizando partida " + numPartida);
            
            String sqlUpdate = "UPDATE JUGADORS SET partides_jugades = partides_jugades + 1 WHERE nickname = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                for (jugador j : partida.getJugadores()) {
                    pstmt.setString(1, j.getNombre());
                    int filas = pstmt.executeUpdate();
                    System.out.println("✅ Actualizado partides_jugades para " + j.getNombre() + ": " + filas + " filas");
                }
            }
            
            String sqlPosicio = "UPDATE PARTIDA_JUGADORS SET posicio_max = ? WHERE num_partida = ? AND nickname = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlPosicio)) {
                for (jugador j : partida.getJugadores()) {
                    pstmt.setInt(1, j.getPosicion());
                    pstmt.setInt(2, numPartida);
                    pstmt.setString(3, j.getNombre());
                    int filas = pstmt.executeUpdate();
                    System.out.println("✅ posicio_max=" + j.getPosicion() + " para " + j.getNombre() + " (" + filas + " filas)");
                }
            }
            
            if (partida.getGanador() != null) {
                String sqlGuanyador = "UPDATE PARTIDES SET guanyador = ? WHERE num_partida = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sqlGuanyador)) {
                    pstmt.setString(1, partida.getGanador().getNombre());
                    pstmt.setInt(2, numPartida);
                    pstmt.executeUpdate();
                    System.out.println("✅ Guanyador registrat: " + partida.getGanador().getNombre());
                }
            }
            
            connection.commit();
            System.out.println("✅ Partida " + numPartida + " finalizada correctamente");
            return true;
            
        } catch (SQLException e) {
            try { if (connection != null) connection.rollback(); } catch (SQLException ex) {}
            System.out.println("❌ Error al finalitzar partida: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try { if (connection != null) connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
    
    
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
                         "WHERE partides_jugades > 0 " +
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
    
    
    public boolean registrarJugador(String nickname, String contrasenya) {
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            return false;
        }
        
        try {
            String sql = "INSERT INTO JUGADORS (nickname, contrasenya, partides_jugades) VALUES (?, ?, 0)";
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
    
    public void registrarGuanyador(int numPartida, String nicknameGuanyador) {
        if (!isConnected()) return;
        
        try {
            String sql = "UPDATE PARTIDES SET guanyador = ? WHERE num_partida = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nicknameGuanyador);
            pstmt.setInt(2, numPartida);
            pstmt.executeUpdate();
            System.out.println("✅ Guanyador " + nicknameGuanyador + " registrat a la partida " + numPartida);
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar guanyador: " + e.getMessage());
        }
    }
    
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