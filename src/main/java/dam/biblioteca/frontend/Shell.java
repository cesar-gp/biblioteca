package dam.biblioteca.frontend;

import dam.biblioteca.backend.Usuario;
import dam.biblioteca.backend.Libro;
import dam.biblioteca.backend.Prestamo;
import dam.biblioteca.backend.Sesion;
import java.util.Scanner;

/**
 *	<p>
 *		Implementación de una <em>shell</em>: un programa
 *		en el que el usuario introduce comandos y recibe
 *		una respuesta que, habitualmente, indica los
 *		cambios producidos al ejecutar el comando.
 *	</p>
 *	<p>
 *		La shell solo podrá tener una sesión abierta, y
 *		no varias a la vez.
 *	</p>
 * 
 *	@author		Rubén Benítez Soler
 *	@author		César Gutiérrez Pérez
 *	@version	0
 */
public class Shell {
	
	// Constantes
	
	private static final Scanner SCANNER = new Scanner(System.in);

	// Código de error inicial.
	public static final int ERR_INICIO = -1;

	// Rango de errores para comandos (0 - 99).
	public static final int ERR_COMANDO = 0;

	// Rango de errores para argumentos (100 - 199).
	public static final int ERR_ARGUMENTO = 100;

	// Errores de autenticación (200 - 299).
	public static final int ERR_AUTENTICACION = 200;

	// Errores relacionados con acciones del usuario (300 - 399).
	public static final int ERR_USUARIO = 300;

	// Errores desconocidos o no clasificados (400 - ...).
	public static final int ERR_DESCONOCIDO = 400;

	// Propiedades no estáticas

	private boolean abierta;
	private int codigoError;
	private Sesion sesion;

	// Constructores

	public Shell() {
		this.abierta = false;
		this.codigoError = ERR_INICIO;
		this.sesion = new Sesion();
	}

	// Getters

	/**
	 *	Devuelve si la shell está abierta o no.
	 * 
	 *	@return	Si la shell está abierta o no.
	 */
	public boolean isAbierta() {
		return this.abierta;
	}

	/**
	 *	Devuelve el último código de error.
	 * 
	 *	@return	Último código de error.
	 */
	public int getCodigoError() {
		return this.codigoError;
	}

	/**
	 *	Devuelve la sesión actual.
	 * 
	 *	@return	Sesión actual
	 */
	public Sesion getSesion() {
		return this.sesion;
	}

	// Funciones: lectura de respuestas del usuario.

	/**
	 *	<p>
	 *		Muestra un mensaje al usuario y devuelve su respuesta.
	 *	</p>
	 *	<p>
	 *		Mediante el segundo argumento, se puede escoger si la
	 *		respuesta del usuario será mostrada en el terminal o no.
	 *		Esto es especialmente útil para preguntar contraseñas.
	 *	</p>
	 * 
	 *	@param	msg		Mensaje a mostrar.
	 *	@param	ocultar	Si se ocultará o no la respuesta del usuario.
	 * 
	 *	@return	Respuesta del usuario.
	 */
	private String respuesta(String msg, boolean ocultar) {
		// Mostrar mensaje.
		System.out.print(msg);

		// Leer respuesta del usuario usando System.console()
		// si es posible, porque puede ocultar los caracteres
		// escritos por el usuario.
		if(System.console() == null) {
			return SCANNER.nextLine();
		} else {
			if(ocultar) return new String(System.console().readPassword());
			else return System.console().readLine();
		}
	}

	/**
	 *	<p>
	 *		Convierte una String a Boolean dada una respuesta
	 *		predeterminada.
	 *	</p>
	 *	<ul>
	 *		<li>
	 *			Si la String es {@code "y"} o {@code "Y"},
	 *			la función devolverá {@code true}.
	 *		</li>
	 *		<li>
	 *			Si la String es {@code "n"} o {@code "N"},
	 *			la función devolverá {@code false}.
	 *		</li>
	 *		<li>
	 *			Si la String está vacía, pero hay un
	 *			valor predeterminado, la función devolverá
	 *			ese valor predeterminado.
	 *		</li>
	 *		<li>
	 *			En cualquier otra situación, la función
	 *			devolverá el valor {@code null}, o el
	 *			valor predeterminado si el tercer argumento
	 *			tiene el valor {@code true}.
	 *		</li>
	 *	</ul>
	 * 
	 *	@param	in				String a convertir.
	 *	@param	predeterminada	Valor predeterminado.
	 * 
	 *	@return	Conversión de la String a Boolean.
	 */
	private Boolean stringABoolean(String in, Boolean predeterminada, boolean forzar) {
		if(in.equalsIgnoreCase("y"))
			return true;
		else if(in.equalsIgnoreCase("n"))
			return false;
		else if(in.isEmpty() && predeterminada != null)
			return predeterminada;
		else
			if(forzar) return predeterminada;
			else return null;
	}

	/**
	 *	<p>
	 *		Muestra un mensaje al usuario y recoge su respuesta
	 *		esperando un <em>sí</em> (la letra {@code y}) o un
	 *		<em>no</em> (la letra {@code n}).
	 *	</p>
	 *	<p>
	 *		El mensaje se mostrará junto a un indicador que
	 *		mostrará las opciones disponibles para el usuario
	 *		[{@code y}/{@code n}]. Si hay un valor predeterminado,
	 *		esa opción se mostrará en mayúsculas [{@code Y}/{@code n}].
	 *	</p>
	 *	<ul>
	 *		<li>
	 *			Si se recibe una {@code y} o una {@code n}, se
	 *			devolverá {@code true} o {@code false},
	 *			respectivamente.
	 *		</li>
	 *		<li>
	 *			Si se recibe un texto vacío y existe un valor
	 *			predeterminado, se devolverá ese valor.
	 *		</li>
	 *		<li>
	 *			Si recibe cualquier otro texto, se repetirá la
	 *			pregunta hasta obtener una respuesta válida.
	 *		</li>
	 *	</ul>
	 *	<p>
	 *		El valor predeterminado ({@code true}, {@code false} o
	 *		{@code null}) se indica en el segundo argumento.
	 *	</p>
	 * 
	 *	@param	msg				Mensaje a mostrar.
	 *	@param	predeterminada	Respuesta predeterminada.
	 * 
	 *	@return	Respuesta del usuario convertida a boolean.
	 */
	private boolean respuestaBinaria(String msg, Boolean predeterminada) {
		// Poner en mayúsculas el valor predeterminado, si existe.
		char yes = 'y';
		char no = 'n';
		if(predeterminada != null) {
			if(predeterminada == true) yes = 'Y';
			else no = 'N';
		}

		// Recibir respuesta del usuario y convertirla a Boolean.
		String respuesta = respuesta(msg + " [" + yes + "/" + no + "] ", false);
		Boolean out = stringABoolean(respuesta, predeterminada, false);

		// ¿Valor inválido? Volver a preguntar. ¿Válido? Devolver.
		if(out == null) return respuestaBinaria(msg, predeterminada);
		else return out;
	}

	// Funciones: código compartido entre comandos.

	/**
	 *	Comprueba si el usuario tiene permisos
	 *	de administrador y, en caso negativo,
	 *	lo indica mediante un mensaje de error.
	 * 
	 *	@return	Si el usuario es administrador o no.
	 */
	private boolean comprobarPermisos() {
		if(!this.sesion.getUsuario().isAdmin()) {
			System.out.println("Error: permiso denegado.");
			return false;
		}

		return true;
	}

	/**
	 *	Indica al usuario que ha escogido un tipo
	 *	de dato que no está entre las opciones
	 *	"usuario", "libro" y "prestamo".
	 */
	private void tipoIncorrecto() {
		System.out.println("Error: tipo de dato desconocido.");
		System.out.println("Valores válidos: usuario, libro, prestamo.");
	}

	// Funciones: código ejecutable a través de comandos.

	/**
	 *	<p>
	 *		Función del comando {@code list}.
	 *	</p>
	 *	<p>
	 *		Lista los usuarios, libros o préstamos
	 *		registrados en el programa. Solo puede
	 *		ser ejecutado por un administrador.
	 *	</p>
	 * 
	 *	@return	Código de error
	 */
	private int list(String[] argv) {
		// ¿El usuario no es administrador? Error.
		if(!comprobarPermisos()) return ERR_AUTENTICACION + 1;

		// Especificar tipo a listar, o cogerlo
		// del primer argumento si existe.
		String tipo;
		if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
		else tipo = argv[1];

		switch(tipo) {
			case "u": case "usuario": case "usuarios":
				// Mostrar lista de usuarios.
				System.out.println(Listas.lista(Usuario.getUsuarios(), true));
				return ERR_COMANDO;
			case "l": case "libro": case "libros":
				// Mostrar lista de libros.
				System.out.println(Listas.lista(Libro.getLibros(), true));
				return ERR_COMANDO;
			case "p": case "prestamo": case "préstamo":
			case "prestamos": case "préstamos":
				// Mostrar lista de préstamos.
				System.out.println(Listas.lista(Prestamo.getPrestamos(), true));
				return ERR_COMANDO;
			default:
				// ¿Otro tipo? Error.
				tipoIncorrecto();
				return ERR_ARGUMENTO + 1;
		}
	}

	/**
	 *	<p>
	 *		Función del comando {@code register}.
	 *	</p>
	 *	<p>
	 *		Registra usuarios, libros o préstamos.
	 *		Solo puede ser ejecutado por un administrador.
	 *	</p>
	 * 
	 *	@return	Código de error
	 */
	private int register(String[] argv) {
		// ¿El usuario no es administrador? Error.
		if(!comprobarPermisos()) return ERR_AUTENTICACION + 1;

		// Especificar tipo a registrar, o cogerlo
		// del primer argumento si existe.
		String tipo;
		if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
		else tipo = argv[1];

		switch(tipo) {
			case "u": case "usuario":
				// Especificar nombre del usuario, o cogerlo
				// del segundo argumento si existe. 
				String nombre;
				if(argv.length < 3) nombre = respuesta("Nombre: ", false);
				else nombre = argv[2];

				// Especificar contraseña del usuario, o cogerla
				// del tercer argumento si existe.
				String contrasena;
				if(argv.length < 4) contrasena = respuesta("Contraseña: ", true);
				else contrasena = argv[3];

				// Si la contraseña está vacía, asignarle un
				// valor nulo (no tiene contraseña).
				if(contrasena.isEmpty() || contrasena.isBlank())
					contrasena = null;

				// Especificar si tiene permisos de administrador,
				// o coger la respuesta del cuarto argumento,
				// si existe.
				Boolean administrador;
				if(argv.length < 5) administrador = respuestaBinaria("¿Dar permisos de administrador?", false);
				else administrador = stringABoolean(argv[4], false, true);

				// Intentar registrar al usuario y mostrar
				// mensaje indicando si se ha realizado la operación.
				int err = Usuario.registrar(nombre, contrasena, administrador);
				switch(err) {
					case 0:
						System.out.print("Se ha registrado el usuario '" + nombre + "'");
						if(administrador) System.out.print(" con permisos de administrador");
						System.out.println('.');
						break;
					case 1:
						System.out.println("Error: se ha alcanzado el máximo de usuarios.");
						break;
					case 2:
						System.out.println("Error: no se admiten nombres nulos o vacíos.");
						break;
					case 3:
						System.out.println("Error: el usuario '" + nombre + "' ya está registrado.");
						break;
					default:
						System.out.println("Error: fallo desconocido al registrar al usuario.");
						break;
				}

				return ERR_COMANDO + err;
			case "l": case "libro":
				// Registrar libro. No soportado.
				return ERR_DESCONOCIDO;
			case "p": case "prestamo": case "préstamo":
				// Registrar préstamo. No soportado.
				return ERR_DESCONOCIDO;
			default:
				// ¿Otro tipo? Error.
				tipoIncorrecto();
				return ERR_ARGUMENTO + 1;
		}
	}

	/**
	 *	<p>
	 *		Función del comando {@code remove}.
	 *	</p>
	 *	<p>
	 *		Borra usuarios, libros o préstamos. Si quien
	 *		lo ejecuta no es administrador, borra su propia
	 *		cuenta. De lo contrario, puede elegir qué
	 *		usuario, libro o préstamo quiere borrar.
	 *	</p>
	 * 
	 *	@return	Código de error
	 */
	private int remove(String[] argv) {
		// ¿El usuario no es administrador? Pedirle
		// confirmación para borrar su propio usuario.
		boolean noAdmin = false;
		if(!this.sesion.getUsuario().isAdmin()) {
			// Averiguar si quiere continuar con la ejecución
			// del comando. Preguntar o sacar segundo argumento.
			if(argv.length < 2) respuestaBinaria("Este comando eliminará tu propio usuario, ¿quieres continuar?", false);
			else noAdmin = stringABoolean(argv[1], false, true);
			
			// Detener la ejecución si no quiere continuar.
			if(!noAdmin) return ERR_USUARIO;
		}

		// Especificar tipo a borrar:
		//  - Si no es administrador, borrará su propio usuario.
		//  - Si lo es, pedir el tipo o cogerlo del primer argumento.
		String tipo;
		if(noAdmin) tipo = "usuario";
		else if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
		else tipo = argv[1];

		switch(tipo) {
			case "u": case "usuario":
				String nombre;

				// Sacar nombre del usuario a borrar.
				// ¿No es administrador? Sacar su propio nombre.
				// ¿Sí lo es? Tercer argumento o su respuesta.
				if(noAdmin) nombre = this.sesion.getUsuario().getNombre();
				else if(argv.length < 3) nombre = respuesta("Nombre: ", false);
				else nombre = argv[2];

				// Sacar usuario a borrar a partir del nombre.
				Usuario objetivo;
				if(noAdmin) objetivo = this.sesion.getUsuario();
				else objetivo = Usuario.getUsuario(nombre);

				// ¿El usuario no existe? Error.
				if(objetivo == null) {
					System.out.println("Error: el usuario introducido no existe.");
					return ERR_ARGUMENTO + 2;
				}

				// Intentar eliminar el usuario
				// e informar del resultado.
				int err = objetivo.eliminar();
				switch(err) {
					case 0:
						System.out.println("Se ha eliminado el usuario '" + objetivo.getNombre() + "'.");

						if(this.sesion.getUsuario() == objetivo) {
							this.sesion.cerrar();
							System.out.println("Se ha cerrado la sesión.");
						}

						break;
					case 1:
						System.out.println("Error: el usuario no está registrado.");
						break;
					case 2:
						System.out.println("Error: no se puede eliminar el usuario root.");
						break;
					default:
						System.out.println("Error: fallo desconocido al intentar eliminar el usuario.");
						break;
				}

				return ERR_COMANDO + err;
			case "l": case "libro":
				// No soportado.
				return ERR_DESCONOCIDO;
			case "p": case "prestamo": case "préstamo":
				// No soportado.
				return ERR_DESCONOCIDO;
			default:
				// ¿Otro tipo? Error.
				tipoIncorrecto();
				return ERR_ARGUMENTO + 1;
		}
	}

	/**
	 *	<p>
	 *		Función del comando {@code set}.
	 *	</p>
	 *	<p>
	 *		Cambia el valor de propiedades relacionadas
	 *		con el usuario, como su nombre, contraseña
	 *		y rol.
	 *	</p>
	 * 
	 *	@return	Código de error
	 */
	private int set(String[] argv) {
		// Sacar campo del segundo argumento
		// o de respuesta del usuario.
		String campo;
		if(argv.length < 2) campo = respuesta("Campo [nombre/contraseña/rol]: ", false);
		else campo = argv[1];

		// Inicializar variable con el nuevo
		// valor del campo a cambiar.
		String valor;

		// Inicializar código de error para
		// devolverlo al terminar la ejecución.
		int err;

		// Intentar cambiar el valor del cambio.
		switch(campo) {
			case "n": case "nom": case "nombre":
				// Sacar valor del tercer argumento
				// o de respuesta del usuario.
				if(argv.length < 3) valor = respuesta("Nuevo nombre: ", false);
				else valor = argv[2];

				// Intentar cambiar el nombre e informar del resultado.
				err = this.sesion.getUsuario().setNombre(valor);
				switch(err) {
					case 0:
						System.out.println("Nombre cambiado.");
						break;
					case 1:
						System.out.println("Error: el nombre ya está cogido.");
						break;
					default:
						System.out.println("Error: fallo desconocido al cambiar el nombre.");
						break;
				}

				return ERR_COMANDO + err;
			case "c": case "con": case "contrasena": case "contraseña":
				// Sacar valor del tercer argumento
				// o de respuesta del usuario.
				if(argv.length < 3) valor = respuesta("Nueva contraseña: ", true);
				else valor = argv[2];

				// Intentar cambiar la contraseña e informar del resultado.
				err = this.sesion.getUsuario().setContrasena(valor);
				switch(err) {
					case 0:
						System.out.println("Contraseña cambiada.");

						// Cerrar la sesión al cambiar la contraseña.
						this.sesion.cerrar();
						System.out.println("Se ha cerrado la sesión.");
						break;
					case 1:
						System.out.println("Error: ya tienes esa contraseña.");
						break;
					default:
						System.out.println("Error: fallo desconocido al cambiar la contraseña.");
				}
				
				return ERR_COMANDO + err;
			case "r": case "rol":
				// ¿No es administrador? Error.
				if(!comprobarPermisos()) return ERR_AUTENTICACION + 1;

				// Sacar valor del tercer argumento
				// o de respuesta del usuario.
				if(argv.length < 3) valor = respuesta("Nuevos permisos [administrador/usuario]: ", false);
				else valor = argv[2];

				// Transformar String recibida en
				// rol (usuario o administrador).
				boolean newAdmin;
				switch(valor) {
					case "a": case "admin": case "administrador":
						newAdmin = true;
						break;
					case "u": case "usuario":
						newAdmin = false;
						break;
					default:
						System.out.println("Error: rol no reconocido.");
						System.out.println("Valores válidos: usuario, administrador.");
						return ERR_ARGUMENTO + 2;
				}

				// Sacar nombre del usuario afectado
				// del cuarto argumento o de respuesta
				// del usuario.
				String nombre;
				if(argv.length < 4) nombre = respuesta("Usuario: ", false);
				else nombre = argv[3];

				// Sacar usuario a partir del nombre.
				// ¿No existe? Error.
				Usuario usuario = Usuario.getUsuario(nombre);
				if(usuario == null) {
					System.out.println("Error: el usuario '" + nombre + "' no existe.");
					return ERR_ARGUMENTO + 3;
				}

				// Intentar cambiar el rol e informar del resultado.
				err = usuario.setAdmin(newAdmin);
				switch(err) {
					case 0:
						String rol = "administrador";
						if(!newAdmin) rol = "usuario";

						System.out.println("El usuario '" + usuario.getNombre() + "' ahora es " + rol + ".");
						break;
					case 1:
						System.out.println("Error: no se puede modificar el rol del usuario root.");
						break;
					case 2:
						System.out.println("Error: el usuario '" + usuario.getNombre() + "' ya tiene asignado ese rol.");
						break;
				}

				return ERR_COMANDO + err;
			default:
				// ¿Otro campo? Error.
				System.out.println("Error: campo no reconocido.");
				System.out.println("Valores válidos: nombre, contraseña, rol.");
				
				return ERR_ARGUMENTO + 1;
		}
	}

	/**
	 *	<p>
	 *		Función del comando {@code error}.
	 *	</p>
	 *	<p>
	 *		Muestra el último código de error.
	 *	</p>
	 * 
	 *	@return	Código de error
	 */
	public int error() {
		String mensaje = "código de error no reconocido";
		if(this.codigoError >= ERR_DESCONOCIDO)
			mensaje = "error desconocido";
		else if(this.codigoError >= ERR_USUARIO)
			mensaje = "operación cancelada o detenida por el usuario";
		else if(this.codigoError >= ERR_AUTENTICACION)
			mensaje = "sesión inválida o permiso denegado";
		else if(this.codigoError >= ERR_ARGUMENTO)
			mensaje = "se ha introducido un comando o argumento inválido";
		else if(this.codigoError > ERR_COMANDO)
			mensaje = "error nº " + (this.codigoError - ERR_COMANDO) + " al ejecutar el comando.";
		else if(this.codigoError == ERR_COMANDO)
			mensaje = "comando ejecutado sin errores";
		else if(this.codigoError == ERR_INICIO)
			mensaje = "todavía no se ha ejecutado ningún comando";

		System.out.println(this.codigoError + ": " + mensaje + ".");
		return ERR_COMANDO;
	}

	/**
	 *	<p>
	 *		Función del comando {@code logout}.
	 *	</p>
	 *	<p>
	 *		Cierra la sesión del usuario.
	 *	</p>
	 * 
	 *	@return	Código de error
	 */
	private int logout() {
		// Intentar cerrar sesión e informar del resultado.
		int err = this.sesion.cerrar();
		switch(err) {
			case 0:
				System.out.println("Sesión cerrada.");
				break;
			case 1:
				System.out.println("Error: no hay ningún usuario conectado.");
				break;
			default:
				System.out.println("Error: fallo desconocido al cerrar sesión.");
				break;
		}

		// Devolver código de error del comando.
		return ERR_COMANDO + err;
	}

	/**
	 *	<p>
	 *		Función del comando {@code help}.
	 *	</p>
	 *	<p>
	 *		Muestra un mensaje de ayuda con la
	 *		lista de comandos y sus descripciones.
	 *	</p>
	 * 
	 *	@return	Código de error
	 */
	private int help() {
		// Crear texto vacío.
		String out = "";

		if(this.sesion.getUsuario().isAdmin()) {
			// Añadir descripciones únicas para administradores.
			out +=
				" - list\n" +
				"   [Administrador] Lista usuarios, libros o préstamos.\n" +
				"\n" +
				" - register\n" +
				"   [Administrador] Registra usuarios, libros o préstamos.\n" +
				"\n" +
				" - remove\n" +
				"   [Administrador] Elimina usuarios, libros o préstamos.\n" +
				"\n" +
				" - set\n" +
				"   Cambia tu nombre, tu contraseña o el rol de cualquier usuario.\n" +
				"\n";
		} else {
			// Añadir descripciones únicas para no administradores.
			out +=
				" - remove\n" +
				"   Elimina tu propio usuario.\n" +
				"\n" +
				" - set\n" +
				"   Cambia tu nombre o tu contraseña.\n" +
				"\n";
		}

		// Añadir descripciones para todos los usuarios.
		out +=
			" - error\n" +
			"   Muestra el último código de error.\n" +
			"\n" +
			" - help\n" +
			"   Muestra este mensaje.\n" +
			"\n" +
			" - logout\n" +
			"   Cierra la sesión actual.\n" +
			"\n" +
			" - exit\n" +
			"   Cierra el programa.";

		// Mostrar texto completo.
		System.out.println(out);

		// Operación realizada.
		return ERR_COMANDO;
	}

	// Funciones: funcionalidad básica de la shell.

	/**
	 *	Recibe un comando, lo separa por argumentos y
	 *	ejecuta su función.
	 * 
	 *	@param	cmd	Comando recibido, con todos sus
	 *				argumentos
	 * 
	 *	@return	Código de error
	 */
	public int ejecutar(String cmd) {
		// Separar el comando en argumentos.
		String[] argv = cmd.split(" ");

		if(this.sesion.getUsuario() == null) {
			System.out.println("Error crítico: sesión inválida.");
			return ERR_AUTENTICACION;
		}

		// Ver qué comando quiere ejecutar el usuario.
		switch(argv[0]) {
			case "l": case "ls": case "list":
				return this.list(argv);
			case "r": case "reg": case "register":
				return this.register(argv);
			case "d": case "del": case "rem": case "delete": case "remove":
				return this.remove(argv);
			case "h": case "?": case "help":
				return this.help();
			case "s": case "set":
				return this.set(argv);
			case "e": case "err": case "error":
				return this.error();
			case "o": case "out": case "logout":
				return this.logout();
			case "x": case "ex": case "exit":
				this.abierta = false;
				return ERR_COMANDO;
			default:
				System.out.println("Error: no se reconoce '" + argv[0] + "' como comando.");
				System.out.println("Pon 'help' para ver la lista de comandos.");
				return ERR_ARGUMENTO;
		}
	}
	
	/**
	 *	Abre la shell, que obligará al usuario a
	 *	iniciar sesión y, tras esto, le pedirá
	 *	comandos repetidamente hasta que decida
	 *	cerrar la sesión o el programa.
	 */
	public void abrir() {
		this.abierta = true;

		System.out.println("\n--");
		System.out.println("¡Hola! Si es la primera vez que usas el programa,");
		System.out.println("pon el nombre de usuario 'root' para empezar.");
		System.out.println();
		System.out.println("El usuario 'root' no tiene contraseña, pero es muy");
		System.out.println("recomendable que le asignes una antes de empezar a");
		System.out.println("añadir libros, usuarios y préstamos.");
		System.out.println();
		System.out.println("Si tan solo quieres cerrar el programa, pon un");
		System.out.println("nombre de usuario vacío.");
		System.out.println("--");

		while(abierta) {
			Usuario con = this.sesion.getUsuario();

			if(con == null) {
				// Limpiar código de error anterior.
				this.codigoError = ERR_INICIO;

				// Pedir nombre de usuario.
				String nombre = respuesta("\nUsuario: ", false);

				// Si el nombre está vacío, salir del programa.
				if(nombre == null || nombre.isEmpty() || nombre.isBlank()) {
					abierta = false;
					break;
				}

				// Conseguir datos del usuario con ese nombre.
				con = Usuario.getUsuario(nombre);

				// Pedir su contraseña, si procede.
				String contrasena = null;
				if(con == null || con.tieneContrasena())
					contrasena = respuesta("Contraseña: ", true);

				// En cualquier otro caso, intentar abrir sesión.
				switch(this.sesion.abrir(con, contrasena)) {
					case 0:
						System.out.println("\n¡Bienvenido a la biblioteca, " + con.getNombre() + "! <3");
						System.out.println("Pon 'help' para ver la lista de comandos.");
						if(con.isAdmin()) {
							System.out.println("\nTienes permisos de administrador, revisa bien");
							System.out.println("lo que escribes antes de ejecutarlo.");
						}
						break;
					case 1:
						System.out.println("\nError: ya hay un usuario conectado.");
						break;
					case 2:
					case 3:
						System.out.println("\nError: nombre o contraseña incorrectos.");
						break;
					default:
						System.out.println("\nError: fallo desconocido al iniciar sesión.");
						break;
				}
			} else {
				// Pedir comando al usuario, ejecutarlo
				// y guardar el valor devuelto como
				// último código de error.
				this.codigoError = this.ejecutar(respuesta("\n" + this.sesion + " ", false));
			}
		}
		
		SCANNER.close();
	}
}