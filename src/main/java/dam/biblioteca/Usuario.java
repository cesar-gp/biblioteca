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

	// Constantes

	private static Usuario ROOT = new Usuario("root", null, true);

	// Propiedades estáticas

	private static Usuario[] lista = new Usuario[] { ROOT };
	private static Usuario conectado = null;

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
	 *	Devuelve el índice del usuario en la lista
	 *	de usuarios. Si no está registrado, devuelve -1.
	 * 
	 *	@return	Índice del usuario en la lista.
	 */
	private int getIndice() {
		// Buscar índice del usuario en la lista.
		int indice = -1;
		for(int i = 0; i < lista.length && indice == -1; i++)
			if(lista[i].nombre.equalsIgnoreCase(this.nombre)) indice = i;

		// Devolver su índice (-1 si no está en la lista).
		return indice;
	}

	/**
	 *	Devuelve la lista de usuarios.
	 * 
	 *	@return	Lista de usuarios.
	 */
	public static Usuario[] getUsuarios() {
		return lista;
	}

	/**
	 *	Busca un usuario con ese nombre en la lista
	 *	de usuarios registrados. Si lo encuentra, lo
	 *	devuelve. Si no, devuelve un valor nulo.
	 *
	 *	@return	Usuario con ese nombre, si existe.
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
	 *	@return	Usuario actualmente conectado.
	 */
	public static Usuario getUsuarioConectado() {
		return conectado;
	}

	/**
	 *	Devuelve el nombre de un usuario.
	 * 
	 *	@return	Nombre del usuario.
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 *	Devuelve si el usuario tiene permisos de
	 *	administrador o no.
	 * 
	 *	@return	Si el usuario es administrador o no.
	 */
	public boolean isAdmin() {
		return this.admin;
	}

	/**
	 *	Devuelve si el usuario está registrado en
	 *	la lista de usuarios o no.
	 *
	 *	@return	Si el usuario está registrado o no.
	 */
	public boolean isRegistrado() {
		return this.getIndice() != -1;
	}

	/**
	 *	Devuelve si el usuario tiene contraseña o no.
	 * 
	 *	@return Si el usuario tiene contraseña o no.
	 */
	public boolean tieneContrasena() {
		return this.contrasena != null;
	}

	/**
	 *	Devuelve si el usuario tiene la contraseña
	 *	proporcionada como argumento o no.
	 * 
	 *	@return	Si el usuario tiene esa contraseña o no.
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
	 *	@param	in	Usuario a registrar.
	 * 
	 *	@return	Si se ha realizado la operación o no.
	 */
	public static boolean registrar(Usuario in) {
		// ¿Usuario nulo? Operación cancelada.
		if(in == null) return false;

		// ¿Permiso denegado? Operación cancelada.
		if(conectado == null || !conectado.admin) return false;

		// ¿No caben más usuarios? Operación cancelada.
		if(lista.length == Integer.MAX_VALUE) return false;

		// ¿El nombre es nulo o está vacío? Operación cancelada.
		if(in.nombre == null || in.nombre.isEmpty() || in.nombre.isBlank())
			return false;

		// ¿El usuario ya está registrado? Operación cancelada.
		for(int i = 0; i < lista.length; i++)
			if(lista[i].nombre.equalsIgnoreCase(in.nombre)) return false;

		// Crear copia de la lista de usuarios con un hueco extra.
		Usuario[] copia = new Usuario[lista.length + 1];

		// Copiar los valores en ella.
		for(int i = 0; i < lista.length; i++)
			copia[i] = lista[i];

		// Meter este usuario al final.
		copia[lista.length] = in;

		// Sustituir la lista original por la copia.
		lista = copia;

		// Operación realizada.
		return true;
	}

	/**
	 *	<p>
	 *		Desconecta al usuario que esté actualmente conectado.
	 *	</p>
	 * 
	 *	@return	Si se ha realizado la operación o no.
	 */
	public static boolean desconectar() {
		// ¿No hay ningún usuario conectado? Operación cancelada.
		if(conectado == null) return false;

		// Realizar operación.
		conectado = null;
		return true;
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
	 *		La función no permite eliminar al usuario {@link #ROOT}.
	 *	</p>
	 * 
	 *	@param	in	Usuario a eliminar.
	 * 
	 *	@return	Si se ha realizado la operación o no.
	 */
	public static boolean eliminar(Usuario in) {
		// ¿Usuario nulo? Operación cancelada.
		if(in == null) return false;

		// ¿Permiso denegado? Operación cancelada.
		if(conectado == null ||
			(!in.nombre.equalsIgnoreCase(conectado.nombre) && !conectado.admin))
			return false;

		// ¿El usuario no está registrado? Operación cancelada.
		int indice = in.getIndice();
		if(indice == -1) return false;

		// ¿El usuario a eliminar es ROOT? Operación cancelada.
		if(in.nombre.equalsIgnoreCase(ROOT.nombre)) return false;

		// Cerrar sesión en la cuenta a eliminar, si procede.
		if(conectado.nombre.equalsIgnoreCase(in.nombre)) desconectar();

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
	 *	@param	newNombre	Nuevo nombre para el usuario.
	 *
	 *	@return	Si se ha realizado la operación o no.
	 */
	public static boolean cambiarNombre(String newNombre) {
		// ¿Permiso denegado? Operación cancelada.
		if(conectado == null ||
			conectado.nombre.equalsIgnoreCase(ROOT.nombre))
			return false;

		// ¿El nuevo nombre ya está cogido? Operación cancelada.
		if(getUsuario(newNombre) != null) return false;

		// Realizar operación.
		conectado.nombre = newNombre;
		return true;
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
	 *	@param	newContrasena	Nueva contraseña para el usuario.
	 * 
	 *	@return	Si se ha realizado la operación o no.
	 */
	public static boolean cambiarContrasena(String newContrasena) {
		// ¿Ningún usuario conectado? Operación cancelada.
		if(conectado == null) return false;

		// Realizar operación.
		conectado.contrasena = newContrasena;

		// Cerrar sesión en la cuenta.
		conectado = null;

		// Operación realizada.
		return true;
	}

	/**
	 *	<p>
	 *		Conecta al usuario a la sesión actual.
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
	 *	@param	contrasena	Contraseña del usuario
	 * 
	 *	@return	Si se ha realizado la operación o no.
	 */
	public boolean conectar(String contrasena) {
		// ¿Hay un usuario ya conectado? Operación cancelada.
		if(conectado != null) return false;

		// ¿El usuario no está registrado? Operación cancelada.
		if(!this.isRegistrado()) return false;

		// ¿Contraseña incorrecta? Operación cancelada.
		if(!this.tieneContrasena(contrasena)) return false;

		// Realizar operación.
		conectado = this;
		return true;
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
