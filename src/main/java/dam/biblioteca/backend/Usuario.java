package dam.biblioteca.backend;

/**
 *	<p>
 *		Usuario de la biblioteca.
 *	</p>
 *	<p>
 *		Principalmente, puede tomar libros prestados
 *		y devolverlos. Además, si tiene permisos de
 *		administrador puede obtener información sobre
 *		otros usuarios y modificar los listados de
 *		libros, usuarios y préstamos.
 *	</p>
 *	
 *	@author		Rubén Benítez Soler
 *	@author		César Gutiérrez
 *	@version	0
 */
public class Usuario {

	// Propiedades no estáticas

	private String nombre;
	private String contrasena; // TODO: cifrar.
	private boolean admin;

	/**
	 *	<p>
	 *		Crea un usuario y le asigna un nombre y una
	 *		contraseña y le otorga, opcionalmente, permisos
	 *		de administrador.
	 *	</p>
	 *	<p>
	 *		El constructor está protegido. Para crear nuevos
	 *		usuarios se debe usar la función
	 *		{@link GestorUsuarios#registrar(String, String, boolean)}.
	 *		Así se evita que existan usuarios no registrados
	 *		o con nombres nulos.
	 *	</p>
	 * 
	 *	@param	nombre		Nombre del usuario
	 *	@param	contrasena	Contraseña del usuario
	 *	@param	admin		Si es administrador o no
	 */
	protected Usuario(String nombre, String contrasena, boolean admin) {
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.admin = admin;
	}

	// Getters

	/**
	 *	Devuelve el nombre de un usuario.
	 * 
	 *	@return	Nombre del usuario
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 *	Devuelve si el usuario tiene permisos de
	 *	administrador o no.
	 * 
	 *	@return	Si el usuario es administrador o no
	 */
	public boolean isAdmin() {
		return this.admin;
	}

	/**
	 *	Devuelve si el usuario tiene contraseña o no.
	 * 
	 *	@return Si el usuario tiene contraseña o no
	 */
	public boolean tieneContrasena() {
		return this.contrasena != null;
	}

	/**
	 *	Devuelve si el usuario tiene la contraseña
	 *	proporcionada como argumento o no.
	 * 
	 *	@return	Si el usuario tiene esa contraseña o no
	 */
	public boolean tieneContrasena(String contrasena) {
		return (this.contrasena == null && contrasena == null) ||
			(this.contrasena != null && this.contrasena.equals(contrasena));
	}

	// Setters

	/**
	 *	<p>
	 *		Cambia el nombre del usuario.
	 *	</p>
	 *	<p>
	 *		Esta función es insegura y está protegida.
	 *		Usa {@link GestorUsuarios#cambiarNombre(Usuario, String)}
	 *		para cambiar el nombre de un usuario.
	 *	</p>
	 * 
	 *	@param	newNombre	Nuevo nombre del usuario
	 */
	protected void setNombre(String newNombre) {
		this.nombre = newNombre;
	}

	/**
	 *	<p>
	 *		Cambia la contraseña del usuario.
	 *	</p>
	 *	<p>
	 *		Esta función es insegura y está protegida.
	 *		Usa {@link GestorUsuarios#cambiarContrasena(Usuario, String)}
	 *		para cambiar el nombre de un usuario.
	 *	</p>
	 * 
	 *	@param	newNombre	Nuevo nombre del usuario
	 */
	protected void setContrasena(String newContrasena) {
		this.contrasena = newContrasena;
	}

	protected void setAdmin(boolean newAdmin) {
		this.admin = newAdmin;
	}

	// Funciones

	/**
	 *	Devuelve el nombre del usuario y, si es
	 *	administrador, incluye un texto que lo indica.
	 * 
	 *	@return	Representación del objeto como String
	 */
	@Override
	public String toString() {
		String out = this.nombre;
		if(this.admin) out += " (administrador)";

		return out;
	}
}