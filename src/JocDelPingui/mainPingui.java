package JocDelPingui;

import java.util.Scanner;
import JocDelPingui.controller.gestionJugador;
import JocDelPingui.controller.guardarCargar;
import JocDelPingui.view.missatgesConsola;
import JocDelPingui.model.jugador;
import JocDelPingui.model.pingino;
import JocDelPingui.model.partida;

// Version del juego por consola (sin ventana grafica)
public class mainPingui {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        missatgesConsola.mostrarBienvenida();
        
        // Bucle del menu principal: se repite hasta que el usuario elija salir
        boolean salir = false;
        while (!salir) {
            missatgesConsola.mostrarMenuPrincipal();
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    iniciarNuevaPartida(scanner);  // empieza partida nueva
                    break;
                case 2:
                    cargarPartida(scanner);  // carga una partida guardada
                    break;
                case 3:
                    salir = true;  // sale del juego
                    missatgesConsola.mostrarDespedida();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
        scanner.close();
    }
    
    // Pide el nombre del usuario y crea una partida con 4 jugadores
    private static void iniciarNuevaPartida(Scanner scanner) {
        System.out.print("Introduce tu nombre de usuario: ");
        String username = scanner.nextLine();
        
        // Crea la lista de jugadores con nombre y color
        java.util.ArrayList<String[]> jugadoresInfo = new java.util.ArrayList<>();
        jugadoresInfo.add(new String[]{username, "Azul"});
        jugadoresInfo.add(new String[]{"Jugador 2", "Rojo"});
        jugadoresInfo.add(new String[]{"Jugador 3", "Verde"});
        jugadoresInfo.add(new String[]{"Jugador 4", "Amarillo"});
        
        // Crea la partida y la arranca
        partida partida = new partida();
        partida.inicializarPartida(jugadoresInfo);
        
        jugarPartida(partida, scanner);
    }
    
    // Bucle principal del juego: cada turno el jugador elige que hacer
    private static void jugarPartida(partida partida, Scanner scanner) {
        gestionJugador gestionJugador = new gestionJugador();
        
        // Se repite hasta que alguien gane
        while (!partida.isFinalizada()) {
            jugador jugadorActual = partida.getJugadores().get(partida.getJugadorActual());
            System.out.println("\n--- Turno de " + jugadorActual.getNombre() + " ---");
            System.out.println("Posición: " + jugadorActual.getPosicion());
            
            // Muestra el inventario del jugador
            pingino p = (pingino) jugadorActual;
            System.out.println("Inventario: " + p.getInventario().mostrar());
            
            // Muestra las opciones que tiene
            System.out.println("\nOpciones:");
            System.out.println("1. Tirar dado");
            System.out.println("2. Usar bola de nieve");
            System.out.println("3. Ver estado");
            System.out.println("4. Guardar y salir");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    // Tira el dado y mueve al jugador
                    gestionJugador.jugadorSeleccion(jugadorActual, 1, 0, partida.getTablero());
                    int pasos = p.getDadoActual().tirar();
                    System.out.println("Has sacado un " + pasos);
                    partida.moverJugador(jugadorActual, pasos);
                    gestionJugador.jugadorFinalizarTurno(jugadorActual);
                    partida.siguienteTurno();
                    break;
                case 2:
                    // Lanza una bola de nieve a otro jugador para hacerlo retroceder
                    if (p.getInventario().getBolasNieve() > 0) {
                        System.out.print("¿A qué jugador quieres atacar? (0-" + (partida.getJugadores().size()-1) + "): ");
                        int idxObjetivo = scanner.nextInt();
                        if (idxObjetivo >= 0 && idxObjetivo < partida.getJugadores().size() && idxObjetivo != partida.getJugadorActual()) {
                            jugador objetivo = partida.getJugadores().get(idxObjetivo);
                            p.usarBolaNieve(objetivo);
                            System.out.println("¡Has hecho retroceder a " + objetivo.getNombre() + "!");
                        } else {
                            System.out.println("Objetivo no válido");
                        }
                    } else {
                        System.out.println("No tienes bolas de nieve");
                    }
                    break;
                case 3:
                    // Muestra como va la partida
                    mostrarEstadoPartida(partida);
                    break;
                case 4:
                    // Guarda y sale
                    System.out.println("Partida guardada. ¡Hasta pronto!");
                    return;
            }
        }
        
        // Alguien ha ganado
        System.out.println("¡¡¡" + partida.getGanador().getNombre() + " ha ganado la partida!!!");
    }
    
    // Muestra la posicion y el inventario de todos los jugadores
    private static void mostrarEstadoPartida(partida partida) {
        System.out.println("\n--- ESTADO DE LA PARTIDA ---");
        for (jugador j : partida.getJugadores()) {
            pingino p = (pingino) j;
            System.out.println(j.getNombre() + " (" + j.getColor() + "): Posición " + j.getPosicion() + 
                             " | Inventario: " + p.getInventario().mostrar());
        }
        System.out.println("Turno actual: " + partida.getTurnos());
        System.out.println("----------------------------\n");
    }
    
    // Pide el archivo de la partida guardada y la carga
    private static void cargarPartida(Scanner scanner) {
        System.out.print("Introduce el nombre del archivo de partida: ");
        String archivo = scanner.nextLine();
        
        guardarCargar gc = new guardarCargar("", "", "");
        partida partida = gc.cargarPartida(archivo);
        
        if (partida != null) {
            System.out.println("Partida cargada correctamente");
            jugarPartida(partida, scanner);
        } else {
            System.out.println("No se pudo cargar la partida");
        }
    }
}