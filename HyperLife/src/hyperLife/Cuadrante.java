//Sergio Alonso Pascual
//Helio Fernandez Abad
package hyperLife;

import hyperLife.TablaDeDispersionCerrada;

public class Cuadrante {

	public static final Cuadrante muerta = new Cuadrante(0);
	public static final Cuadrante viva = new Cuadrante(1);
	public static final TablaDeDispersionCerrada tabla = new TablaDeDispersionCerrada(32, 0.8);

	public final int niv; // Nivel del nodo
	public final long pob; // Población: Número de celdas vivas del nodo
	// Referencias a los 4 cuadrantes
	public final Cuadrante nw, ne, sw, se;
	// Referencia al resultado de calcular la sig. generación
	public Cuadrante res = null;

	// Creación de un nodo de nivel 0 -> celda
	protected Cuadrante(int valor) {
		niv = 0;
		pob = valor;
		nw = null;
		ne = null;
		sw = null;
		se = null;
	}

	// Creación de un nodo de nivel > 0
	protected Cuadrante(Cuadrante nw, Cuadrante ne, Cuadrante sw, Cuadrante se) {
		niv = nw.niv + 1;
		pob = nw.pob + ne.pob + sw.pob + se.pob;
		this.nw = nw;
		this.ne = ne;
		this.sw = sw;
		this.se = se;
	}

	// Métodos para obtener cuadrantes únicos
	public static Cuadrante crear(int valor) {
		if (valor == 0) {
			return muerta;
		} else {
			return viva;
		}
	}

	public static Cuadrante crear(Cuadrante nw, Cuadrante ne, Cuadrante sw, Cuadrante se) {
		Cuadrante c = tabla.buscar(nw, ne, sw, se);
		if (c == null) {
			c = new Cuadrante(nw, ne, sw, se);
			tabla.insertar(c);
		}
		return c;
	}

	// Crea recursivamente un árbol vacío del nivel especificado
	public static Cuadrante crearVacio(int niv) {
		if (niv == 0) {
			return crear(0);
		} else {
			Cuadrante vacio=crearVacio(niv-1);
			return crear(vacio,vacio,vacio, vacio);
		}
	}

	// Devuelve un cuadrante de tamaño doble que el actual,
	// con el cuadrante actual en su centro y el borde vacío
	public Cuadrante expandir() {
		if (niv == 0) {
			return crear(this, muerta, muerta, muerta);
		}
		Cuadrante vacio=crearVacio(niv-1);
		return crear(crear(vacio, vacio, vacio, nw),
				crear(vacio, vacio, ne, vacio),
				crear(vacio, sw, vacio, vacio),
				crear(se, vacio, vacio, vacio));
	}

	//Calcula las 2^n-2 iteraciones (si no esta calculada ya)
	public Cuadrante generacion() {
		if (res != null) {
			return res;
		}
		if (niv < 2) {
			System.out.println("esto no deberia suceder...");
			return null;
		}
		if (niv == 2) {
			return generacion2();
		}
		Cuadrante n00 = nw.generacion();
		Cuadrante n01 = crear(nw.ne, ne.nw, nw.se, ne.sw).generacion();
		Cuadrante n02 = ne.generacion();
		Cuadrante n10 = crear(nw.sw, nw.se, sw.nw, sw.ne).generacion();
		Cuadrante n11 = crear(nw.se, ne.sw, sw.ne, se.nw).generacion();
		Cuadrante n12 = crear(ne.sw, ne.se, se.nw, se.ne).generacion();
		Cuadrante n20 = sw.generacion();
		Cuadrante n21 = crear(sw.ne, se.nw, sw.se, se.sw).generacion();
		Cuadrante n22 = se.generacion();

		Cuadrante m00 = crear(n00, n01, n10, n11);
		Cuadrante m01 = crear(n01, n02, n11, n12);
		Cuadrante m10 = crear(n10, n11, n20, n21);
		Cuadrante m11 = crear(n11, n12, n21, n22);
		res = crear(m00.generacion(), m01.generacion(), m10.generacion(), m11.generacion());
		return res;
	}

	// Caso especial del cálculo cuando el nivel = 2
	public Cuadrante generacion2() {
		long x, y;
		long cont = 0;
		int[] celda = new int[4];
		int cursor = 0;
		for (y = 1; y <= 2; y++) {
			for (x = 1; x <= 2; x++) {
				if (this.getPixel(x - 1, y - 1) == 1) {
					cont++;
				}
				if (this.getPixel(x, y - 1) == 1) {
					cont++;
				}
				if (this.getPixel(x + 1, y - 1) == 1) {
					cont++;
				}
				if (this.getPixel(x - 1, y) == 1) {
					cont++;
				}
				if (this.getPixel(x + 1, y) == 1) {
					cont++;
				}
				if (this.getPixel(x - 1, y + 1) == 1) {
					cont++;
				}
				if (this.getPixel(x, y + 1) == 1) {
					cont++;
				}
				if (this.getPixel(x + 1, y + 1) == 1) {
					cont++;
				}
				if (this.getPixel(x, y) == 0 && cont == 3) {
					celda[cursor] = 1;
					cursor++;
				} else if (this.getPixel(x, y) == 1 && (cont < 2 || cont > 3)) {
					celda[cursor] = 0;
					cursor++;
				} else {
					celda[cursor] = this.getPixel(x, y);
					cursor++;
				}
				cont = 0;
			}
		}
		res = crear(crear(celda[0]), crear(celda[1]), crear(celda[2]), crear(celda[3]));
		return res;
	}

	// Devuelve el valor (población) del pixel (x,y) de este
	// cuadrante (coordenadas relativas al cuadrante)
	public int getPixel(long x, long y) {
		if (niv == 0) {
			return (int) pob;
		}
		if (x < Math.pow(2, niv) / 2 && y < Math.pow(2, niv) / 2) {
			return nw.getPixel(x, y);
		} else if (x >= Math.pow(2, niv) / 2 && y < Math.pow(2, niv) / 2) {
			return ne.getPixel((long) (x - Math.pow(2, niv) / 2), y);
		} else if (x < Math.pow(2, niv) / 2 && y >= Math.pow(2, niv) / 2) {
			return sw.getPixel(x, (long) (y - Math.pow(2, niv) / 2));
		} else {
			return se.getPixel((long) (x - Math.pow(2, niv) / 2), (long) (y - Math.pow(2, niv) / 2));
		}
	}

	// Devuelve un nuevo cuadrante, igual al actual salvo que el
	// pixel (x,y) cambia al valor v (coordenadas relativas)
	public Cuadrante setPixel(long x, long y, int v) {
		if (niv == 0) {
			return crear(v);
		}
		if (x < Math.pow(2, niv) / 2 && y < Math.pow(2, niv) / 2) {
			return crear(nw.setPixel(x, y, v), ne, sw, se);
		} else if (x >= Math.pow(2, niv) / 2 && y < Math.pow(2, niv) / 2) {
			return crear(nw, ne.setPixel((long) (x - Math.pow(2, niv) / 2), y, v), sw, se);
		} else if (x < Math.pow(2, niv) / 2 && y >= Math.pow(2, niv) / 2) {
			return crear(nw, ne, sw.setPixel(x, (long) (y - Math.pow(2, niv) / 2), v), se);
		} else {
			return crear(nw, ne, sw, se.setPixel((long) (x - Math.pow(2, niv) / 2), (long) (y - Math.pow(2, niv) / 2), v));
		}
	}

	//Devuelve true si todas las celdas vivas estan en el sub-subcuadrante central
	public boolean centroVivo() {
		if (this.niv < 3) {
			return false;
		}
		long poblacion = nw.se.se.pob + ne.sw.sw.pob + sw.ne.ne.pob + se.nw.nw.pob;
		if (poblacion == this.pob) {
			return true;
		} else if (poblacion < this.pob) {
			return false;
		} else {
			System.out.println("No se como te las has apañado para que haya mas celdas vivas dentro del Sub-subcuadrante que en el cuadrante total.");
			System.out.println("Enhorabuena, te has cargado el programa.");
			return false;
		}
	}
	
	//Muestra el cuadrante por pantalla
	public void printCuadrante() {
		System.out.println();
		System.out.println();
		System.out.println();
		for (int i = 0; i < Math.pow(2, niv); i++) {
			System.out.println();
			for (int j = 0; j < Math.pow(2, niv); j++) {
				System.out.print(getPixel(j, i));
			}
		}
	}
}
