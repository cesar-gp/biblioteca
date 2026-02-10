package dam.biblioteca;

public class Usuario {

	// Propiedades estáticas

	private static Usuario[] lista = new Usuario[0];

	// Propiedades no estáticas

	private String nombre;
	private String contrasena; // TODO: cifrar.
	private boolean admin;

	// Constructor

	public Usuario(String nombre, String contrasena, boolean admin) {
		// Asignar propiedades
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.admin = admin;
	}

	// Getters

	public static Usuario[] getUsuarios() {
		return lista;
	}

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

	// Funciones

	public boolean registrar() {
		// Evitar overflow.
		if(lista.length == Integer.MAX_VALUE) return false;

		// Crear copia de la lista de usuarios con un hueco extra.
		Usuario[] copia = new Usuario[lista.length + 1];

		// Copiar los valores en ella.
		for(int i = 0; i < lista.length; i++)
			copia[i] = lista[i];

		// Meter este usuario al final.
		copia[lista.length] = this;

		// Sustituir la lista original por la copia.
		lista = copia;

		// Operación realizada.
		return true;
	}

	public boolean eliminar() {
		// Buscar índice del usuario en la lista.
		int indice = -1;
		for(int i = 0; i < lista.length && indice == -1; i++)
			if(lista[i].nombre == this.nombre)
				indice = i;

		// ¿El usuario no está registrado? Error.
		if(indice == -1) return false;

		// Crear copia de la lista sin el elemento de ese indice.
		Usuario[] copia = new Usuario[lista.length - 1];
		for(int i = 0; i < indice; i++)
			copia[i] = lista[i];

		for(int i = indice; i < lista.length - 1; i++)
			copia[i] = lista[i + 1];

		// Sustituir lista original por la copia.
		lista = copia;

		// Operación realizada.
		return true;
	}
}
