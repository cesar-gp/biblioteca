package dam.biblioteca.backend;

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

	private static Usuario[] lista = new Usuario[] {
		new Usuario("root", null, true)
	};

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
	 *		El constructor es privado. Para crear nuevos
	 *		usuarios se debe usar la función
	 *		{@link #registrar(String, String, boolean)}.
	 *		Así se evita que existan usuarios no registrados.
	 *	</p>
	 * 
	 *	@param	nombre		Nombre del usuario
	 *	@param	contrasena	Contraseña del usuario
	 *	@param	admin		Si es administrador o no
	 */
	private Usuario(String nombre, String contrasena, boolean admin) {
		this.nombre = nombre;
		this.contrasena = procesarContrasena(contrasena);
		this.admin = admin;
	}

	// Getters

	/**
	 *	Devuelve la lista de usuarios.
	 * 
	 *	@return	Lista de usuarios
	 */
	public static Usuario[] getUsuarios() {
		return lista;
	}

	/**
	 *	Busca un usuario con ese nombre en la lista
	 *	de usuarios registrados. Si lo encuentra, lo
	 *	devuelve. Si no, devuelve un valor nulo.
	 *
	 *	@return	Usuario con ese nombre, si existe
	 */
	public static Usuario getUsuario(String nombre) {
		for(int i = 0; i < lista.length; i++)
			if(lista[i].nombre.equalsIgnoreCase(nombre))
				return lista[i];

		return null;
	}

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
	 *	Devuelve si el usuario está registrado en
	 *	la lista de usuarios o no.
	 *
	 *	@return	Si el usuario está registrado o no
	 */
	public boolean isRegistrado() {
		return getUsuario(this.nombre) != null;
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
	 *		Cambia el nombre de un usuario.
	 *	</p>
	 *	<p>
	 *		Si el nombre de usuario ya está cogido,
	 *		el cambio no se realizará y la función
	 *		devolverá {@code false}.
	 *	</p>
	 * 
	 *	@param	newNombre	Nuevo nombre de usuario
	 *	@return	Si se ha realizado la operación
	 */
	public boolean setNombre(String newNombre) {
		// ¿El nombre ya está cogido? Error.
		if(getUsuario(newNombre) != null) return false;

		// Realizar operación.
		this.nombre = newNombre;
		return true;
	}

	/**
	 *	Cambia la contraseña de un usuario.
	 * 
	 *	@param newContrasena Nueva contraseña
	 * 
	 *	@return	Si se ha realizado la operación
	 */
	public boolean setContrasena(String newContrasena) {
		// ¿Es la misma contraseña que antes? Error.
		if(tieneContrasena(newContrasena)) return false;

		// Realizar operación.
		this.contrasena = procesarContrasena(newContrasena);
		return true;
	}

	/**
	 *	Otorga o revoca permisos de administrador a un usuario.
	 * 
	 *	@param	newAdmin	Si tiene permisos de administrador o no
	 * 
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int setAdmin(boolean newAdmin) {
		// ¿Se está intentando modificar el
		// rol del usuario root? Error 1.
		if(lista[0] == this) return 1;

		// ¿Es el mismo rol que antes? Error 2.
		if(this.admin == newAdmin) return 2;

		// Realizar operación.
		this.admin = newAdmin;
		return 0;
	}

	// Funciones

	/**
	 *	Devuelve el mismo texto que se introduce, a no ser
	 *	que el texto esté vacío, en cuyo caso devuelve un
	 *	valor nulo.
	 * 
	 *	@return	Contraseña procesada
	 */
	private String procesarContrasena(String contrasena) {
		if(contrasena != null &&
			(contrasena.isEmpty() || contrasena.isBlank()))
			return null;

		return contrasena;
	}

	/**
	 *	<p>
	 *		Añade al usuario a la lista de usuarios registrados,
	 *		siempre y cuando:
	 *	</p>
	 *	<ol>
	 *		<li>Haya espacio para más usuarios.</li>
	 *		<li>El nombre no sea nulo ni esté vacío.</li>
	 *		<li>El usuario no esté ya registrado.</li>
	 *	</ol>
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public int registrar() {
		// ¿No caben más usuarios? Error 1.
		if(lista.length == Integer.MAX_VALUE) return 1;

		// ¿Nombre nulo o vacío? Error 2.
		if(this.nombre == null ||
			this.nombre.isEmpty() ||
			this.nombre.isBlank())
			return 2;

		// ¿El usuario ya está registrado? Error 3.
		if(getUsuario(this.nombre) != null) return 3;

		// Crear copia de la lista de usuarios con un hueco
		// extra y meter a este usuario en ese hueco final.
		Usuario[] copia = new Usuario[lista.length + 1];
		for(int i = 0; i < lista.length; i++) copia[i] = lista[i];
		copia[lista.length] = this;

		// Sustituir la lista de usuarios por la copia.
		lista = copia;

		// Operación realizada.
		return 0;
	}

	/**
	 *	<p>
	 *		Borra al usuario de la lista de usuarios registrados,
	 *		siempre y cuando:
	 *	</p>
	 *	<ol>
	 *		<li>El usuario esté registrado.</li>
	 *		<li>No se esté eliminando el usuario {@code root}</li>
	 *	</ol>
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public int eliminar() {
		// Conseguir índice del usuario en la lista
		// de usuarios registrados.
		int indice = -1;
		for(int i = 0; i < lista.length && indice == -1; i++)
			if(lista[i].getNombre().equalsIgnoreCase(this.nombre))
				indice = i;

		// ¿No está registrado? Error 1.
		if(indice == -1) return 1;

		// ¿El usuario a eliminar es 'root'? Error 2.
		if(indice == 0) return 2;

		// Crear copia de la lista sin el elemento en ese índice
		// y sustituir la lista original por la copia.
		Usuario[] copia = new Usuario[lista.length - 1];
		for(int i = 0; i < indice; i++)
			copia[i] = lista[i];

		for(int i = indice; i < lista.length - 1; i++)
			copia[i] = lista[i + 1];

		lista = copia;

		// Operación realizada.
		return 0;
	}

	/**
	 *	Crea un nuevo usuario y lo registra usando la función
	 *	{@link #registrar()}.
	 * 
	 *	@param	nombre		Nombre del usuario a registrar
	 *	@param	contrasena	Contraseña del usuario a registrar
	 *	@param	admin		Si el usuario es administrador o no
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public static int registrar(String nombre, String contrasena, boolean admin) {
		return new Usuario(nombre, contrasena, admin).registrar();
	}

	/**
	 *	Devuelve el nombre del usuario y, si es administrador,
	 *	incluye un texto que lo indica.
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