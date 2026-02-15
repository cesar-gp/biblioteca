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

	private static Usuario conectado = null;

	// Propiedades no estáticas

	private String nombre;
	private String contrasena; // TODO: cifrar.
	private boolean admin;

	// Constructor

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
		// Asignar propiedades
		this.nombre = nombre;
		this.contrasena = contrasena;
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
	 *	Devuelve el usuario que está conectado
	 *	en la sesión actual.
	 * 
	 *	@return	Usuario actualmente conectado
	 */
	public static Usuario getUsuarioConectado() {
		return conectado;
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

	// Funciones

	/**
	 *	<p>
	 *		Añade al usuario a la lista de usuarios registrados,
	 *		siempre y cuando no exista un usuario registrado con
	 *		el mismo nombre.
	 *	</p>
	 *	<p>
	 *		Esta función solo se completará si el usuario
	 *		conectado en el momento de su ejecución es un
	 *		administrador de la biblioteca.
	 *	</p>
	 * 
	 *	@param	nombre		Nombre del usuario a registrar
	 *	@param	contrasena	Contraseña del usuario a registrar
	 *	@param	admin		Si el usuario es administrador o no
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public static int registrar(String nombre, String contrasena, boolean admin) {
		// ¿Permiso denegado? Error 1.
		if(conectado == null || !conectado.admin) return 1;

		// ¿No caben más usuarios? Error 2.
		if(lista.length == Integer.MAX_VALUE) return 2;

		// ¿Nombre nulo o vacío? Error 3.
		if(nombre == null || nombre.isEmpty() || nombre.isBlank())
			return 3;

		// ¿El usuario ya está registrado? Error 4.
		if(Usuario.getUsuario(nombre) != null) return 4;

		// ¿Contraseña vacía? Transformar a `null`.
		if(contrasena != null && (contrasena.isEmpty() || contrasena.isBlank()))
			contrasena = null;

		// Crear copia de la lista de usuarios con un hueco extra.
		Usuario[] copia = new Usuario[lista.length + 1];

		// Copiar los valores en ella.
		for(int i = 0; i < lista.length; i++)
			copia[i] = lista[i];

		// Meter este usuario al final.
		copia[lista.length] = new Usuario(nombre, contrasena, admin);

		// Sustituir la lista original por la copia.
		lista = copia;

		// Operación realizada.
		return 0;
	}

	/**
	 *	<p>
	 *		Conecta al usuario con el nombre proporcionado
	 *		a la sesión actual.
	 *	</p>
	 *	<p>
	 *		La operación <strong>no se realizará</strong> en
	 *		ninguno de estos escenarios:
	 *	</p>
	 *	<ul>
	 *		<li>Hay un usuario ya conectado.</li>
	 *		<li>El usuario a conectar no está registrado.</li>
	 *		<li>La contraseña introducida es incorrecta.</li>
	 *	</ul>
	 *	<p>
	 *		En cualquier otro caso, se realizará sin problema.
	 *	</p>
	 *
	 *	@param	nombre		Nombre del usuario
	 *	@param	contrasena	Contraseña del usuario
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public static int conectar(String nombre, String contrasena) {
		// ¿Hay un usuario ya conectado? Error 1.
		if(conectado != null) return 1;

		// ¿Nombre nulo o vacío? Error 2.
		if(nombre == null || nombre.isEmpty() || nombre.isBlank())
			return 2;

		// ¿El usuario no está registrado? Error 3.
		Usuario meta = Usuario.getUsuario(nombre);
		if(meta == null) return 3;

		// Convertir contraseñas vacías a `null`.
		if(contrasena != null && (contrasena.isEmpty() || contrasena.isBlank()))
			contrasena = null;

		// ¿Contraseña incorrecta? Error 4.
		if(!meta.tieneContrasena(contrasena)) return 4;

		// Realizar operación.
		conectado = meta;
		return 0;
	}

	/**
	 *	<p>
	 *		Desconecta al usuario que esté actualmente conectado.
	 *	</p>
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public static int desconectar() {
		// ¿No hay ningún usuario conectado? Error 1.
		if(conectado == null) return 1;

		// Realizar operación.
		conectado = null;
		return 0;
	}

	/**
	 *	<p>
	 *		Borra al usuario de la lista de usuarios registrados,
	 *		siempre y cuando esté presente en dicha lista.
	 *	</p>
	 *	<p>
	 *		Esta función solo se completará si el usuario conectado
	 *		en el momento de su ejecución es un administrador o
	 *		el mismo usuario al que afecta el borrado de cuenta.
	 *	</p>
	 *	<p>
	 *		La función no permite eliminar al usuario root
	 *		porque esa acción podría dejar el programa en un
	 *		estado inutilizable.
	 *	</p>
	 * 
	 *	@param	nombre	Nombre del usuario a eliminar
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public static int eliminar(String nombre) {
		// ¿Nombre nulo o vacío? Error 1.
		if(nombre == null || nombre.isEmpty() || nombre.isBlank())
			return 1;

		// ¿Permiso denegado? Error 2.
		if(conectado == null ||
			(!nombre.equalsIgnoreCase(conectado.nombre) && !conectado.admin))
			return 2;

		// Conseguir índice del usuario en la lista de usuarios
		// registrados.
		int indice = -1;
		for(int i = 0; i < lista.length && indice == -1; i++)
			if(lista[i].nombre.equalsIgnoreCase(nombre))
				indice = i;

		// ¿No está registrado? Error 3.
		if(indice == -1) return 3;

		// ¿El usuario a eliminar es 'root'? Error 4.
		if(nombre.equalsIgnoreCase(lista[0].nombre)) return 4;

		// Cerrar sesión en la cuenta a eliminar, si procede.
		if(conectado.nombre.equalsIgnoreCase(nombre)) desconectar();

		// Crear copia de la lista sin el elemento de ese indice.
		Usuario[] copia = new Usuario[lista.length - 1];
		for(int i = 0; i < indice; i++)
			copia[i] = lista[i];

		for(int i = indice; i < lista.length - 1; i++)
			copia[i] = lista[i + 1];

		// Sustituir lista original por la copia.
		lista = copia;

		// Operación realizada.
		return 0;
	}

	/**
	 *	<p>
	 *		Cambia el nombre del usuario actualmente conectado.
	 *		Pueden realizar este cambio todos los usuarios menos
	 *		el usuario 'root', que mantendrá su nombre siempre.
	 *	</p>
	 *	<p>
	 *		Si el nuevo nombre ya pertenece a otro usuario,
	 *		se cancelará la operación.
	 *	</p>
	 * 
	 *	@param	newNombre	Nuevo nombre para el usuario
	 *
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public static int cambiarNombre(String newNombre) {
		// ¿Permiso denegado? Error 1.
		if(conectado == null ||
			conectado.nombre.equalsIgnoreCase(lista[0].nombre))
			return 1;

		// ¿El nuevo nombre ya está cogido? Error 2.
		if(getUsuario(newNombre) != null) return 2;

		// Realizar operación.
		conectado.nombre = newNombre;
		return 0;
	}

	/**
	 *	<p>
	 *		Cambia la contraseña del usuario actualmente conectado.
	 *	</p>
	 *	<p>
	 *		Tras cambiar la contraseña, se cerrará sesión en la cuenta.
	 *		Si quiere continuar conectado, el usuario deberá volver
	 *		a entrar en su cuenta usando la nueva contraseña.
	 *	</p>
	 * 
	 *	@param	newContrasena	Nueva contraseña para el usuario
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public static int cambiarContrasena(String newContrasena) {
		// ¿Ningún usuario conectado? Error 1.
		if(conectado == null) return 1;

		// Realizar operación.
		conectado.contrasena = newContrasena;

		// Cerrar sesión en la cuenta.
		conectado = null;

		// Operación realizada.
		return 0;
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
