package JocDelPingui.controller;

import JocDelPingui.model.partida;
import JocDelPingui.model.jugador;
import JocDelPingui.model.pingino;
import JocDelPingui.model.casilla;
import JocDelPingui.model.tablero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Esta clase se encarga de todo lo que tiene que ver con la base de datos
public class gestionBBD {
    
    // La conexion con la base de datos
    private Connection connection;
    
    // Datos para conectarse a la base de datos (direccion, usuario y contraseña)
    private static final String URL = "jdbc:oracle:thin:@192.168.3.26:1521/XEPDB2";
    private static final String USUARI = "DW2526_GR05_PINGU";
    private static final String CONTRASENYA = "AAPCSDS";
    
    // Al crear esta clase, se conecta automaticamente a la BD
    public gestionBBD() {
        connectar();
    }
    
    // Intenta conectarse a la base de datos
    private void connectar() {
        try {
            // Carga el driver de Oracle para poder hablar con la BD
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Se conecta usando la direccion, usuario y contraseña
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
    
    // Cierra la conexion con la base de datos
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
    
    // Comprueba si estamos conectados a la BD
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Pide a la BD el siguiente numero de partida disponible
    public int obtenirSeguentNumPartida() throws SQLException {
        String sql = "SELECT seq_num_partida.NEXTVAL FROM dual";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt(1);
    }
    
    // Convierte el estado del tablero a texto para guardarlo en la BD
    private String guardarTaulell(partida p) {
        StringBuilder sb = new StringBuilder();
        
        // Primero guarda el turno actual y que jugador toca
        sb.append(p.getTurnos()).append("|");
        sb.append(p.getJugadorActual()).append("|");
        
        // Luego guarda el tipo de cada casilla
        for (casilla c : p.getTablero().getCasillas()) {
            sb.append(c.getTipo()).append("|");
        }
        return sb.toString();
    }
    
    // Convierte la info de un jugador (posicion, color, inventario) a texto
    private String guardarInventari(pingino p) {
        return p.getPosicion() + "|" +
               p.getColor() + "|" +
               p.getInventario().getDausRapidos() + "|" +
               p.getInventario().getDausLentos() + "|" +
               p.getInventario().getPeces() + "|" +
               p.getInventario().getBolasNieve();
    }
    
    // Guarda la partida entera en la base de datos
    public boolean guardarPartida(partida partida, String nickname) {
        System.out.println("🔵 1. Entrando a guardarPartida para: " + nickname);
        
        if (!isConnected()) {
            System.out.println("❌ 2. No hay conexión");
            return false;
        }
        System.out.println("✅ 2. Conexión OK");
        
        try {
            // Desactiva el guardado automatico para hacer todo de golpe
            connection.setAutoCommit(false);
            System.out.println("✅ 3. AutoCommit false OK");
            
            // Mira si la partida ya existia o es nueva
            String id = partida.getIdPartida();
            System.out.println("📌 4. ID Partida: " + id);
            
            int numPartida = -1;
            boolean esNova = false;
            
            // Intenta sacar el numero de la partida del ID
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
            
            // Si es nueva, pide un numero nuevo a la BD
            if (esNova) {
                System.out.println("📌 6. Es partida NUEVA, obteniendo siguiente número");
                numPartida = obtenirSeguentNumPartida();
                System.out.println("📌 7. Nuevo numPartida: " + numPartida);
            } else {
                System.out.println("📌 6. Es partida EXISTENTE, actualizando: " + numPartida);
            }
            
            // Guarda o actualiza la partida en la tabla PARTIDES
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
            
            // Borra los jugadores antiguos de esa partida y los vuelve a meter
            String sqlDelete = "DELETE FROM PARTIDA_JUGADORS WHERE num_partida = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
                pstmt.setInt(1, numPartida);
                int filas = pstmt.executeUpdate();
                System.out.println("📌 9. DELETE en PARTIDA_JUGADORS: " + filas + " filas afectadas");
            }
            
            // Mete cada jugador con su inventario
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
            
            // Confirma todos los cambios de golpe
            connection.commit();
            System.out.println("✅ 11. COMMIT realizado con éxito!");
            
            // Actualiza el ID de la partida
            partida.setIdPartida("PARTIDA_" + numPartida);
            return true;
            
        } catch (SQLException e) {
            // Si algo falla, deshace todos los cambios
            try { if (connection != null) connection.rollback(); } catch (SQLException ex) {}
            System.out.println("❌ ERROR SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Vuelve a activar el guardado automatico
            try { if (connection != null) connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
    
    // Busca en la BD las partidas guardadas de un jugador
    public List<String> obtenirPartidesPendents(String nickname) {
        List<String> partides = new ArrayList<>();
        
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            partides.add("❌ Sense connexió a la BD");
            return partides;
        }
        
        try {
            // Busca partidas donde participa este jugador
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
    
    // Carga una partida completa desde la BD (tablero + jugadores + inventarios)
    public partida carregarPartidaCompleta(int numPartida) {
        if (!isConnected()) return null;
        
        try {
            partida p = new partida();
            
            // Primero lee el tablero guardado
            String sqlPartida = "SELECT taulell FROM PARTIDES WHERE num_partida = ?";
            PreparedStatement pstmtPartida = connection.prepareStatement(sqlPartida);
            pstmtPartida.setInt(1, numPartida);
            ResultSet rsPartida = pstmtPartida.executeQuery();
            
            if (rsPartida.next()) {
                String taulellStr = rsPartida.getString("taulell");
                String[] parts = taulellStr.split("\\|");
                
                // Comprueba si el formato tiene turno y jugador actual al principio
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
                    // Formato nuevo: turno|jugadorActual|casillas...
                    p.setTurnos(Integer.parseInt(parts[0]));
                    p.setJugadorActual(Integer.parseInt(parts[1]));
                    
                    // Junta las casillas en un solo texto y recrea el tablero
                    StringBuilder layout = new StringBuilder();
                    for (int i = 2; i < parts.length; i++) {
                        layout.append(parts[i]).append("|");
                    }
                    p.getTablero().inicializarDesdeString(layout.toString());
                } else {
                    // Formato antiguo: solo casillas, sin turno
                    p.setTurnos(0);
                    p.setJugadorActual(0);
                    p.getTablero().inicializarDesdeString(taulellStr);
                }
            } else {
                return null;
            }
            
            // Ahora lee los jugadores de esa partida
            String sqlJugadors = "SELECT nickname, inventari FROM PARTIDA_JUGADORS WHERE num_partida = ?";
            ArrayList<jugador> llistaJugadors = new ArrayList<>();
            
            try (PreparedStatement pstmtJugadors = connection.prepareStatement(sqlJugadors)) {
                pstmtJugadors.setInt(1, numPartida);
                try (ResultSet rsJugadors = pstmtJugadors.executeQuery()) {
                    while (rsJugadors.next()) {
                        String nickname = rsJugadors.getString("nickname");
                        String invStr = rsJugadors.getString("inventari");
                        if (invStr != null) {
                            String[] partsInv = invStr.split("\\|");
                            
                            // Formato viejo: solo 4 datos del inventario
                            if (partsInv.length == 4) {
                                pingino pin = new pingino(nickname, "Azul");
                                pin.getInventario().setDausRapidos(Integer.parseInt(partsInv[0]));
                                pin.getInventario().setDausLentos(Integer.parseInt(partsInv[1]));
                                pin.getInventario().setPeces(Integer.parseInt(partsInv[2]));
                                pin.getInventario().setBolasNieve(Integer.parseInt(partsInv[3]));
                                llistaJugadors.add(pin);
                            } else if (partsInv.length >= 6) {
                                // Formato nuevo: posicion|color|datos inventario
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
            }

            // Si no hay jugadores, la partida no sirve
            if (llistaJugadors.isEmpty()) {
                System.out.println("⚠️ Partida " + numPartida + " no té jugadors registrats.");
                return null;
            }

            // Mete los jugadores en la partida y le pone el ID
            p.setJugadores(llistaJugadors);
            p.setIdPartida("PARTIDA_" + numPartida);
            
            return p;
            
        } catch (Exception e) {
            System.out.println("❌ Error al carregar partida completa: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Cuando alguien gana, actualiza las estadisticas en la BD
    public boolean finalitzarPartida(partida partida) {
        if (!isConnected() || partida == null) return false;
        
        try {
            connection.setAutoCommit(false);
            
            // Saca el numero de la partida de su ID
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
            
            // Suma 1 partida jugada a cada jugador
            String sqlUpdate = "UPDATE JUGADORS SET partides_jugades = partides_jugades + 1 WHERE nickname = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                for (jugador j : partida.getJugadores()) {
                    pstmt.setString(1, j.getNombre());
                    int filas = pstmt.executeUpdate();
                    System.out.println("✅ Actualizado partides_jugades para " + j.getNombre() + ": " + filas + " filas");
                }
            }
            
            // Guarda la posicion final de cada jugador
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
            
            // Apunta quien ha ganado
            if (partida.getGanador() != null) {
                String sqlGuanyador = "UPDATE PARTIDES SET guanyador = ? WHERE num_partida = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sqlGuanyador)) {
                    pstmt.setString(1, partida.getGanador().getNombre());
                    pstmt.setInt(2, numPartida);
                    pstmt.executeUpdate();
                    System.out.println("✅ Guanyador registrat: " + partida.getGanador().getNombre());
                }
            }
            
            // Confirma todos los cambios
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
    
    // Saca el top 5 de jugadores con mas partidas jugadas
    public List<String> obtenirRanking() {
        List<String> ranking = new ArrayList<>();
        
        if (!isConnected()) {
            System.out.println("❌ No hi ha connexió a la base de dades");
            ranking.add("❌ Sense connexió a la BD");
            return ranking;
        }
        
        try {
            // Busca los 5 jugadores con mas partidas
            String sql = "SELECT nickname, partides_jugades " +
                         "FROM JUGADORS " +
                         "WHERE partides_jugades > 0 " +
                         "ORDER BY partides_jugades DESC " +
                         "FETCH FIRST 5 ROWS ONLY";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            // Les pone medalla segun su posicion
            int pos = 1;
            while (rs.next()) {
                String medalla = pos == 1 ? "🥇" : pos == 2 ? "🥈" : pos == 3 ? "🥉" : "  ";
                ranking.add(medalla + " " + rs.getString("nickname") + " - " + rs.getInt("partides_jugades") + " partides");
                pos++;
            }
            
            // Si no hay nadie, pone medallas vacias
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
    
    // Registra un jugador nuevo en la BD con su nombre y contraseña
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
    
    // Apunta quien ha ganado una partida en la BD
    public void registrarGuanyador(int numPartida, String nicknameGuanyador) {
        if (isConnected()) {
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
    }
    
    // Comprueba si el nombre y la contraseña son correctos para iniciar sesion
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
    
    // Comprueba si un jugador ya existe en la BD
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