//Sergio Alonso Pascual
//Helio Fernandez Abad
package hyperLife;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import hyperLife.Cuadrante;

public class HiperVida {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		System.out.println("Introduzca el nombre del fichero txt a usar: ");
		String fichero = teclado.nextLine();
		System.out.println("Introduzca el número de iteraciones deseadas: ");
		long iteraciones = teclado.nextLong();
		Cuadrante tablero = leerFichero(fichero);
		long i=0;
		long inicio = System.currentTimeMillis(); // Esta variable contiene el tiempo que ha pasado, en milisegundos, desde el 1 de Enero de el año actual hasta el momento en el que se ejecuta esta misma línea.
		while (i<iteraciones) {
			while (!tablero.centroVivo()) {
				tablero = tablero.expandir();
			}	
			i+=Math.pow(2, tablero.niv-2);
			tablero=tablero.generacion();
		}
		long acaba = System.currentTimeMillis(); // Esta variable hace lo mismo que la variable inicio, solo que se ejecuta una vez acabamos las iteraciones.
		ArrayList<ArrayList<Boolean>> grid = transformarALista(tablero);
		reducirMatriz(grid);
		double tiempo = (acaba-inicio)/1000.0;//Este tiempo solo incluye el calculo de itereaciones, no la lectura del fichero ni la reduccion final
		System.out.println("1.Número de iteraciones solicitadas: " + iteraciones);
		System.out.println("2.Número de iteraciones realizadas: "+i);
		System.out.println("3.Número de celdas vivas: " + tablero.pob);
		System.out.println("4.Dimensiones: " + grid.size() + "x" + grid.get(0).size());
		System.out.println("5.Tiempo: " + tiempo+ " segundos.");
		System.out.println("Introduzca el nombre del fichero en el que desea almacenar el patrón: ");
		String nombre = teclado.nextLine();
		nombre = teclado.nextLine();
		teclado.close();
		escribirFichero(grid, nombre);
	}

	public static Cuadrante leerFichero(String nombreFichero) {
		// Lee el fichero (el patrón) y crea el tablero.
		Scanner f = null;
		try {
			f = new Scanner(new FileReader(nombreFichero));
			int numfilas = f.nextInt();
			int numcols = f.nextInt();
			Cuadrante c = Cuadrante.crearVacio((int) Math.ceil((Math.log(Math.max(numfilas, numcols)) / Math.log(2))));
			for (int i = 0; i < numfilas; i++) {
				String line = f.next();
				for (int j = 0; j < numcols; j++) {
					if (line.charAt(j) == 'X')
						c=c.setPixel(j, i, 1);
				}
			}
			f.close();
			return c;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("No se ha encontrado el fichero");
		}
	}

	public static void reducirMatriz(ArrayList<ArrayList<Boolean>> grid) {// TO DO
		// Método encargado de que el tablero mostrado al finalizar la simulación sea el
		// "rectángulo mínimo", es decir, que no presente filas ni columnas en los
		// bordes compuestas por únicamente celdas muertas.
		boolean reduce = true;
		// test up
		while (reduce) {
			for (int i = 0; i < grid.get(0).size(); i++) {
				if (grid.get(0).get(i)) {
					reduce = false;
					break;
				}
			}
			if (reduce) {
				grid.remove(0);
			}
		}
		// test down
		reduce = true;
		while (reduce) {
			for (int i = 0; i < grid.get(0).size(); i++) {
				if (grid.get(grid.size() - 1).get(i)) {
					reduce = false;
					break;
				}
			}
			if (reduce) {
				grid.remove(grid.size() - 1);
			}
		}
		// test left
		reduce = true;
		while (reduce) {
			for (int i = 0; i < grid.size(); i++) {
				if (grid.get(i).get(0)) {
					reduce = false;
					break;
				}
			}
			if (reduce) {
				for (int i = 0; i < grid.size(); i++) {
					grid.get(i).remove(0);
				}
			}
		}

		// test right
		reduce = true;
		while (reduce) {
			for (int i = 0; i < grid.size(); i++) {
				if (grid.get(i).get(grid.get(0).size() - 1)) {
					reduce = false;
					break;
				}
			}
			if (reduce) {
				for (int i = 0; i < grid.size(); i++) {
					grid.get(i).remove(grid.get(i).size() - 1);
				}
			}
		}

	}

	public static void escribirFichero(ArrayList<ArrayList<Boolean>> grid, String nombreFichero) {
		// Este método es el encargado de almacenar el patrón final (en el formato
		// indicado) en un fichero cuyo nombre se pide al usuario anteriormente.
		if (nombreFichero == null || nombreFichero.equals(""))
			for (int i = 0; i < grid.size(); i++) {
				System.out.println();
				for (int j = 0; j < grid.get(i).size(); j++) {
					if (grid.get(i).get(j))
						System.out.print("X");
					else
						System.out.print(".");
				}
			}
		else
			try {
				PrintWriter fichero = new PrintWriter(nombreFichero);
				fichero.println("Filas: " + grid.size());
				fichero.print("Columnas: " + grid.get(0).size());
				for (int i = 0; i < grid.size(); i++) {
					fichero.println();
					for (int j = 0; j < grid.get(i).size(); j++) {
						if (grid.get(i).get(j))
							fichero.print("X");
						else
							fichero.print(".");
					}
				}
				fichero.close();
			} catch (IOException e) {
				System.out.println(e);
				return;
			}
	}

	public static ArrayList<ArrayList<Boolean>> transformarALista(Cuadrante c) {
		//Transforma un cuadrante en un ArrayList
		ArrayList<ArrayList<Boolean>> l = new ArrayList<ArrayList<Boolean>>();
		for (int i = 0; i < Math.pow(2, c.niv); i++) {
			l.add(new ArrayList<Boolean>());
			for (int j = 0; j < Math.pow(2, c.niv); j++) {
				if (c.getPixel(j, i) == 0) {
					l.get(i).add(false);
				} else {
					l.get(i).add(true);
				}
			}
		}
		return l;
	}
}















