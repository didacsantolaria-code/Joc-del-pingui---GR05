package JocDelPingui.model;

// Clase base de todas las casillas del tablero (cada casilla tiene algo diferente)
public abstract class casilla {
    protected int posicion;       // en que posicion del tablero esta
    protected String descripcion; // texto que describe la casilla

    // Crea una casilla con su posicion y descripcion
    public casilla(int posicion, String descripcion) {
        this.posicion = posicion;
        this.descripcion = descripcion;
    }

    // Cada tipo de casilla hace algo diferente cuando caes en ella
    public abstract void realizarAccion(partida partida, jugador jugador);

    // Devuelve la ruta de la imagen que corresponde a cada tipo de casilla
    public String getRutaImagen() {
        if (this instanceof casillaNormal) {
            if (posicion == 0)
                return "/JocDelPingui/images/casilla_meta.png";
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

    // Devuelve la posicion de la casilla
    public int getPosicion() {
        return posicion;
    }

    // Devuelve la descripcion de la casilla
    public String getDescripcion() {
        return descripcion;
    }

    // Devuelve el nombre del tipo de casilla (ej: "casillaOso")
    public String getTipo() {
        return this.getClass().getSimpleName();
    }
}