package JocDelPingui.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import JocDelPingui.MainApp;

public class MenuView extends VBox {
    
    public MenuView(MainApp mainApp) {
        // Espacio entre elementos
        this.setSpacing(10);
        
        // Título
        Label titulo = new Label("🐧 MENÚ PRINCIPAL 🐧");
        
        // Botones
        Button btnNueva = new Button("Nueva Partida");
        Button btnCargar = new Button("Cargar Partida");
        Button btnSalir = new Button("Salir");
        
        // Acción del botón salir
        btnSalir.setOnAction(e -> System.exit(0));
        
        // Añadir todo al contenedor
        this.getChildren().addAll(titulo, btnNueva, btnCargar, btnSalir);
    }
}