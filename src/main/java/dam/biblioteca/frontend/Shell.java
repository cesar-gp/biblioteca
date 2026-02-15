package dam.biblioteca.frontend;

import dam.biblioteca.backend.Usuario;
import dam.biblioteca.backend.Libro;
import dam.biblioteca.backend.Prestamo;
import dam.biblioteca.backend.Sesion;
import java.util.Scanner;

/**
 *	<p>
 *		Implementación de una <em>shell</em>, un programa
 *		en el que el usuario introduce comandos y recibe
 *		una respuesta del programa.
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

	// Propiedades no estáticas

	private boolean abierta;
	private Sesion sesion;

	// Constructores

	public Shell() {
		this.abierta = false;
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

	// Funciones: código compartido entre comandos.

	private boolean comprobarPermisos() {
		if(!this.sesion.getUsuario().isAdmin()) {
			System.out.println("Error: permiso denegado.");
			return false;
		}

		return true;
	}

	private void tipoIncorrecto() {
		System.out.println("Error: tipo de dato desconocido.");
		System.out.println("Valores válidos: usuario, libro, prestamo.");
	}

	// Funciones: código ejecutable a través de comandos.

	private void list(String[] argv) {
		// ¿El usuario no es administrador? Error.
		if(!comprobarPermisos()) return;

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
				tipoIncorrecto();
				break;
		}
	}

	private void register(String[] argv) {
		// ¿El usuario no es administrador? Error.
		if(!comprobarPermisos()) return;

		// Especificar tipo a registrar, o cogerlo
		// del primer argumento si existe.
		String tipo;
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
				switch(Usuario.registrar(nombre, contrasena, administrador)) {
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
						System.out.println("Error: ha ocurrido un error desconocido al registrar al usuario.");
						break;
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
				tipoIncorrecto();
				break;
		}
	}

	private void remove(String[] argv) {
		// ¿El usuario no es administrador? Pedirle
		// confirmación para borrar su propio usuario.
		boolean propia = false;
		if(!this.sesion.getUsuario().isAdmin()) {
			propia = respuestaBinaria("Este comando eliminará tu propio usuario, ¿quieres continuar?", false);
			if(!propia) return;
		}

		// Especificar tipo a borrar:
		//  - Si no es administrador, borrará su propio usuario.
		//  - Si lo es, pedir el tipo o cogerlo del primer argumento.
		String tipo;
		if(propia) tipo = "usuario";
		else if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
		else tipo = argv[1];

		switch(tipo) {
			case "u":
			case "usuario":
				String nombre;

				if(propia) nombre = this.sesion.getUsuario().getNombre();
				else if(argv.length < 3) nombre = respuesta("Nombre: ", false);
				else nombre = argv[2];

				Usuario objetivo;
				if(propia) objetivo = this.sesion.getUsuario();
				else objetivo = Usuario.getUsuario(nombre);

				if(objetivo == null) {
					System.out.println("Error: el usuario introducido no existe.");
					break;
				}

				switch(objetivo.eliminar()) {
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
						System.out.println("Error: ha ocurrido un error desconocido al intentar eliminar el usuario.");
						break;
				}

				break;
			case "l":
			case "libro":
				break;
			case "p":
			case "prestamo":
				break;
			default:
				tipoIncorrecto();
				break;
		}
	}

	private void set(String[] argv) {
		String campo;
		if(argv.length < 2) campo = respuesta("Campo [nombre/contraseña/rol]: ", false);
		else campo = argv[1];

		String valor;

		switch(campo) {
			case "n": case "nom": case "nombre":
				if(argv.length < 3) valor = respuesta("Nuevo nombre: ", false);
				else valor = argv[2];

				if(this.sesion.getUsuario().setNombre(valor))
					System.out.println("Nombre cambiado.");
				else
					System.out.println("Error: el nombre ya está cogido.");

				break;
			case "c": case "con": case "contraseña":
				if(argv.length < 3) valor = respuesta("Nueva contraseña: ", true);
				else valor = argv[2];

				if(this.sesion.getUsuario().setContrasena(valor)) {
					System.out.println("Contraseña cambiada.");

					this.sesion.cerrar();
					System.out.println("Se ha cerrado la sesión.");
				} else {
					System.out.println("Error: ya tienes esa contraseña.");
				}
				
				break;
			case "r": case "rol":
				if(!comprobarPermisos()) return;

				if(argv.length < 3) valor = respuesta("Nuevos permisos [administrador/usuario]: ", false);
				else valor = argv[2];

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
						return;
				}

				String nombre;
				if(argv.length < 4) nombre = respuesta("Usuario: ", false);
				else nombre = argv[3];

				Usuario usuario = Usuario.getUsuario(nombre);
				if(usuario == null) {
					System.out.println("Error: el usuario '" + nombre + "' no existe.");
					return;
				}

				switch(usuario.setAdmin(newAdmin)) {
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

				break;
			default:
				System.out.println("Error: campo no reconocido.");
				System.out.println("Valores válidos: nombre, contraseña, rol.");
				break;
		}
	}

	private void logout() {
		switch(this.sesion.cerrar()) {
			case 0:
				System.out.println("Sesión cerrada.");
				break;
			case 1:
				System.out.println("Error: no hay ningún usuario conectado.");
				break;
			default:
				System.out.println("Error: ha ocurrido un error desconocido al cerrar sesión.");
				break;
		}
	}

	/**
	 *	<p>
	 *		Función del comando {@code help}.
	 *	</p>
	 *	<p>
	 *		Muestra un mensaje de ayuda con la
	 *		lista de comandos y sus descripciones.
	 *	</p>
	 */
	private void help() {
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
	}

	// Funciones: funcionalidad básica de la shell.

	/**
	 *	Recibe un comando, lo separa por argumentos y
	 *	ejecuta su función.
	 * 
	 *	@param	cmd	Comando recibido, con todos sus
	 *				argumentos
	 * 
	 *	@return	Si la shell debe seguir abierta tras
	 *			la ejecución
	 */
	public boolean ejecutar(String cmd) {
		// Separar el comando en argumentos.
		String[] argv = cmd.split(" ");

		if(this.sesion.getUsuario() == null) {
			System.out.println("Error crítico: sesión inválida.");
			return false;
		}

		// Ver qué comando quiere ejecutar el usuario.
		switch(argv[0]) {
			case "l": case "ls": case "list":
				this.list(argv);
				break;
			case "r": case "reg": case "register":
				this.register(argv);
				break;
			case "d": case "del": case "rem": case "delete": case "remove":
				this.remove(argv);
				break;
			case "h": case "?": case "help":
				this.help();
				break;
			case "s": case "set":
				this.set(argv);
				break;
			case "o": case "out": case "logout":
				this.logout();
				break;
			case "x": case "ex": case "exit":
				return false;
			default:
				System.out.println("Error: no se reconoce '" + argv[0] + "' como comando.");
				System.out.println("Pon 'help' para ver la lista de comandos.");
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
			Usuario con = this.sesion.getUsuario();

			if(con == null) {
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
						System.out.println("\nError: ha ocurrido un error desconocido al iniciar sesión.");
						break;
				}
			} else {
				// Pedir comando al usuario, ejecutar lo que responda
				// y decidir si la shell sigue abierta en función del
				// valor devuelto por la función ejecutada.
				abierta = ejecutar(respuesta("\n" + this.sesion + " ", false));
			}
		}
		
		SCANNER.close();
	}
}