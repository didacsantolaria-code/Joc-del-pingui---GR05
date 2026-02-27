package JocDelPingui;

public class pingui {
	private String nombre;
    private String color;
    private int posicion;
    private int dadosExtra;
    private int peces;
    private int bolasNieve;
    
    public pingui(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = 0;
        this.dadosExtra = 0;
        this.peces = 0;
        this.bolasNieve = 0;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getColor() {
        return color;
    }
    
    public int getPosicion() {
        return posicion;
    }
    
    public int getDadosExtra() {
        return dadosExtra;
    }
    
    public int getPeces() {
        return peces;
    }
    
    public int getBolasNieve() {
        return bolasNieve;
    }
}
