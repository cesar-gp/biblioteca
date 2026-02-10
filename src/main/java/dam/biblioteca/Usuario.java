package dam.biblioteca;

/**
 *	<p>
 *		Usuario de la biblioteca.
 *	</p>
 *	<p>
 *		Principalmente, puede tomar libros prestados y devolverlos.
 *		Además, si tiene permisos de administrador puede obtener
 *		información sobre otros usuarios y modificar los listados
 *		de libros y usuarios.
 *	</p>
 *	
 *	@author		Rubén Benítez Soler
 *	@author		César Gutiérrez
 *	@version	0
 */
public class Usuario {

	// Propiedades estáticas

	private static Usuario[] lista = new Usuario[0];

	// Propiedades no estáticas

	private String nombre;
	private String contrasena; // TODO: cifrar.
	private boolean admin;

	// Constructor

	/**
	 *	Crea un usuario y le asigna un nombre y una contraseña y
	 *	le otorga, opcionalmente, permisos de administrador.
	 * 
	 *	@param	nombre		Nombre del usuario
	 *	@param	contrasena	Contraseña del usuario
	 *	@param	admin		Si es administrador o no
	 */
	public Usuario(String nombre, String contrasena, boolean admin) {
		// Asignar propiedades
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.admin = admin;
	}

	// Getters

	/**
	 *	Devuelve la lista de usuarios.
	 */
	public static Usuario[] getUsuarios() {
		return lista;
	}

	/**
	 *	Devuelve el nombre de un usuario.
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 *	Devuelve si el usuario tiene permisos de administrador o no.
	 */
	public boolean isAdmin() {
		return this.admin;
	}

	// Setters

	/**
	 *	Cambia el nombre de un usuario al valor introducido,
	 *	siempre y cuando la contraseña del usuario coincida
	 *	con la que se recibe como argumento.
	 * 
	 *	@param	newNombre	Nuevo nombre para el usuario.
	 *	@param	contrasena	Contraseña del usuario.
	 *
	 *	@return	Si se ha realizado la operación o no.
	 */
	public boolean setNombre(String newNombre, String contrasena) {
		// ¿Contraseña incorrecta? Operación cancelada.
		if(!this.contrasena.equals(contrasena)) return false;

		// Realizar operación.
		this.nombre = newNombre;
		return true;
	}

	// Funciones

	/**
	 *	Añade al usuario a la lista de usuarios registrados,
	 *	siempre y cuando no exista un usuario registrado con
	 *	el mismo nombre.
	 * 
	 *	@return	Si se ha realizado la operación o no.
	 */
	public boolean registrar() {
		// ¿No caben más usuarios? Operación cancelada.
		if(lista.length == Integer.MAX_VALUE) return false;

		// ¿El usuario ya está registrado? Operación cancelada.
		for(int i = 0; i < lista.length; i++)
			if(lista[i].nombre == this.nombre) return false;

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

	/**
	 *	Borra al usuario de la lista de usuarios registrados,
	 *	siempre y cuando esté presente en dicha lista.
	 * 
	 *	@return	Si se ha realizado la operación o no.
	 */
	public boolean eliminar() {
		// Buscar índice del usuario en la lista.
		int indice = -1;
		for(int i = 0; i < lista.length && indice == -1; i++)
			if(lista[i].nombre == this.nombre)
				indice = i;

		// ¿El usuario no está registrado? Operación cancelada.
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
