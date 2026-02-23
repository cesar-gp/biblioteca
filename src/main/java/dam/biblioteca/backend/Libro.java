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

	/**
	 *	<p>
	 *		Crea un libro con un nombre, autor,
	 *		categoría e ISBN.
	 *	</p>
	 *	<p>
	 *		El constructor está protegido para
	 *		evitar la creación de libros sin registrar.
	 *		Para crear nuevos libros de forma segura
	 *		se debe usar {@link GestorLibros#registrar}.
	 *	</p>
	 * 
	 *	@param	nombre		Nombre del libro
	 *	@param	autor		Nombre de su autor
	 *	@param	categoria	Categoría del libro
	 *	@param	isbn		ISBN del libro
	 */
	protected Libro(String nombre, String autor, Categoria categoria, String isbn) {
		this.titulo = nombre;
		this.autor = autor;
		this.categoria = categoria;
		this.isbn = isbn;
	}

	/**
	 *	Devuelve el título del libro.
	 * 
	 *	@return	Título del libro
	 */
	public String getTitulo() {
		return this.titulo;
	}
	
	/**
	 *	Devuelve el autor del libro.
	 * 
	 *	@return	Autor del libro
	 */
	public String getAutor() {
		return this.autor;
	}
	
	/**
	 *	Devuelve la categoría del libro.
	 * 
	 *	@return	Categoría del libro
	 */
	public Categoria getCategoria() {
		return this.categoria;
	}
	
	/**
	 *	Devuelve el ISBN del libro.
	 * 
	 *	@return	ISBN del libro
	 */
	public String getIsbn() {
		return this.isbn;
	}

	/**
	 *	Devuelve una representación del
	 *	libro como String, que incluye el
	 *	autor, el título, la categoría
	 *	y el ISBN del mismo.
	 * 
	 *	@return	Representación del libro
	 *			como String
	 */
	@Override
	public String toString() {
		return this.titulo + ", de " + this.autor
			+ " (" + this.categoria + ") ["
			+ this.isbn + "]";
	}
	
}
