package dam.biblioteca.backend;

import dam.biblioteca.enums.Categoria;
import dam.biblioteca.enums.Criterio;

public class GestorLibros {
	
	// Propiedades estáticas

	Libro[] lista = new Libro[0];

	// Funciones

	/**
	 *	Devuelve el listado de libros.
	 * 
	 *	@return	Listado de libros
	 */
	public Libro[] getLibros() {
		return this.lista;
	}
	
	/**
	 *	Devuelve el libro del listado cuyo ISBN
	 *	coincida con el proporcionado.
	 * 
	 *	@return	Libro cuyo ISBN coincide
	 */
	public Libro getLibro(String isbn) {
		for(int i = 0; i < lista.length; i++)
			if (lista[i].getIsbn().equalsIgnoreCase(isbn))
				return this.lista[i];

		return null;
	}	
	
	/**
	 *	<p>
	 *		Devuelve todos los libros del registro
	 *		que cumplan la condición proporcionada
	 *		en los argumentos.
	 *	</p>
	 *	<p>
	 *		Los argumentos permiten especificar un
	 *		criterio (autor, título o categoría) y
	 *		una clave. Si el criterio es "título" y
	 *		la clave es "Quijote", mostrará todos
	 *		los libros cuyo título contenga la
	 *		palabra "Quijote".
	 *	</p>
	 * 
	 *	@param	criterio	Criterio por el que filtrar
	 *	@param	dato		Clave a consultar en el listado
	 */
	public Libro[] buscar(Criterio criterio, String dato) {
		// Crear lista vacia.
		Libro[] coincidencias = new Libro[lista.length];
		
		// Apuntar los encuentros.
		int contador = 0;
		for(int i = 0; i < lista.length; i++) {
			boolean encuentro = false;

			// Comprobar si el criterio de filtrado se cumple.
			switch(criterio) {
				case AUTOR:
					encuentro = lista[i].getAutor().contains(dato);
					break;
				case TITULO:
					encuentro = lista[i].getTitulo().contains(dato);
					break;
				case CATEGORIA:
					encuentro = lista[i].getCategoria().name().contains(dato);
					break;
				default:
					return null;
			}
			
			// ¿Se cumple? Ponemos el resultado en la nueva lista.
			if (encuentro) {
				coincidencias[i] = lista[i];
				contador++;
			}
		}

		// Crear lista con tamaño recortado.
		Libro[] recortada = new Libro[contador];
		int posi = 0;
		
		// Llenar la lista recortada.
		for (int i = 0 ; i < coincidencias.length ; i++) {
			if (coincidencias[i] != null) {
				recortada[posi] = coincidencias[i];
				posi++;
			}
		}

		// Devolver la lista recortada.
		return recortada;
	}
	
	/**
	 *	<p>
	 *		Registra un libro en el listado.
	 *	</p>
	 *	<p>
	 *		Se deben proporcionar el título, autor,
	 *		categoría e ISBN del libro. El programa
	 *		registrará el libro siempre que no haya
	 *		otro que tenga el mismo ISBN.
	 *	</p>
	 * 
	 *	@param	titulo		Título del libro
	 *	@param	autor		Nombre de su autor
	 *	@param	categoria	Categoría del libro
	 *	@param	isbn		ISBN del libro
	 * 
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int registrar(String titulo, String autor, Categoria categoria, String isbn) {
		// Si no hay hueco en la biblioteca
		if(this.lista.length == Integer.MAX_VALUE) return 1;
		
		// Saber si el libro está repetido
		if(getLibro(isbn) != null) return 2;
		
		// Aumentar la lista de libros
		Libro[] nuevo = new Libro[this.lista.length+1];

		// Pasar los elementos registrados a una nueva lista
		for(int i = 0; i < lista.length; i++)
			nuevo[i] = this.lista[i];

		// Añadir el nuevo libro
		nuevo[this.lista.length] = new Libro(titulo, autor, categoria, isbn);
		
		// Usar la nueva lista
		this.lista = nuevo;
		
		// Finalizar accion
		return 0;
	}
	
}