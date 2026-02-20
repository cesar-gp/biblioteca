package dam.biblioteca.backend;

public class GestorUsuarios {

	// Propiedades estáticas

	private Usuario conectado = null;

	private Usuario[] lista = new Usuario[] {
		new Usuario("root", null, true)
	};

	// Getters

	/**
	 *	Devuelve el usuario que está
	 *	conectado actualmente.
	 * 
	 *	@return	Usuario conectado a la sesión
	 */
	public Usuario getConectado() {
		return this.conectado;
	}

	/**
	 *	Devuelve la lista de usuarios.
	 * 
	 *	@return	Lista de usuarios
	 */
	public Usuario[] getUsuarios() {
		return lista;
	}

	/**
	 *	Busca un usuario con ese nombre en la lista
	 *	de usuarios registrados. Si lo encuentra, lo
	 *	devuelve. Si no, devuelve un valor nulo.
	 * 
	 *	@param	nombre	Nombre del usuario
	 *
	 *	@return	Usuario con ese nombre, si existe
	 */
	public Usuario getUsuario(String nombre) {
		for(int i = 0; i < this.lista.length; i++)
			if(this.lista[i].getNombre().equalsIgnoreCase(nombre))
				return this.lista[i];

		return null;
	}

	/**
	 *	<p>
	 *		Devuelve un prefijo de sesión para
	 *		mostrarlo en la linea de comandos.
	 *	</p>
	 *	<p>
	 *		Este prefijo consiste en una String que
	 *		contiene el nombre del usuario conectado
	 *		entre corchetes y un caracter que indica
	 *		si el usuario tiene permisos de
	 *		administrador.
	 *	</p>
	 * 
	 *	@return	Prefijo de sesión
	 */
	public String getPrefijo() {
		String prefijo = "[";

		if(this.conectado == null) prefijo += '?';
		else prefijo += this.conectado.getNombre();

		prefijo += "]";

		if(this.conectado == null) prefijo += '>';
		else if(this.conectado.isAdmin()) prefijo += '#';
		else prefijo += '$';

		return prefijo;
	}

	/**
	 *	Devuelve si el usuario está registrado en
	 *	la lista de usuarios o no.
	 * 
	 *	@param	nombre	Nombre del usuario
	 *
	 *	@return	Si el usuario está registrado o no
	 */
	public boolean isRegistrado(String nombre) {
		return getUsuario(nombre) != null;
	}

	/**
	 *	Devuelve si el usuario proporcionado está
	 *	conectado o no. Al proporcionar un valor
	 *	nulo, comprueba si la sesión está cerrada.
	 * 
	 *	@param	usuario	Usuario a buscar
	 * 
	 *	@return	Si el usuario está conectado o no
	 */
	public boolean isConectado(Usuario usuario) {
		return (this.getConectado() == null && usuario == null) ||
			(this.getConectado() != null && this.getConectado() == usuario);
	}

	/**
	 *	Llamada a {@link #isConectado(Usuario)}
	 *	que solo requiere el nombre del usuario.
	 * 
	 *	@param	usuario	Nombre del usuario
	 * 
	 *	@return	Si el usuario está conectado o no.
	 */
	public boolean isConectado(String usuario) {
		return this.isConectado(this.getUsuario(usuario));
	}

	/**
	 *	Verifica que un nombre no es nulo, no está
	 *	vacío y no contiene caracteres que no sean
	 *	letras, números, barrabajas o guiones.
	 * 
	 *	@param	nombre Nombre a validar
	 * 
	 *	@return	Si el nombre es válido o no
	 */
	public boolean validarNombre(String nombre) {
		// Descartar nombres nulos y vacíos.
		if(nombre == null || nombre.isEmpty()) return false;

		// Descartar nombres con caracteres que
		// no sean letras, números, '_' y '-'.
		for(int i = 0; i < nombre.length(); i++) {
			char c = nombre.charAt(i);

			if((c < 'a' || c > 'z') &&
				(c < 'A' || c > 'Z') &&
				(c < '0' || c > '9') &&
				c != '_' &&
				c != '-')
				return false;
		}

		// Nombre validado.
		return true;
	}

	/**
	 *	<p>
	 *		Verifica que una contraseña no sea nula ni
	 *		esté vacía.
	 *	</p>
	 *	<p>
	 *		<strong>Importante:</strong> Cualquier usuario
	 *		que se ponga una contraseña inválida (es decir,
	 *		vacía o nula) podrá conectarse sin que la shell
	 *		le pida ninguna contraseña.
	 *	</p>
	 * 
	 *	@param	contrasena	Contraseña a validar
	 *
	 *	@return	Si la contraseña es válida o no
	 */
	public boolean validarContrasena(String contrasena) {
		return contrasena != null && !contrasena.isEmpty();
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
	 *	@param	usuario		Usuario al que afectará
	 *						la operación
	 *	@param	newNombre	Nuevo nombre de usuario
	 * 
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int cambiarNombre(Usuario usuario, String newNombre) {
		// ¿Usuario nulo? Error 1.
		if(usuario == null) return 1;

		// ¿El nombre ya está cogido? Error 2.
		if(getUsuario(newNombre) != null) return 2;

		// ¿Nombre inválido? Error 3.
		if(!validarNombre(newNombre)) return 3;

		// Realizar operación.
		usuario.setNombre(newNombre);
		return 0;
	}

	/**
	 *	Llamada a {@link #cambiarNombre(Usuario, String)}
	 *	que solo requiere el nombre del usuario.
	 * 
	 *	@param	usuario	Nombre del usuario
	 *
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int cambiarNombre(String usuario, String newNombre) {
		return this.cambiarNombre(this.getUsuario(usuario), newNombre);
	}

	/**
	 *	Cambia la contraseña de un usuario.
	 * 
	 *	@param	usuario			Usuario al que afectará
	 *							la operación
	 *	@param	newContrasena	Nueva contraseña
	 * 
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int cambiarContrasena(Usuario usuario, String newContrasena) {
		// ¿Usuario nulo? Error 1.
		if(usuario == null) return 1;

		// ¿Es la misma contraseña que ya tiene? Error 2.
		if(usuario.tieneContrasena(newContrasena)) return 2;

		// ¿El usuario está conectado? Desconectarlo.
		if(this.conectado == usuario) this.desconectar();

		// Realizar operación.
		usuario.setContrasena(procesarContrasena(newContrasena));
		return 0;
	}

	/**
	 *	Llamada a {@link #cambiarContrasena(Usuario, String)}
	 *	que solo requiere el nombre del usuario.
	 * 
	 *	@param	usuario	Nombre del usuario
	 *
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int cambiarContrasena(String usuario, String newContrasena) {
		return this.cambiarContrasena(this.getUsuario(usuario), newContrasena);
	}

	/**
	 *	Otorga o revoca permisos de administrador a un usuario.
	 * 
	 *	@param	usuario		Usuario al que afectará
	 *						la operación
	 *	@param	newAdmin	Si tiene permisos de
	 *						administrador o no
	 * 
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int cambiarRol(Usuario usuario, boolean newAdmin) {
		// ¿Usuario nulo? Error 1.
		if(usuario == null) return 1;

		// ¿Es el mismo rol que ya tiene? Error 2.
		if(usuario.isAdmin() == newAdmin) return 2;

		// ¿Se está intentando modificar el
		// rol del usuario root? Error 3.
		if(this.lista[0] == usuario) return 3;

		// Realizar operación.
		usuario.setAdmin(newAdmin);
		return 0;
	}

	/**
	 *	Llamada a {@link #cambiarRol(Usuario, boolean)}
	 *	que solo requiere el nombre del usuario.
	 * 
	 *	@param	usuario	Nombre del usuario
	 *
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int cambiarRol(String usuario, boolean newAdmin) {
		return this.cambiarRol(this.getUsuario(usuario), newAdmin);
	}

	// Funciones

	/**
	 *	Devuelve la contraseña proporcionada si es válida.
	 *	Si no, devuelve un valor nulo.
	 * 
	 *	@return	Contraseña procesada
	 */
	private String procesarContrasena(String contrasena) {
		if(!validarContrasena(contrasena)) return null;
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
	 *	@param	nombre		Nombre del nuevo usuario
	 *	@param	contrasena	Contraseña del nuevo usuario
	 *	@param	admin		Rol del nuevo usuario
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public int registrar(String nombre, String contrasena, boolean admin) {
		// ¿No caben más usuarios? Error 1.
		if(this.lista.length == Integer.MAX_VALUE) return 1;

		// ¿Nombre inválido? Error 2.
		if(!validarNombre(nombre)) return 2;

		// ¿El usuario ya está registrado? Error 3.
		if(getUsuario(nombre) != null) return 3;

		// Crear copia de la lista de usuarios con un hueco
		// extra y meter a este usuario en ese hueco final.
		Usuario[] copia = new Usuario[this.lista.length + 1];
		for(int i = 0; i < this.lista.length; i++) copia[i] = this.lista[i];
		copia[this.lista.length] = new Usuario(nombre, procesarContrasena(contrasena), admin);

		// Sustituir la lista de usuarios por la copia.
		this.lista = copia;

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
	 *	@param	usuario		Usuario al que afectará
	 *						la operación
	 * 
	 *	@return	Código de error (0 si se ha realizado la operación)
	 */
	public int eliminar(Usuario usuario) {
		// ¿Usuario nulo? Error 1 (nulo o no registrado).
		if(usuario == null) return 1;

		// Conseguir índice del usuario en la lista
		// de usuarios registrados.
		int indice = -1;
		for(int i = 0; i < this.lista.length && indice == -1; i++)
			if(this.lista[i] == usuario) indice = i;

		// ¿No está registrado? Error 1 (nulo o no registrado).
		if(indice == -1) return 1;

		// ¿El usuario a eliminar es 'root'? Error 2.
		if(indice == 0) return 2;

		// ¿El usuario a eliminar está conectado? Desconectarlo.
		if(this.conectado == usuario) this.desconectar();

		// Crear copia de la lista sin el elemento en ese índice
		// y sustituir la lista original por la copia.
		Usuario[] copia = new Usuario[this.lista.length - 1];
		for(int i = 0; i < indice; i++)
			copia[i] = this.lista[i];

		for(int i = indice; i < this.lista.length - 1; i++)
			copia[i] = this.lista[i + 1];

		this.lista = copia;

		// Operación realizada.
		return 0;
	}

	/**
	 *	Llamada a {@link #eliminar(Usuario)}
	 *	que solo requiere el nombre del usuario.
	 * 
	 *	@param	usuario	Nombre del usuario
	 *
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int eliminar(String usuario) {
		return this.eliminar(this.getUsuario(usuario));
	}

	/**
	 *	<p>
	 *		Conecta al usuario proporcionado, pero solo
	 *		si no hay ninguno ya conectado y si su
	 *		contraseña coincide con la proporcionada
	 *		en el segundo argumento.
	 *	</p>
	 *	<p>
	 *		Por razones de seguridad, la función devuelve
	 *		el mismo código de error siempre que el usuario
	 *		no exista o la contraseña sea incorrecta, sin
	 *		dar forma de distinguir ambas situaciones.
	 *	</p>
	 * 
	 *	@param	usuario	Usuario a conectar
	 *	@param	pw		Contraseña del usuario
	 * 
	 *	@return	Código de error (0 si no hay error)
	 */
	public int conectar(Usuario usuario, String pw) {
		// ¿Hay un usuario ya conectado? Error 1.
		if(this.conectado != null) return 1;

		// ¿Usuario nulo? Error 2.
		if(usuario == null) return 2;

		// ¿Contraseña correcta? Abrir sesión. ¿No? Error 2.
		if(usuario.tieneContrasena(procesarContrasena(pw)))
			this.conectado = usuario;
		else return 2;

		// Operación realizada.
		return 0;
	}

	/**
	 *	Llamada a {@link #conectar(Usuario, String)}
	 *	que solo requiere el nombre del usuario.
	 * 
	 *	@param	usuario	Nombre del usuario
	 *
	 *	@return	Código de error (0 si no hay errores)
	 */
	public int conectar(String usuario, String pw) {
		return this.conectar(this.getUsuario(usuario), pw);
	}

	/**
	 *	Desconecta al usuario que esté conectado
	 *	actualmente. Si no hay ningún usuario
	 *	conectado, da error.
	 * 
	 *	@return	Código de error (0 si no hay error)
	 */
	public int desconectar() {
		// ¿No hay ningún usuario conectado? Error 1.
		if(this.conectado == null) return 1;

		// Realizar operación.
		this.conectado = null;
		return 0;
	}
}