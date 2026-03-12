package JocDelPingui;

public class guardarCargar {
    private String urlBBDD;
    private String username;
    private String password;
    private encriptacionUtil encriptador;
    
    public guardarCargar(String urlBBDD, String username, String password) {
        this.urlBBDD = urlBBDD;
        this.username = username;
        this.password = password;
        this.encriptador = new encriptacionUtil();
    }
    
    public boolean guardarPartida(partida partida, String rutaArchivo) {
        try {
            String datos = serializarPartida(partida);
           // String datosEncriptados = encriptador.encriptar(datos);
            // Escribir a archivo
            System.out.println("Partida guardada en: " + rutaArchivo);
            return true;
        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
            return false;
        }
    }
    
   /* public partida cargarPartida(String rutaArchivo) {
        try {
            // Leer de archivo
            String datosEncriptados = ""; // Leer archivo
            String datos = encriptador.desencriptar(datosEncriptados);
            return deserializarPartida(datos);
        } catch (Exception e) {
            System.out.println("Error al cargar: " + e.getMessage());
            return null;
        }
    }*/
    
    private String serializarPartida(partida p) {
        // Similar a GestionBDD
        return "";
    }
    
    private partida deserializarPartida(String datos) {
        return new partida();
    }
}