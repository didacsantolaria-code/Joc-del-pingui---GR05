package JocDelPingui;

public class eventsPingui {
	public class EventosJuego {
	    
	    private String tipoEvento;
	    private String descripcion;
	    private int efecto;
	    
	    public EventosJuego() {
	        this.tipoEvento = "";
	        this.descripcion = "";
	        this.efecto = 0;
	    }
	    
	    public EventosJuego(String tipoEvento, String descripcion, int efecto) {
	        this.tipoEvento = tipoEvento;
	        this.descripcion = descripcion;
	        this.efecto = efecto;
	    }
	    
	    // Getters y Setters
	    public String getTipoEvento() {
	        return tipoEvento;
	    }
	    
	    public void setTipoEvento(String tipoEvento) {
	        this.tipoEvento = tipoEvento;
	    }
	    
	    public String getDescripcion() {
	        return descripcion;
	    }
	    
	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
	    }
	    
	    public int getEfecto() {
	        return efecto;
	    }
	    
	    public void setEfecto(int efecto) {
	        this.efecto = efecto;
	    }
	}
}