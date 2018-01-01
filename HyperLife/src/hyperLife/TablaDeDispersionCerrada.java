//Sergio Alonso Pascual
//Helio Fernandez Abad
package hyperLife;

import hyperLife.Cuadrante;

public class TablaDeDispersionCerrada {
	//Tamano de la tabla
	public int m;
	//Maximo factor de carga
	public double maxL;
	public Cuadrante[] tabla;
	//Numero de elementos en la tabla
	public int n;
	
	public TablaDeDispersionCerrada(int m,double maxL) {
		n=0;
		this.m=m;
		this.maxL=maxL;
		tabla=new Cuadrante[m];
	}
	
	//Introduce un elememto en la tabla, si es necesario ampliar el tamaño de la tabla llama a reestructurar
	public void insertar(Cuadrante c) {
		n++;
		if((1.0*n)/m> maxL) reestructurar();
		
		int i,s,h;
		h= c.nw.hashCode() + 11*c.ne.hashCode() + 101*c.sw.hashCode() + 1007*c.se.hashCode();
		h = h ^ (h >>> 20) ^ (h >>> 12);
		h = h ^ (h >>> 7) ^ (h >>> 4);
		i = h % m;
		s = h / m;
		if(s < 0) { s = -s; }
		if(s % 2 == 0) { s++; }
		if(i < 0) { i = -i; }
		while(tabla[i] != null)
			i= (i+s)%m;

		tabla[i]=c;
	}
	
	//Devuelve el elemento correspondiente a la clave introducida, si este elemento no se encuentra en la tabla devuelve null
	public Cuadrante buscar(Cuadrante nw, Cuadrante ne, Cuadrante sw, Cuadrante se) {
		int i,s,h;
		h= nw.hashCode() + 11*ne.hashCode() + 101*sw.hashCode() + 1007*se.hashCode();
		h = h ^ (h >>> 20) ^ (h >>> 12);
		h = h ^ (h >>> 7) ^ (h >>> 4);
		i = h % m;
		s = h / m;
		if(s < 0) { s = -s; }
		if(s % 2 == 0) { s++; } 
		if(i < 0) { i = -i; }
		
		while(true){
			if(tabla[i]==null) {
				return null;
			}
			if(tabla[i].nw==nw && tabla[i].ne==ne && tabla[i].sw==sw && tabla[i].se==se) {
				return tabla[i];
			}
			i=(i+s)%m;
		}
	}
	
	//Duplica el tamaño de la tabla
	public void reestructurar() {
		//Salvamos la tabla anterior
		Cuadrante[] tmp=tabla;
		//creamos una nueva tabla
		n=0;
		m=2*m;//Duplicamos el tamano
		tabla=new Cuadrante[m];
		for (int i = 0; i < m; i++) {
			tabla[i]=null;
		}
		for (int i = 0; i < tmp.length; i++) {
			Cuadrante c=tmp[i];
			if(c!=null) {
				insertar(c);
			}
		}
	}
}
