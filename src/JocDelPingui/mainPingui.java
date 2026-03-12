package JocDelPingui;

import java.util.Scanner;

public class mainPingui {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        missatgesConsola.mostrarBienvenida();
        
   //MENU INICIAL     
        boolean salir = false;
        while (!salir) {
            missatgesConsola.mostrarMenuPrincipal();
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    iniciarNuevaPartida(scanner);
                    break;
                case 2:
                	cargarPartida(scanner);
                    break;
                case 3:
                    salir = true;
                    missatgesConsola.mostrarDespedida();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
        scanner.close();
    }
    
    private static void iniciarNuevaPartida(Scanner scanner) {
        System.out.print("Introduce tu nombre de usuario: ");
        String username = scanner.nextLine();
        
        partida partida = new partida();
        partida.inicializarPartida(username);
        
        jugarPartida(partida, scanner);
    }
    

    
    private static void jugarPartida(partida partida, Scanner scanner) {
        gestionJugador gestionJugador = new gestionJugador();
        
        while (!partida.isFinalizada()) {
            jugador jugadorActual = partida.getJugadores().get(partida.getJugadorActual());
            System.out.println("\n--- Turno de " + jugadorActual.getNombre() + " ---");
            System.out.println("Posición: " + jugadorActual.getPosicion());
            System.out.println("Inventario: " + jugadorActual.getInventario());
            
            System.out.println("\nOpciones:");
            System.out.println("1. Tirar dado");
            System.out.println("2. Usar bola de nieve");
            System.out.println("3. Ver estado");
            System.out.println("4. Guardar y salir");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    gestionJugador.jugadorSeleccion(jugadorActual, 1, 0, partida.getTablero());
                    int pasos = jugadorActual.getDado().tirar();
                    System.out.println("Has sacado un " + pasos);
                    partida.moverJugador(jugadorActual, pasos);
                    gestionJugador.jugadorFinalizarTurno(jugadorActual);
                    partida.siguienteTurno();
                    break;
                case 2:
                    if (jugadorActual.getInventario().getBolasNieve() > 0) {
                        System.out.print("¿A qué jugador quieres atacar? (0-" + (partida.getJugadores().size()-1) + "): ");
                        int idxObjetivo = scanner.nextInt();
                        if (idxObjetivo >= 0 && idxObjetivo < partida.getJugadores().size() && idxObjetivo != partida.getJugadorActual()) {
                            jugador objetivo = partida.getJugadores().get(idxObjetivo);
                            jugadorActual.usarBolaNieve(objetivo);
                            System.out.println("¡Has hecho retroceder a " + objetivo.getNombre() + "!");
                        } else {
                            System.out.println("Objetivo no válido");
                        }
                    } else {
                        System.out.println("No tienes bolas de nieve");
                    }
                    break;
                case 3:
                    mostrarEstadoPartida(partida);
                    break;
                case 4:
                    System.out.println("Partida guardada. ¡Hasta pronto!");
                    return;
            }
        }
        
        System.out.println("¡¡¡" + partida.getGanador().getNombre() + " ha ganado la partida!!!");
    }
    
    private static void mostrarEstadoPartida(partida partida) {
        System.out.println("\n--- ESTADO DE LA PARTIDA ---");
        for (jugador j : partida.getJugadores()) {
            System.out.println(j.getNombre() + " (" + j.getColor() + "): Posición " + j.getPosicion() + 
                             " | Inventario: " + j.getInventario());
        }
        System.out.println("Turno actual: " + partida.getTurnos());
        System.out.println("----------------------------\n");
    }
    
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