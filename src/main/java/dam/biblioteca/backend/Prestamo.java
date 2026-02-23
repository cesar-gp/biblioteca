package dam.biblioteca.backend;

/**
 *	<p>
 *		Representa un préstamo de un libro
 *		a un usuario, que puede estar activo
 *		o inactivo.
 *	</p>
 *	<p>
 *		Todos los préstamos se guardan en
 *		un registro dinámico.
 *	</p>
 * 
 *	@author		César Gutiérrez Pérez
 *	@author		Rubén Benítez Soler
 *	@version	0
 */
public class Prestamo {

	// Propiedades no estáticas

	private Usuario usuario;
	private Libro libro;

	// Constructor

	public Prestamo(Usuario usuario, Libro libro) {
		this.usuario = usuario;
		this.libro = libro;
	}

	// Getters

	public Usuario getUsuario() {
		return this.usuario;
	}

	public Libro getLibro() {
		return this.libro;
	}

	// Funciones

	@Override
	public String toString() {
		return "Préstamo de '" + this.libro.getTitulo() + "' a '" + this.usuario.getNombre() + "'.";
	}
}
