package dam.biblioteca.backend;

import dam.biblioteca.enums.Categoria;

/**
 *	<p>
 *		Representa un libro de la biblioteca.
 *	</p>
 *	<p>
 *		Todos los libros se guardan en un
 *		registro dinámico.
 *	</p>
 * 
 *	@author		César Gutiérrez Pérez
 *	@author		Rubén Benítez Soler
 *	@version	0
 */
public class Libro {
	
	// Propiedades no estáticas

	private String titulo;
	private String autor;
	private Categoria categoria;
	private String isbn;
	
	// Constructor

	public Libro(String nombre, String autor, Categoria categoria, String isbn) {
		this.titulo = nombre;
		this.autor = autor;
		this.categoria = categoria;
		this.isbn = isbn;
	}

	//Devuelve el título del libro
	public String getTitulo() {
		return this.titulo;
	}
	//Devuelve quien es el autor del libro
	public String getAutor() {
		return this.autor;
	}
	//Devuelve la categoría a la que pertenece el libro
	public Categoria getCategoria() {
		return this.categoria;
	}
	//Devuelve el ISBN propio del libro
	public String getIsbn() {
		return this.isbn;
	}

	@Override
	public String toString() {
		return this.titulo;
	}
	
}
