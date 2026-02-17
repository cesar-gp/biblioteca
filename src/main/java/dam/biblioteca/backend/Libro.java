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

	// Propiedades estáticas

	private static Libro[] lista = new Libro[0];
	
	// Propiedades no estáticas

	private String nombre;
	private String autor;
	private Categoria categoria;
	private String isbn;
	
	// Constructor

	public Libro(String nombre, String autor, Categoria categoria, String isbn) {
		this.nombre = nombre;
		this.autor = autor;
		this.categoria = categoria;
		this.isbn = isbn;
	}

	// Getters

	public static Libro[] getLibros() {
		return lista;
	}

	public String getNombre() {
		return this.nombre;
	}

	public String getAutor() {
		return this.autor;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public String getIsbn() {
		return this.isbn;
	}
}
