package dam.biblioteca;

import dam.biblioteca.backend.GestorUsuarios;
import dam.biblioteca.enums.Categoria;
import dam.biblioteca.backend.GestorLibros;
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

	// Máximo número de argumentos en un comando.
	public static final int MAX_ARGS = 32;

	// Propiedades no estáticas

	private final Scanner scanner;
	private final GestorUsuarios gUsuarios;
	private final GestorLibros gLibros;

	private boolean abierta;
	private int codigoError;

	// Constructores

	public Shell() {
		this.scanner = new Scanner(System.in);
		this.gUsuarios = new GestorUsuarios();
		this.gLibros = new GestorLibros();

		this.abierta = false;
		this.codigoError = ERR_INICIO;
	}

	// Getters

	/**
	 *	Devuelve el gestor de usuarios de la
	 *	shell.
	 * 
	 *	@return	Gestor de usuarios de la shell
	 */
	public GestorUsuarios getGestorUsuarios() {
		return this.gUsuarios;
	}

	/**
	 *	Devuelve si la shell está abierta o no.
	 * 
	 *	@return	Si la shell está abierta o no
	 */
	public boolean isAbierta() {
		return this.abierta;
	}

	/**
	 *	Devuelve el último código de error.
	 * 
	 *	@return	Último código de error
	 */
	public int getCodigoError() {
		return this.codigoError;
	}

	// Funciones: mensajes al usuario y lectura de sus respuestas.

	/**
	 *	Imprime la representación de un objeto como String
	 *	en la pantalla, pero solo si la shell está abierta.
	 */
	private void imprimir(Object mensaje) {
		if(!this.abierta) return;
		System.out.println(mensaje);
	}

	/**
	 *	Llama a {@link #imprimir(Object)} para que imprima
	 *	un texto vacío.
	 */
	private void imprimir() {
		imprimir("");
	}

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
	 *	@param	msg		Mensaje a mostrar
	 *	@param	ocultar	Si se ocultará o no la respuesta del usuario
	 * 
	 *	@return	Respuesta del usuario
	 */
	private String respuesta(String msg, boolean ocultar) {
		if(!this.abierta) return "";

		// Mostrar mensaje.
		System.out.print(msg);

		// Leer respuesta del usuario usando System.console()
		// si es posible, porque puede ocultar los caracteres
		// escritos por el usuario.
		if(System.console() == null) {
			return scanner.nextLine();
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
	 *	@param	in				String a convertir
	 *	@param	predeterminada	Valor predeterminado
	 * 
	 *	@return	Conversión de la String a Boolean
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
	 *	@param	msg				Mensaje a mostrar
	 *	@param	predeterminada	Respuesta predeterminada
	 * 
	 *	@return	Respuesta del usuario convertida a boolean
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

		// ¿Valor inválido? Volver a preguntar.
		// Si la shell no está abierta, asumir 'false'.
		if(out == null) {
			if(!this.abierta) return false;
			return respuestaBinaria(msg, predeterminada);
		}

		// Devolver valor.
		return out;
	}

	// Funciones: código compartido entre comandos.

	/**
	 *	<p>
	 *		Muestra los elementos de una lista, cada uno
	 *		en una linea, precedidos por su índice o un
	 *		guión que actúa como viñeta de una lista.
	 *	</p>
	 *	<p>
	 *		Si la lista está compuesta por los elementos
	 *		{@code a}, {@code b} y {@code c}, se devolverá
	 *		el siguiente texto:
	 *	</p>
	 *	<code>
	 *		- a
	 *		- b
	 *		- c
	 *	</code>
	 *	<p>
	 *		Mediante el segundo argumento, se puede indicar
	 *		si la lista mostrará guiones o números junto a
	 *		sus elementos. Los números a partir del 100 se
	 *		mostrarán desajustados respecto al resto de la
	 *		lista.
	 *	</p>
	 *	<p>
	 *		Si la lista es nula o está vacía, se devolverá
	 *		un texto que indique su estado.
	 *	</p>
	 *
	 *	@param	lista	Lista a mostrar.
	 *	@return	Texto que muestra los elementos de la lista.
	 */
	public static String lista(Object[] lista, boolean ordenada) {
		if(lista == null) return "[Lista nula]";
		else if(lista.length == 0) return "[Lista vacía]";

		String out = "";
		for(int i = 0; i < lista.length; i++) {
			if(i != 0) out += "\n";

			if(ordenada) {
				if(i < 10) out += " ";
				out += i + ". ";
			} else out += "  - ";

			out += lista[i];
		}

		return out;
	}

	/**
	 *	Comprueba si el usuario tiene permisos
	 *	de administrador y, en caso negativo,
	 *	lo indica mediante un mensaje de error.
	 * 
	 *	@return	Si el usuario es administrador o no
	 */
	private boolean comprobarPermisos() {
		if(!gUsuarios.getConectado().isAdmin()) {
			imprimir("Error: permiso denegado.");
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
		imprimir("Error: tipo de dato desconocido.");
		imprimir("Valores válidos: usuario, libro, prestamo.");
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
				imprimir(lista(gUsuarios.getUsuarios(), true));
				return ERR_COMANDO;
			case "l": case "libro": case "libros":
				// Mostrar lista de libros.
				imprimir(lista(gLibros.getLibros(), true));
				return ERR_COMANDO;
			case "p": case "prestamo": case "préstamo":
			case "prestamos": case "préstamos":
				// Mostrar lista de préstamos.
				// TODO: no soportado.
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

				// Especificar si tiene permisos de administrador,
				// o coger la respuesta del cuarto argumento,
				// si existe.
				Boolean administrador;
				if(argv.length < 5) administrador = respuestaBinaria("¿Dar permisos de administrador?", false);
				else administrador = stringABoolean(argv[4], false, true);

				// Intentar registrar al usuario y mostrar
				// mensaje indicando si se ha realizado la operación.
				int err = gUsuarios.registrar(nombre, contrasena, administrador);
				switch(err) {
					case 0:
						String mensaje = "Se ha registrado el usuario '" + nombre + "'";
						if(administrador) mensaje += " con permisos de administrador";
						mensaje += '.';

						imprimir(mensaje);
						break;
					case 1:
						imprimir("Error: se ha alcanzado el máximo de usuarios.");
						break;
					case 2:
						imprimir("Error: el nombre solo puede contener letras, números, barrabajas y guiones.");
						break;
					case 3:
						imprimir("Error: el usuario '" + nombre + "' ya está registrado.");
						break;
					default:
						imprimir("Error: fallo desconocido al registrar al usuario.");
						break;
				}

				return ERR_COMANDO + err;
				
				
			case "l": case "libro":
				// Coger titulo del segundo argumento o preguntar. 
				String titulo;
				if (argv.length < 3) titulo = respuesta("Título del libro: ", true);
				else titulo = argv[2];
				
				// Coger autor del tercer argumento o preguntar.
				String autor;
				if (argv.length < 4) autor = respuesta("Autor de la obra: ", true);
				else autor = argv[3];
				
				// Coger categoria del quinto argumento o preguntar.
				// TODO: Importante. Corregir. No pasa los tests.
				Categoria categoria;
				if (argv.length < 5) {
					String resp = respuesta("Categoría de la obra: ", false);
					categoria = Categoria.valueOf(resp);
				}
				else categoria = Categoria.valueOf(argv[4]);
				
				// Coger ISBN del sexto argumento o preguntar.
				String isbn;
				if (argv.length < 6) isbn = respuesta ("ISBN de la obra: ", true);
				else isbn = argv[5];
				
				// Realizar operación.
				int errLib = gLibros.registrar(titulo, autor, categoria, isbn);
				switch (errLib) {
				case 0:
					imprimir("Se ha registrado en la biblioteca el libro: " + titulo);
					break;
				case 1:
					imprimir("Error: Se ha alcanzado el número máximo de libros.");
					break;
				case 2:
					imprimir("Error: Libro " + titulo + " ya está registrado.");
					break;
				}
				
				// TODO: el comando debe retornar `errLib`,
				// no ERR_DESCONOCIDO cuando se ha ejecutado
				// correctamente.
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
		if(!gUsuarios.getConectado().isAdmin()) {
			// Averiguar si quiere continuar con la ejecución
			// del comando. Preguntar o sacar segundo argumento.
			if(argv.length < 2) noAdmin = respuestaBinaria("Este comando eliminará tu propio usuario, ¿quieres continuar?", false);
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
				if(noAdmin) nombre = gUsuarios.getConectado().getNombre();
				else if(argv.length < 3) nombre = respuesta("Nombre: ", false);
				else nombre = argv[2];

				// Intentar eliminar el usuario
				// e informar del resultado.
				int err = gUsuarios.eliminar(nombre);
				switch(err) {
					case 0:
						imprimir("Se ha eliminado el usuario '" + nombre + "'.");
						break;
					case 1:
						imprimir("Error: el usuario no está registrado.");
						break;
					case 2:
						imprimir("Error: no se puede eliminar el usuario root.");
						break;
					default:
						imprimir("Error: fallo desconocido al intentar eliminar el usuario.");
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
				err = gUsuarios.cambiarNombre(gUsuarios.getConectado(), valor);
				switch(err) {
					case 0:
						imprimir("Nombre cambiado.");
						break;
					case 1:
						imprimir("Error: el usuario no está registrado, ¿está la shell cerrada?");
						break;
					case 2:
						imprimir("Error: el nombre ya está cogido.");
						break;
					case 3:
						imprimir("Error: el nombre solo puede contener letras, números, guiones y barrabajas.");
						break;
					default:
						imprimir("Error: fallo desconocido al cambiar el nombre.");
						break;
				}

				return ERR_COMANDO + err;
			case "c": case "con": case "contrasena": case "contraseña":
				// Sacar valor del tercer argumento
				// o de respuesta del usuario.
				if(argv.length < 3) valor = respuesta("Nueva contraseña: ", true);
				else valor = argv[2];

				// Intentar cambiar la contraseña e informar del resultado.
				err = gUsuarios.cambiarContrasena(gUsuarios.getConectado(), valor);
				switch(err) {
					case 0:
						imprimir("Contraseña cambiada.");
						break;
					case 1:
						imprimir("Error: ya tienes esa contraseña.");
						break;
					default:
						imprimir("Error: fallo desconocido al cambiar la contraseña.");
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
						imprimir("Error: rol no reconocido.");
						imprimir("Valores válidos: usuario, administrador.");
						return ERR_ARGUMENTO + 2;
				}

				// Sacar nombre del usuario afectado
				// del cuarto argumento o de respuesta
				// del usuario.
				String nombre;
				if(argv.length < 4) nombre = respuesta("Usuario: ", false);
				else nombre = argv[3];

				// Intentar cambiar el rol e informar del resultado.
				err = gUsuarios.cambiarRol(gUsuarios.getUsuario(nombre), newAdmin);
				switch(err) {
					case 0:
						String rol = "administrador";
						if(!newAdmin) rol = "usuario";

						imprimir("El usuario '" + nombre + "' ahora es " + rol + ".");
						break;
					case 1:
						imprimir("Error: no se puede modificar el rol del usuario root.");
						break;
					case 2:
						imprimir("Error: el usuario '" + nombre + "' ya tiene asignado ese rol.");
						break;
				}

				return ERR_COMANDO + err;
			default:
				// ¿Otro campo? Error.
				imprimir("Error: campo no reconocido.");
				imprimir("Valores válidos: nombre, contraseña, rol.");
				
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
			mensaje = "se ha introducido un comando o argumentos inválidos";
		else if(this.codigoError > ERR_COMANDO)
			mensaje = "error nº " + (this.codigoError - ERR_COMANDO) + " al ejecutar el comando.";
		else if(this.codigoError == ERR_COMANDO)
			mensaje = "comando ejecutado sin errores";
		else if(this.codigoError == ERR_INICIO)
			mensaje = "todavía no se ha ejecutado ningún comando";

		imprimir(this.codigoError + ": " + mensaje + ".");
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
		int err = gUsuarios.desconectar();
		switch(err) {
			case 0:
				imprimir("Sesión cerrada.");
				break;
			case 1:
				imprimir("Error: no hay ningún usuario conectado.");
				break;
			default:
				imprimir("Error: fallo desconocido al cerrar sesión.");
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

		if(gUsuarios.getConectado().isAdmin()) {
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
		imprimir(out);

		// Operación realizada.
		return ERR_COMANDO;
	}

	// Funciones: funcionalidad básica de la shell.

	public String[] separarArgumentos(String in) {
		// ¿Comando nulo? Error.
		if(in == null) return null;

		String[] argumentos = new String[MAX_ARGS];
		int len = 0;

		// Volcar argumentos en el array.
		String arg = "";
		boolean escape = false;
		boolean grupo = false;
		for(int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);

			// Caracter de escape: ignora si el
			// siguiente caracter es especial.
			if(c == '\\' && !escape) {
				escape = true;
				continue;
			}

			// Caracter de agrupación: ignora los
			// caracteres especiales hasta encontrar
			// otro caracter que cierre el grupo.
			if(c == '"' && !escape) {
				grupo = !grupo;
				continue;
			}

			// Caracter delimitador: indica el
			// paso de un argumento al siguiente.
			if(c == ' ' && !escape && !grupo) {
				argumentos[len++] = arg;
				if(len == MAX_ARGS) return null;

				arg = "";
				continue;
			}

			// Agregar caracter al argumento actual
			// y desactivar escape si está activo.
			arg += c;
			escape = false;
		}

		// ¿Grupo sin cerrar? Error.
		if(grupo) return null;

		// Meter en `argumentos` el texto que falte.
		if(!arg.isEmpty()) {
			argumentos[len++] = arg;
			if(len == MAX_ARGS) return null;
		}

		// Si el array tiene los argumentos
		// máximos, devolverla directamente.
		if(len == MAX_ARGS) return argumentos;

		// Crear array recortada y llenarla
		// con los argumentos recogidos.
		String[] out = new String[len];
		for(int i = 0; i < out.length; i++)
			out[i] = argumentos[i];

		// Devolver array recortada.
		return out;
	}

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
		// Pasar el comando a minúsculas y separarlo en argumentos.
		String[] argv = separarArgumentos(cmd);

		// ¿Comando y argumentos nulos o vacíos? Error.
		if(argv == null || argv.length == 0) {
			imprimir("Error: comando nulo, vacío o con comillas sin cerrar.");
			return ERR_ARGUMENTO;
		}

		// ¿Ningún usuario conectado? Error.
		if(gUsuarios.getConectado() == null) {
			imprimir("Error: sesión inválida.");
			return ERR_AUTENTICACION;
		}

		// Ver qué comando quiere ejecutar el usuario.
		switch(argv[0].toLowerCase()) {
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
			case "args":
				imprimir(lista(argv, true));
				return ERR_COMANDO;
			case "x": case "ex": case "exit":
				this.abierta = false;
				return ERR_COMANDO;
			default:
				imprimir("Error: no se reconoce '" + argv[0] + "' como comando.");
				imprimir("Pon 'help' para ver la lista de comandos.");
				return ERR_ARGUMENTO + 1;
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

		imprimir("\n--");
		imprimir("¡Hola! Si es la primera vez que usas el programa,");
		imprimir("pon el nombre de usuario 'root' para empezar.");
		imprimir();
		imprimir("El usuario 'root' no tiene contraseña, pero es muy");
		imprimir("recomendable que le asignes una antes de empezar a");
		imprimir("añadir libros, usuarios y préstamos.");
		imprimir();
		imprimir("Si tan solo quieres cerrar el programa, pon un");
		imprimir("nombre de usuario vacío.");
		imprimir("--");

		while(abierta) {
			if(gUsuarios.getConectado() == null) {
				// Limpiar código de error anterior.
				this.codigoError = ERR_INICIO;

				// Pedir nombre de usuario.
				String nombre = respuesta("\nUsuario: ", false);

				// Si el nombre está vacío, salir del programa.
				if(nombre.isEmpty()) {
					abierta = false;
					break;
				}

				// Intentar iniciar sesión sin contraseña.
				int err = gUsuarios.conectar(nombre, null);

				// Si hay algún error, pedir la contraseña
				// y volver a intentar el inicio de sesión.
				if(err != 0) {
					String contrasena = respuesta("Contraseña: ", true);
					err = gUsuarios.conectar(nombre, contrasena);
				}

				// Indicar resultado de la operación.
				switch(err) {
					case 0:
						imprimir("\n¡Bienvenido a la biblioteca, " + gUsuarios.getConectado().getNombre() + "! <3");
						imprimir("Pon 'help' para ver la lista de comandos.");
						if(gUsuarios.getConectado().isAdmin()) {
							imprimir("\nTienes permisos de administrador, revisa bien");
							imprimir("lo que escribes antes de ejecutarlo.");
						}
						break;
					case 1:
						imprimir("\nError: ya hay un usuario conectado.");
						break;
					case 2:
					case 3:
						imprimir("\nError: nombre o contraseña incorrectos.");
						break;
					default:
						imprimir("\nError: fallo desconocido al iniciar sesión.");
						break;
				}
			} else {
				// Pedir comando al usuario, ejecutarlo
				// y guardar el valor devuelto como
				// último código de error.
				String cmd = respuesta("\n" + gUsuarios.getPrefijo() + " ", false);
				this.codigoError = this.ejecutar(cmd);
			}
		}
		
		scanner.close();
	}
}