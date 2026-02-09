package dam.biblioteca;

public class Usuario {

	// Propiedades estáticas

	private static Usuario[] lista;

	// Propiedades no estáticas

	private String nombre;
	private String contrasena; // TODO: cifrar.
	private boolean admin;

	// Constructor

	public Usuario(String nombre, String contrasena, boolean admin) {
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.admin = admin;
	}

	// Getters

	public String getNombre() {
		return this.nombre;
	}

	public boolean isAdmin() {
		return this.admin;
	}

	// Setters

	public boolean setNombre(String newNombre, String contrasena) {
		if(!this.contrasena.equals(contrasena)) return false;

		this.nombre = newNombre;
		return true;
	}
}
