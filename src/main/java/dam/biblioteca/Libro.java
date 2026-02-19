package dam.biblioteca;
	
public class Libro {
	
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

	//Devuelve el título del libro
	public String getNombre() {
		return this.nombre;
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

}
