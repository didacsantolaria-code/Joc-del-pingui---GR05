package JocDelPingui;

// Esta clase guarda la info basica del juego: titulo, version, etc.
public class jocPingui {
    private String titol;           // nombre del juego
    private String versio;          // version del juego
    private int numJugadors;        // cuantos jugadores hay
    private boolean partidaIniciada;     // si ya empezo la partida
    private boolean partidaFinalitzada;  // si ya acabo la partida
    
    // Crea el juego con valores por defecto
    public jocPingui() {
        this.titol = "El Joc del Pingüí";
        this.versio = "1.0";
        this.numJugadors = 0;
        this.partidaIniciada = false;
        this.partidaFinalitzada = false;
    }
    
    // Crea el juego con los datos que le pases
    public jocPingui(String titol, String versio, int numJugadors) {
        this.titol = titol;
        this.versio = versio;
        this.numJugadors = numJugadors;
        this.partidaIniciada = false;
        this.partidaFinalitzada = false;
    }
    
    // Para obtener y cambiar los datos del juego
    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }
    public String getVersio() { return versio; }
    public void setVersio(String versio) { this.versio = versio; }
    public int getNumJugadors() { return numJugadors; }
    public void setNumJugadors(int numJugadors) { this.numJugadors = numJugadors; }
    public boolean isPartidaIniciada() { return partidaIniciada; }
    public void setPartidaIniciada(boolean partidaIniciada) { this.partidaIniciada = partidaIniciada; }
    public boolean isPartidaFinalitzada() { return partidaFinalitzada; }
    public void setPartidaFinalitzada(boolean partidaFinalitzada) { this.partidaFinalitzada = partidaFinalitzada; }
}