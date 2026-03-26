package JocDelPingui.model;

public abstract class casilla {
    protected int posicion;
    protected String descripcion;

    public casilla(int posicion, String descripcion) {
        this.posicion = posicion;
        this.descripcion = descripcion;
    }

    public abstract void realizarAccion(partida partida, jugador jugador);

    public String getRutaImagen() {
        if (this instanceof casillaNormal) {
            if (posicion == 0)
                return "/JocDelPingui/images/casilla_meta.png"; // O salida si tuvieras
            if (posicion == 49)
                return "/JocDelPingui/images/casilla_meta.png";
            return "/JocDelPingui/images/casilla_normal.png";
        }
        if (this instanceof casillaOso)
            return "/JocDelPingui/images/casilla_oso.png";
        if (this instanceof casillaAgujero)
            return "/JocDelPingui/images/casilla_agujero.png";
        if (this instanceof casillaTrineo)
            return "/JocDelPingui/images/casilla_trineo.png";
        if (this instanceof casillaInterrogante)
            return "/JocDelPingui/images/casilla_interrogante.png";
        if (this instanceof casillaTierraQuebradiza)
            return "/JocDelPingui/images/casilla_tierrarota.png";

        return "/JocDelPingui/images/casilla_normal.png";
    }

    public int getPosicion() {
        return posicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return this.getClass().getSimpleName();
    }
}