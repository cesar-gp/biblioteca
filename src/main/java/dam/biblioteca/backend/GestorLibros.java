package dam.biblioteca.backend;

import dam.biblioteca.enums.Categoria;
import dam.biblioteca.enums.Criterio;

public class GestorLibros {
	
	// Propiedades estáticas

	Libro[] lista = new Libro[0];

	//Obtener lista de libros
	public Libro[] getLibros() {
		return this.lista;
	}
	
	//Obtener isbn de libro para saber que está registrado
	public Libro getLibro(String isbn) {
		for(int i = 0 ; i<lista.length ; i++) {
			if (lista[i].getIsbn().equalsIgnoreCase(isbn)) return this.lista[i];
		}
		return null;
	}	
	
	//Comprobar que el libro que se busca se encuentra en el almacén de la biblioteca
	// TODO: cambiar para devolver un array de Libros.
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
				if (lista[i].getTitulo().equals(dato)) {
					listado[i] = lista[i];
					contador++;
				}
			}
			
			if (criterio == Criterio.CATEGORIA) {
				if (lista[i].getCategoria().equals(dato)) {
					listado[i] = lista[i];
					contador++;
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
			if (listado[i] != null) {
				System.out.println((i+1) + ":_ " +listado[i].getTitulo());
			}
		}
	}
	
	public int registrar(String titulo, String autor, Categoria categoria, String isbn) {
		//Si no hay hueco en la biblioteca
		if (this.lista.length == Integer.MAX_VALUE) return 1;
		
		//Saber si el libro está repetido
		if (getLibro(isbn) != null ) return 2;
		
		//Aumentar la lista de libros
		Libro[] nuevo = new Libro[this.lista.length+1];
		//Pasar los elementos registrados a una nueva lista
		for (int i = 0 ; i < lista.length ; i++) {
			nuevo[i] = this.lista[i];
		}
		//Añadir el nuevo libro
		nuevo[this.lista.length] = new Libro(titulo, autor, categoria, isbn);
		//Usar la nueva lista
		this.lista = nuevo;
		
		//Finalizar accion
		return 0;
	}
	
}