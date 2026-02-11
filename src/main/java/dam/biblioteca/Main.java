package dam.biblioteca;

import java.util.Scanner;

/**
 *	<p>
 *		Clase principal del proyecto "Biblioteca".
 *	</p>
 * 
 *	@author Rubén Benítez Soler
 *	@author César Gutiérrez Pérez
 */
public class Main {

	// Constantes

	private static final Scanner SCANNER = new Scanner(System.in);

	// Propiedades estáticas

	private static boolean encendido = true;

	// Método principal

	private static void mostrarUsuarios() {
		Usuario[] reg = Usuario.getUsuarios();

		System.out.print("Usuarios: [");
		for(int i = 0; i < reg.length; i++) {
			if(i != 0) System.out.print(", ");

			Usuario actual = Usuario.getUsuarioConectado();
			if(reg[i] == actual) System.out.print("<");
			System.out.print(reg[i].getNombre());
			if(reg[i] == actual) System.out.print(">");
		}
		System.out.println("]");
	}

	private static void ejecutar(String cmd) {
		String[] argv = cmd.split(" ");

		switch(argv[0]) {
			case "exit":
				Usuario.desconectar();
				break;
			case "poweroff":
				
			default:
				System.out.println("Error: no se reconoce '" + cmd + "' como comando.");
				break;
		}
	}

	public static void main(String[] args) {
		while(encendido) {
			if(Usuario.getUsuarioConectado() == null) {
				System.out.print("Usuario: ");
				String nombre = SCANNER.nextLine();

				System.out.print("Contraseña: ");
				String contrasena = SCANNER.nextLine();

				Usuario usuario = Usuario.getUsuario(nombre);

				if(usuario != null && usuario.conectar(contrasena))
					System.out.println("\nSesión iniciada :)\n");
				else
					System.out.println("\nDatos incorrectos :(\n");

				continue;
			}

			System.out.print("> ");
			ejecutar(SCANNER.nextLine());
		}

		SCANNER.close();
	}
}