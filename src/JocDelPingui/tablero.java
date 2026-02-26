package JocDelPingui;

import java.util.Random;

public class tablero {
	 private int[] casillas;
	 private String nombre;
	 private Random random;
	 
	 public tablero() {
		 this.nombre = "Tablero Principal";
		 this.casillas = new int[50];
		 this.random = new Random();
	 }
	 
	 public tablero(String nombre) {
		 this.nombre = nombre;
		 this.casillas = new int[50];
		 this.random = new Random();
	 }
}
