package JocDelPingui.controller;

public class TestConnexio {
    
    public static void main(String[] args) {
        System.out.println("=== PROVA DE CONNEXIÓ A LA BD ===");
        
        // Crear instància de gestionBBD
        gestionBBD gb = new gestionBBD();
        
        // Comprovar si la connexió està activa
        if (gb.isConnected()) {
            System.out.println("✅ CONNEXIÓ OK!");
            
            // Prova de login amb l'usuari 'prova' que vas crear
            boolean login = gb.validarLogin("prova", "1234");
            if (login) {
                System.out.println("✅ Login correcte per a 'prova'");
            } else {
                System.out.println("❌ Login incorrecte per a 'prova'");
            }
            
            // Obtenir ranking
            System.out.println("\n--- RANKING ---");
            for (String rank : gb.obtenirRanking()) {
                System.out.println(rank);
            }
            
            // Tancar connexió
            gb.tancar();
            
        } else {
            System.out.println("❌ CONNEXIÓ FALLIDA!");
            System.out.println("   Comprova:");
            System.out.println("   1. El driver ojdbc8.jar està afegit al Classpath");
            System.out.println("   2. Les dades de connexió són correctes");
            System.out.println("   3. El servidor està accessible");
        }
        
        System.out.println("\n=== FI DE LA PROVA ===");
    }
}