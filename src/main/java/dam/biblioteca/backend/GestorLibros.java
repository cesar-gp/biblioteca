package dam.biblioteca.backend;

import dam.biblioteca.enums.Criterio;

public class GestorLibros {
	
	// Propiedades estáticas

	Libro[] lista = new Libro[3];

	//Comprobar que el libro que se busca se encuentra en el almacén de la biblioteca
	public void BuscarLibro(Criterio criterio, String dato) {
	
		
		int contador = 0;
	//Crear lista vacia
		Libro[] listado = new Libro[lista.length];
		
	//Apuntar los encuentros
		for (int i = 0 ; i < lista.length ; i++) {
			
			if (criterio == Criterio.AUTOR) {
			//En caso de que haya encuentro
				if (lista[i].getAutor().equals(dato)) {
				
				//Ponemos el resultado en la nueva lista
					listado[i] = lista [i];
					contador++;
				}
			}
			
			if (criterio == Criterio.TITULO) {
				if (lista[i].getNombre().equals(dato)) {
					listado[i] = lista[i];
					contador++;
				}
			}
			
			if (criterio == Criterio.CATEGORIA) {
				if (lista[i].getCategoria().equals(dato)) {
					if(lista[i].getCategoria().equals(dato)) {
						listado[i] = lista[i];
						contador++;
					}
				}
			}
			
		}

	//Lista vacía para mostrar
		Libro[] mostrar = new Libro[contador];
		int posi = 0;
		
	//Llenar la lista para mostrar
		for (int i = 0 ; i < mostrar.length ; i++) {
			if (listado[i] != null) {
				mostrar[posi] = listado[i];
				posi++;
			}
		}
		
	//Mostrar los resultados
		for (int i = 0 ; i < lista.length ; i++) {
			if (listado[i] == null) {
			}
			else {
				System.out.println((i+1) + ":_ " +listado[i].getNombre());
			}
		}
	}

	
	public void Registro(Libro[] listado) {
		
		for (int i = 0 ; i < lista.length ; i++) {
			lista[i] = listado[i];
		}
	}
}

/*
//Crear lista vacía para mostrar
	
	Libro[] listado = new Libro[lista.length];
	

//posición de búsqueda en la biblioteca
	
	int j = 0 ;
	boolean esta = false;
	
//Bucle de búsqueda
	
	for (int i = 0 ;  i < listado.length-1; i++) {
		
		//Compara si el libro de la bibliteca coincide con el dato que piden
		
		while(esta == false || j < listado.length-1) {
			
		//Si tanto el nombre y categoría buscada coinciden con el libro lo devuelve
			
			if (lista[j].getAutor().equals(dato)) {
				esta = true;
				
		//Asociar el libro encontrado al listado para mostrar
				listado[i] = lista[j];
			}
			System.out.println(esta);
			
		//Esté o no, se aumenta el contador de la biblioteca
			j++;
		}
		
		System.out.println(i + " __" + j);
		
		esta = false;
		
	}
	
	for (int i = 0 ; i < lista.length-1 ; i++) {
		System.out.println(lista[i].getNombre());
	}*/
