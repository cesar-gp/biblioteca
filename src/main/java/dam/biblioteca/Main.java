package dam.biblioteca;

/**
 *	<p>
 *		Clase principal del proyecto "Biblioteca".
 *	</p>
 * 
 *	@author Rubén Benítez Soler
 *	@author César Gutiérrez Pérez
 */
public class Main {

	// Propiedades estáticas

	private static boolean encendido = true;

	// Método principal

	private static String ayuda(boolean admin) {
		String out = "";

		if(admin) {
			out +=
				" - list\n" +
				"   Lista usuarios, libros o préstamos.\n" +
				"\n" +
				" - register\n" +
				"   [Administrador] Registra usuarios, libros o préstamos.\n" +
				"\n";
		}

		out +=
			" - help\n" +
			"   Muestra este mensaje.\n" +
			"\n" +
			" - logout\n" +
			"   Cierra la sesión actual.\n" +
			"\n" +
			" - exit\n" +
			"   Cierra el programa.";

		return out;
	}

	private static String respuesta(String msg, boolean ocultar) {
		System.out.print(msg);

		if(ocultar) return new String(System.console().readPassword());
		else return System.console().readLine();
	}

	private static Boolean stringABoolean(String respuesta, Boolean predeterminada) {
		if(respuesta.equalsIgnoreCase("y")) return true;
		else if(respuesta.equalsIgnoreCase("n")) return false;
		else if(respuesta.isEmpty() && predeterminada != null) return predeterminada;
		else return null;
	}

	private static boolean respuestaBinaria(String msg, Boolean predeterminada) {
		char yes = 'y';
		char no = 'n';
		if(predeterminada != null) {
			if(predeterminada == true) yes = 'Y';
			if(predeterminada == false) no = 'N';
		}

		String respuesta = respuesta(msg + " [" + yes + "/" + no + "] ", false);
		Boolean out = stringABoolean(respuesta, predeterminada);

		if(out == null) return respuestaBinaria(msg, predeterminada);
		else return out;
	}

	private static void ejecutar(String cmd) {
		String[] argv = cmd.split(" ");
		Usuario actual = Usuario.getUsuarioConectado();

		switch(argv[0]) {
			case "l":
			case "ls":
			case "list":
				if(!actual.isAdmin()) {
					System.out.println("Error: este comando solo puede ser ejecutado por un administrador.");
					break;
				}

				String tipo;
				if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
				else tipo = argv[1];

				switch(tipo) {
					case "u":
					case "usuario":
					case "usuarios":
						System.out.println(Listas.lista(Usuario.getUsuarios(), false));
						break;
					case "l":
					case "libro":
					case "libros":
						System.out.println(Listas.lista(Libro.getLibros(), false));
						break;
					case "p":
					case "prestamo":
					case "prestamos":
						System.out.println(Listas.lista(Prestamo.getPrestamos(), false));
						break;
					default:
						System.out.println("Error: tipo de dato desconocido.");
						System.out.println("Valores válidos: usuarios, libros, prestamos.");
						break;
				}

				break;
			case "r":
			case "reg":
			case "register":
				if(!actual.isAdmin()) {
					System.out.println("Error: este comando solo puede ser ejecutado por un administrador.");
					break;
				}

				if(argv.length < 2) tipo = respuesta("Tipo [usuario/libro/prestamo]: ", false);
				else tipo = argv[1];

				switch(tipo) {
					case "u":
					case "usuario":
						String nombre;
						if(argv.length < 3) nombre = respuesta("Nombre: ", false);
						else nombre = argv[2];

						String contrasena;
						if(argv.length < 4) contrasena = respuesta("Contraseña: ", true);
						else contrasena = argv[3];

						if(contrasena.isEmpty() || contrasena.isBlank())
							contrasena = null;

						Boolean administrador;
						if(argv.length < 5) administrador = respuestaBinaria("¿Dar permisos de administrador?", false);
						else administrador = stringABoolean(argv[4], false);

						if(administrador == null) {
							administrador = respuestaBinaria("¿Dar permisos de administrador?", false);
						}

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
				boolean administrador = actual != null && actual.isAdmin();

				System.out.println(ayuda(administrador));
				break;
			case "o":
			case "out":
			case "logout":
				Usuario.desconectar();
				break;
			case "x":
			case "ex":
			case "exit":
				encendido = false;
				break;
			default:
				System.out.println("Error: no se reconoce '" + argv[0] + "' como comando.");
				break;
		}
	}

	public static void main(String[] args) {
		System.out.println("\n--");
		System.out.println("¡Hola! Si es la primera vez que usas el programa,");
		System.out.println("usa el nombre de usuario 'root' para empezar.");
		System.out.println();
		System.out.println("El usuario 'root' no tiene contraseña, pero es muy");
		System.out.println("recomendable que le pongas una antes de empezar a");
		System.out.println("añadir libros, usuarios y préstamos.");
		System.out.println();
		System.out.println("Si tan solo quieres cerrar el programa, pon un");
		System.out.println("nombre de usuario vacío.");
		System.out.println("--");

		while(encendido) {
			if(Usuario.getUsuarioConectado() == null) {
				String nombre = respuesta("\nUsuario: ", false);
				if(nombre == null || nombre.isEmpty() || nombre.isBlank()) {
					encendido = false;
					break;
				}

				Usuario usuario = Usuario.getUsuario(nombre);

				String contrasena = null;
				if(usuario == null || usuario.tieneContrasena())
					contrasena = respuesta("\nContraseña: ", true);

				System.out.println("\n--");
				if(usuario != null && usuario.conectar(contrasena)) {
					System.out.println("¡Bienvenido a la biblioteca, " + usuario.getNombre() + "! <3");
					System.out.println("Pon 'help' para ver la lista de comandos.");
					if(usuario.isAdmin()) System.out.println("\nTienes permisos de administrador, ten cuidado al ejecutar.");
				} else {
					System.out.println("El usuario no existe o la contraseña es incorrecta :(");
				}
				System.out.println("--");

				continue;
			}

			Usuario actual = Usuario.getUsuarioConectado();

			char simbolo = '$';
			if(actual != null && actual.isAdmin()) simbolo = '#';
			ejecutar(respuesta("\n" + simbolo + " ", false));
		}
	}
}