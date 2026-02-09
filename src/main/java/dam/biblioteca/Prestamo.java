package dam.biblioteca;

public class Prestamo {

	// Propiedades estáticas

	private static Prestamo[] lista;

	// Propiedades no estáticas

	private Usuario usuario;
	private Libro libro;
	private boolean activo;

	// Constructor

	public Prestamo(Usuario usuario, Libro libro) {
		this.usuario = usuario;
		this.libro = libro;
		this.activo = true;
	}

	// Getters

	public Usuario getUsuario() {
		return this.usuario;
	}

	public Libro getLibro() {
		return this.libro;
	}

	public boolean isActivo() {
		return this.activo;
	}
}
