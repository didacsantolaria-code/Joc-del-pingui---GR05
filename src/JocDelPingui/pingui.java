package JocDelPingui;

public class pingui {
	/**
	 * Clase principal del juego
	 */
	public class JocPingui {
	    
	    private String titol;
	    private String versio;
	    private int numJugadors;
	    private boolean partidaIniciada;
	    private boolean partidaFinalitzada;
	    
	    public JocPingui() {
	        this.titol = "El Joc d'en Pingüí";
	        this.versio = "1.0";
	        this.numJugadors = 0;
	        this.partidaIniciada = false;
	        this.partidaFinalitzada = false;
	    }
	    
	    public JocPingui(String titol, String versio, int numJugadors) {
	        this.titol = titol;
	        this.versio = versio;
	        this.numJugadors = numJugadors;
	        this.partidaIniciada = false;
	        this.partidaFinalitzada = false;
	    }
	    
	    // Getters y Setters
	    public String getTitol() {
	        return titol;
	    }
	    
	    public void setTitol(String titol) {
	        this.titol = titol;
	    }
	    
	    public String getVersio() {
	        return versio;
	    }
	    
	    public void setVersio(String versio) {
	        this.versio = versio;
	    }
	    
	    public int getNumJugadors() {
	        return numJugadors;
	    }
	    
	    public void setNumJugadors(int numJugadors) {
	        this.numJugadors = numJugadors;
	    }
	    
	    public boolean isPartidaIniciada() {
	        return partidaIniciada;
	    }
	    
	    public void setPartidaIniciada(boolean partidaIniciada) {
	        this.partidaIniciada = partidaIniciada;
	    }
	    
	    public boolean isPartidaFinalitzada() {
	        return partidaFinalitzada;
	    }
	    
	    public void setPartidaFinalitzada(boolean partidaFinalitzada) {
	        this.partidaFinalitzada = partidaFinalitzada;
	    }
	}
}
