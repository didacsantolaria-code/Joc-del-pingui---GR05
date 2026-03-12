package JocDelPingui.model;

public class inventario {
    private int daus; 
    private int peces;
    private int bolasNieve;
    
    //CONSTRUCTORS
    public inventario() {
        this.daus = 0;
        this.peces = 0;
        this.bolasNieve = 0;
    }
    
    public void inicializarInventario() {
        this.daus = 1; 
        this.peces = 0;
        this.bolasNieve = 0;
    }
    //METODES
    public void agregarPez() {
        if (peces < 2) peces++;
    }
    
    public void usarPez() {
        if (peces > 0) peces--;
    }
    
    public void agregarBolaNieve(int cantidad) {
        bolasNieve = Math.min(bolasNieve + cantidad, 6);
    }
    
    public void usarBolaNieve() {
        if (bolasNieve > 0) bolasNieve--;
    }
    
    public void agregarDado() {
        if (daus < 3) daus++;
    }
    
    public void perderObjetoAleatorio() {
        double random = Math.random();
        if (random < 0.33 && peces > 0) {
            peces--;
        } else if (random < 0.66 && bolasNieve > 0) {
            bolasNieve--;
        } else if (daus > 1) {
            daus--;
        }
    }
    
    public int totalObjetos() {
        return daus + peces + bolasNieve;
    }
    
    // Getters y Setters
    public int getDaus() { 
    	return daus; 
    }
    
    public void setDaus(int daus) { 
    	this.daus = Math.min(daus, 3); 
    }
    
    public int getPeces() { 
    	return peces; 
    }
    public void setPeces(int peces) { 
    	this.peces = Math.min(peces, 2); 
    }
    
    public int getBolasNieve() { 
    	return bolasNieve; 
    }
    
    public void setBolasNieve(int bolasNieve) { 
    	this.bolasNieve = Math.min(bolasNieve, 6); 
    }
    

    public String mostrar() {
    	return "Dados=" + daus + " Peces=" + peces + " Bolas de nieve=" + bolasNieve;
    }

}
