package dam.biblioteca.backend;

public class Sesion {

	// Propiedades no estáticas

	Usuario usuario;

	// Getters

	/**
	 *	Devuelve el usuario conectado a la
	 *	sesión actualmente.
	 * 
	 *	@return	Usuario conectado a la sesión
	 */
	public Usuario getUsuario() {
		return this.usuario;
	}

	// Funciones

	/**
	 *	<p>
	 *		Abre la sesión para el usuario proporcionado,
	 *		pero solo si su contraseña coincide con la
	 *		proporcionada en el segundo argumento.
	 *	</p>
	 *	<p>
	 *		Por razones de seguridad, la función devuelve
	 *		el mismo código de error siempre que el usuario
	 *		no exista o la contraseña sea incorrecta, sin
	 *		dar forma de distinguir ambas situaciones.
	 *	</p>
	 * 
	 *	@return	Código de error (0 si no hay error)
	 */
	public int abrir(Usuario usuario, String pw) {
		// ¿Hay un usuario ya conectado? Error 1.
		if(this.usuario != null) return 1;

		// ¿Usuario nulo? Error 2.
		if(usuario == null) return 2;

		// ¿Contraseña correcta? Abrir sesión. ¿No? Error 2.
		if(usuario.tieneContrasena(pw))
			this.usuario = usuario;
		else return 2;

		// Operación realizada.
		return 0;
	}

	/**
	 *	Cierra la sesión para el usuario. Si no
	 *	hay ningún usuario conectado, da error.
	 * 
	 *	@return	Código de error (0 si no hay error)
	 */
	public int cerrar() {
		// ¿No hay ningún usuario conectado? Error 1.
		if(this.usuario == null) return 1;

		// Realizar operación.
		this.usuario = null;
		return 0;
	}
}