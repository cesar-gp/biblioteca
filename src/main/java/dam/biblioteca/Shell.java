package dam.biblioteca;

/**
 *	Implementación de una <em>shell</em>, un programa
 *	en el que el usuario introduce comandos y recibe
 *	una respuesta del programa.
 * 
 *	@author		Rubén Benítez Soler
 *	@author		César Gutiérrez Pérez
 *	@version	0
 */
public class Shell {

	// Propiedades no estáticas

	private boolean abierta;

	// Constructor

	public Shell() {
		this.abierta = false;
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

	// Funciones

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

		// Recibir respuesta, ocultándola si procede.
		if(ocultar) return new String(System.console().readPassword());
		else return System.console().readLine();
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
	 *			devolverá el valor {@code null}.
	 *		</li>
	 *	</ul>
	 * 
	 *	@param	in				String a convertir.
	 *	@param	predeterminada	Valor predeterminado.
	 * 
	 *	@return	Conversión de la String a Boolean.
	 */
	private Boolean stringABoolean(String in, Boolean predeterminada) {
		if(in.equalsIgnoreCase("y"))
			return true;
		else if(in.equalsIgnoreCase("n"))
			return false;
		else if(in.isEmpty() && predeterminada != null)
			return predeterminada;
		else
			return null;
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
		Boolean out = stringABoolean(respuesta, predeterminada);

		// ¿Valor inválido? Volver a preguntar. ¿Válido? Devolver.
		if(out == null) return respuestaBinaria(msg, predeterminada);
		else return out;
	}

	/**
	 *	Devuelve un mensaje de ayuda con la lista de comandos
	 *	y sus descripciones.
	 * 
	 *	@param	admin	Si el usuario que recibe el mensaje
	 *					es administrador o no.
	 * 
	 *	@return	Mensaje de ayuda.
	 */
	private static String ayuda(boolean admin) {
		// Crear texto vacío.
		String out = "";

		if(admin) {
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
				"\n";
		} else {
			// Añadir descripciones únicas para no administradores.
			out +=
				" - remove\n" +
				"   Elimina tu propio usuario.\n" +
				"\n";
		}

		// Añadir descripciones para todos los usuarios.
		out +=
			" - help\n" +
			"   Muestra este mensaje.\n" +
			"\n" +
			" - logout\n" +
			"   Cierra la sesión actual.\n" +
			"\n" +
			" - exit\n" +
			"   Cierra el programa.";

		// Devolver texto completo.
		return out;
	}

	/**
	 *	Recibe un comando, lo separa por argumentos y
	 *	ejecuta su función.
	 * 
	 *	@param	cmd	Comando recibido, con todos sus argumentos.
	 * 
	 *	@return	Si la shell debe seguir abierta tras la ejecución.
	 */
	private boolean ejecutar(String cmd) {
		// Separar el comando en argumentos.
		String[] argv = cmd.split(" ");

		// ¿Ejecución sin usuario? Cerrar la shell.
		Usuario actual = Usuario.getUsuarioConectado();
		if(actual == null) {
			System.out.println("Error: no puedes ejecutar comandos sin haber iniciado sesión.");
			System.out.println("       Por seguridad, se cerrará el programa a continuación.");
			return false;
		}

		// Ver qué comando quiere ejecutar el usuario.
		switch(argv[0]) {
			case "l":
			case "ls":
			case "list":
				// list: Listar usuarios, libros o préstamos.

				// ¿El usuario no es administrador? Error.
				if(!actual.isAdmin()) {
					System.out.println("Error: este comando solo puede ser ejecutado por un administrador.");
					break;
				}

				// Especificar tipo a listar, o cogerlo
				// del primer argumento si existe.
				String tipo;
				if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
				else tipo = argv[1];

				switch(tipo) {
					case "u":
					case "usuario":
					case "usuarios":
						// Mostrar lista de usuarios.
						System.out.println(Listas.lista(Usuario.getUsuarios(), true));
						break;
					case "l":
					case "libro":
					case "libros":
						// Mostrar lista de libros.
						System.out.println(Listas.lista(Libro.getLibros(), true));
						break;
					case "p":
					case "prestamo":
					case "prestamos":
						// Mostrar lista de préstamos.
						System.out.println(Listas.lista(Prestamo.getPrestamos(), true));
						break;
					default:
						// ¿Otro tipo? Error.
						System.out.println("Error: tipo de dato desconocido.");
						System.out.println("Valores válidos: usuarios, libros, prestamos.");
						break;
				}

				break;
			case "r":
			case "reg":
			case "register":
				// register: Registrar usuarios, libros y préstamos.

				// ¿El usuario no es administrador? Error.
				if(!actual.isAdmin()) {
					System.out.println("Error: este comando solo puede ser ejecutado por un administrador.");
					break;
				}

				// Especificar tipo a registrar, o cogerlo
				// del primer argumento si existe.
				if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
				else tipo = argv[1];

				switch(tipo) {
					case "u":
					case "usuario":
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
						else administrador = stringABoolean(argv[4], false);

						if(administrador == null) {
							administrador = respuestaBinaria("¿Dar permisos de administrador?", false);
						}

						// Intentar registrar al usuario y mostrar
						// mensaje indicando si se ha realizado la operación.
						if(Usuario.registrar(new Usuario(nombre, contrasena, administrador))) {
							System.out.print("Se ha registrado el usuario '" + nombre + "'");
							if(administrador) System.out.print(" con permisos de administrador");
							System.out.println('.');
						} else {
							System.out.println("Error: no se ha podido registrar el usuario.");
						}

						break;
					case "l":
					case "libro":
						// Registrar libro. No soportado.
						break;
					case "p":
					case "prestamo":
						// Registrar préstamo. No soportado.
						break;
					default:
						// ¿Otro tipo? Error.
						System.out.println("Error: tipo de dato desconocido.");
						System.out.println("Valores válidos: usuario, libro, prestamo.");
						break;
				}

				break;
			case "d":
			case "del":
			case "rem":
			case "delete":
			case "remove":
				// delete/remove: Eliminar usuarios, libros o préstamos.

				// ¿El usuario no es administrador? Pedirle confirmación
				// para borrar su propio usuario.
				boolean propia = false;
				if(!actual.isAdmin()) {
					propia = respuestaBinaria("Este comando eliminará tu propio usuario, ¿quieres continuar?", false);
					if(!propia) break;
				}

				// Especificar tipo a borrar:
				//  - Si no es administrador, borrará su propio usuario.
				//  - Si lo es, pedir el tipo o cogerlo del primer argumento.
				if(propia) tipo = "usuario";
				else if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
				else tipo = argv[1];

				switch(tipo) {
					case "u":
					case "usuario":
						String nombre;

						if(propia) nombre = actual.getNombre();
						else if(argv.length < 3) nombre = respuesta("Nombre: ", false);
						else nombre = argv[2];

						Usuario objetivo;
						if(propia) objetivo = actual;
						else objetivo = Usuario.getUsuario(nombre);

						if(objetivo == null) {
							System.out.println("Error: el usuario introducido no existe.");
							break;
						}

						if(objetivo.tieneContrasena()) {
							String contrasena = respuesta("Contraseña de la cuenta a eliminar: ", true);
							if(!actual.tieneContrasena(contrasena)) {
								System.out.println("Error: contraseña incorrecta.");
								break;
							}
						}

						if(Usuario.eliminar(objetivo)) {
							if(objetivo.getNombre().equalsIgnoreCase(actual.getNombre())) {
								System.out.println("Se ha eliminado tu usuario.");
								System.out.println("En consecuencia, también se ha cerrado la sesión.");
							} else {
								System.out.println("Se ha eliminado el usuario con el nombre '" + objetivo.getNombre() + "'.");
							}
						} else {
							System.out.println("Error: no se ha podido eliminar el usuario.");
						}

						break;
					case "l":
					case "libro":
						break;
					case "p":
					case "prestamo":
						break;
					default:
						System.out.println("Error: tipo de dato desconocido.");
						System.out.println("Valores válidos: usuario, libro, prestamo.");
						break;
				}

				break;
			case "h":
			case "?":
			case "help":
				System.out.println(ayuda(actual.isAdmin()));
				break;
			case "o":
			case "out":
			case "logout":
				Usuario.desconectar();
				break;
			case "x":
			case "ex":
			case "exit":
				return false;
			default:
				System.out.println("Error: no se reconoce '" + argv[0] + "' como comando.");
				break;
		}

		return true;
	}
	
	/**
	 *	Abre la shell, que pedirá textos de entrada al usuario
	 *	repetidamente hasta que este decida cerrarla.
	 */
	public void abrir() {
		abierta = true;

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
			Usuario actual = Usuario.getUsuarioConectado();
			if(actual == null) {
				String nombre = respuesta("\nUsuario: ", false);
				if(nombre == null || nombre.isEmpty() || nombre.isBlank()) {
					abierta = false;
					break;
				}

				actual = Usuario.getUsuario(nombre);

				String contrasena = null;
				if(actual == null || actual.tieneContrasena())
					contrasena = respuesta("\nContraseña: ", true);

				System.out.println("\n--");
				if(actual != null && actual.conectar(contrasena)) {
					System.out.println("¡Bienvenido a la biblioteca, " + actual.getNombre() + "! <3");
					System.out.println("Pon 'help' para ver la lista de comandos.");
					if(actual.isAdmin()) {
						System.out.println("\nTienes permisos de administrador, revisa bien");
						System.out.println("lo que escribes antes de ejecutarlo.");
					}
				} else {
					System.out.println("El usuario no existe o la contraseña es incorrecta :(");
				}
				System.out.println("--\n");

				continue;
			}

			char simbolo = '$';
			if(actual != null && actual.isAdmin()) simbolo = '#';
			abierta = ejecutar(respuesta(simbolo + " ", false));
		}
	}
}