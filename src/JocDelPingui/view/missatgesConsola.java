package JocDelPingui.view;

// Mensajes que se muestran en la consola (version sin ventana grafica)
public class missatgesConsola {

    // Muestra el mensaje de bienvenida al abrir el juego
    public static void mostrarBienvenida() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     BIENVENIDO AL JUEGO DEL PINGÜINO   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
    
    // Muestra las opciones del menu principal
    public static void mostrarMenuPrincipal() {
        System.out.println("\n    MENÚ PRINCIPAL    ");
        System.out.println(" 1. Nueva partida");
        System.out.println(" 2. Cargar partida");
        System.out.println(" 3. Salir");
        System.out.print("  Selecciona una opción: ");
    }
    
    // Mensaje de despedida al salir del juego
    public static void mostrarDespedida() {
        System.out.println("\n¡Juego finalizado!");
    } 
    
    // Muestra las instrucciones del juego con todos los elementos
    public static void mostrarInstrucciones() {
        System.out.println("\n    INSTRUCCIONES    ");
        System.out.println("• 🐧 Pingüino: Tu ficha");
        System.out.println("• 🐻 Oso: Te devuelve al inicio (usa peces para sobornar)");
        System.out.println("• 🕳️ Agujero: Caes al agujero anterior");
        System.out.println("• 🛷 Trineo: Avanzas al siguiente trineo");
        System.out.println("• ❓ Interrogante: Evento aleatorio");
        System.out.println("• 🎲 Dados: Normal(1-6), Rápido(5-10), Lento(1-3)");
        System.out.println("• ❄️ Bolas de nieve: Hacen retroceder a otros jugadores");
        System.out.println("• 🐟 Peces: Para sobornar al oso");
        System.out.println("----------------------\n");
    }
}