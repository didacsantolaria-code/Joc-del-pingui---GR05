package JocDelPingui.model;

import javafx.scene.image.Image;

public abstract class casilla {
    protected int posicion;
    protected String descripcion;
    protected Image imagen;
    
    public casilla(int posicion, String descripcion, String rutaImagen) {
        this.posicion = posicion;
        this.descripcion = descripcion;
        

        try {
            this.imagen = new Image(getClass().getResourceAsStream(rutaImagen));
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + rutaImagen);
            this.imagen = null;
        }
    }
    
    public abstract void realizarAccion(partida partida, jugador jugador);
    
    public Image getImagen() { return imagen; }
    public int getPosicion() { return posicion; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return this.getClass().getSimpleName(); }
}