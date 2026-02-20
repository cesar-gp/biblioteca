import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dam.biblioteca.backend.GestorUsuarios;
import dam.biblioteca.Shell;

public class MainTest {

	// TODO:	añadir tests para respuestaBinaria.

	// TODO:	añadir tests para caracteres de escape
	//			y comillas en los comandos cuando recuperemos
	//			el test de `register libro`.

	// Constantes

	public static final Shell SHELL = new Shell();
	public static final GestorUsuarios USUARIOS = SHELL.getGestorUsuarios();

	public static final String ROOT_NM = "root";
	public static final String ROOT_PW = "admin";

	public static final String TEMP_NM = "temp";
	public static final String TEMP_PW = "temp0r4l";

	public static final String SUDO_NM = "sudo";
	public static final String SUDO_PW = "segura20260101";

	public static final String USER_NM = "user";
	public static final String USER_PW = "u1s2e3r4";

	// Tests

	private void funciona(String cmd, boolean valor) {
		assertEquals(SHELL.ejecutar(cmd) == Shell.ERR_COMANDO, valor);
	}

	private void seConecta(String nombre, String contrasena, boolean valor, boolean comprobar) {
		assertEquals(USUARIOS.conectar(nombre, contrasena) == 0, valor);

		if(!comprobar) return;
		assertEquals(USUARIOS.isConectado(nombre), valor);
	}

	private void sesionCerrada(boolean valor) {
		assertEquals(USUARIOS.getConectado() == null, valor);
	}

	@Test
	public void testUsuarioRoot() {
		seConecta(ROOT_NM, "prueba", false, true);								// Abrir sesión (no).
		seConecta(ROOT_NM, null, true, true);									// Abrir sesión: 'root'.
		seConecta(ROOT_NM, null, false, false);									// Abrir sesión (no).
		
		funciona("set contraseña " + ROOT_PW, true);							// Cambiar contraseña.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta(ROOT_NM, ROOT_PW, true, true);								// Abrir sesión: 'root'.

		funciona("register usuario " + TEMP_NM + " " + TEMP_PW + " n", true);	// Registrar a 'temp'.
		assertTrue(USUARIOS.isRegistrado(TEMP_NM));							// 'temp' existe.
		funciona("register usuario " + TEMP_NM + " prueba y", false);			// Registrar a 'temp' (no).

		funciona("remove usuario " + TEMP_NM + " " + TEMP_PW, true);			// Eliminar a 'temp'.
		assertNull(USUARIOS.getUsuario(TEMP_NM));								// 'temp' no existe.
		funciona("remove usuario " + ROOT_NM, false);							// Eliminar a 'root' (no).
		assertTrue(USUARIOS.isRegistrado(ROOT_NM));							// 'root' existe.

		funciona("set contraseña", true);										// Eliminar contraseña.
		sesionCerrada(true);													// Sesión cerrada.
		seConecta(ROOT_NM, null, true, true);									// Abrir sesión: root.

		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.

		assertNotEquals(USUARIOS.desconectar(), 0);								// Cerrar sesión (no).
		funciona("help", false);												// Sesión inválida.
	}

	@Test
	public void testUsuariosNuevos() {
		seConecta(ROOT_NM, null, true, true);									// Abrir sesión: 'root'.
		funciona("register usuario " + SUDO_NM + " " + SUDO_PW + " y", true);	// Registrar a 'sudo'.
		assertTrue(USUARIOS.isRegistrado(SUDO_NM));							// 'sudo' existe.
		funciona("register usuario " + USER_NM + " " + USER_PW + " y", true);	// Registrar a 'user'.
		assertTrue(USUARIOS.isRegistrado(USER_NM));							// 'user' existe.
		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta(SUDO_NM, SUDO_PW, true, true);								// Abrir sesión: 'sudo'.
		funciona("set rol usuario " + ROOT_NM, false);							// Cambiar rol de 'root' (no).
		assertTrue(USUARIOS.getUsuario(ROOT_NM).isAdmin());						// Cambio no producido.
		funciona("remove usuario " + ROOT_NM, false);							// Eliminar 'root'.
		assertTrue(USUARIOS.isRegistrado(ROOT_NM));							// 'root' existe.

		funciona("set rol usuario " + USER_NM, true);							// Degradar a 'user'.
		assertFalse(USUARIOS.getUsuario(USER_NM).isAdmin());					// Cambio producido.

		funciona("set nombre prueba", true);									// Cambiar nombre.
		assertNull(USUARIOS.getUsuario(SUDO_NM));								// 'sudo' no existe.
		assertTrue(USUARIOS.isRegistrado("prueba"));							// 'prueba' existe.
		funciona("set contraseña insegura", true);								// Cambiar contraseña.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta("prueba", "insegura", true, true);							// Abrir sesión: 'prueba'.
		funciona("set nombre " + SUDO_NM, true);								// Cambiar nombre.
		assertNull(USUARIOS.getUsuario("prueba"));								// 'prueba' no existe.
		assertTrue(USUARIOS.isRegistrado(SUDO_NM));								// 'sudo' existe.
		funciona("set contraseña " + SUDO_PW, true);							// Cambiar contraseña.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta(SUDO_NM, SUDO_PW, true, true);								// Abrir sesión.
		funciona("set nombre " + USER_NM, false);								// Cambiar nombre a 'user' (no).
		assertNotEquals(USUARIOS.getConectado().getNombre(), USER_NM);			// No se llama 'user'.
		assertEquals(USUARIOS.getConectado().getNombre(), SUDO_NM);				// Se llama 'sudo'.
		funciona("set nombre", false);											// Eliminar nombre (no).
		assertEquals(USUARIOS.getConectado().getNombre(), SUDO_NM);				// Se llama 'sudo'.

		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta(USER_NM, USER_PW, true, true);								// Abrir sesión: 'user'.
		funciona("register usuario prueba contrasena n", false);				// Registrar a 'prueba' (no).
		assertNull(USUARIOS.getUsuario("prueba"));								// 'prueba' no existe.
		funciona("remove usuario " + SUDO_NM, false);							// Eliminar a 'sudo' (no).
		assertTrue(USUARIOS.isRegistrado(SUDO_NM));								// 'sudo' existe.
		funciona("list usuarios", false);										// Listar usuarios (no).
		funciona("logout", true);												// Cerrar sesión.

		sesionCerrada(true);													// Sesión cerrada.
	}

	@Test
	public void testComandos() {
		seConecta(ROOT_NM, null, true, true);									// Abrir sesión: 'root'.

		funciona("help", true);													// Probar comandos.
		funciona("\"help\"", true);
		funciona("Error", true);
		funciona("\"Error", false);
		funciona("EXIT", true);
		funciona("EXIT\"", false);

		funciona("register", false);											// 'register' erróneos.
		funciona("register usuario", false);
		funciona("register usuario \"nombre invalido\"", false);
		funciona("register usuario \"nombre\\ invalido\"", false);
		funciona("register usuario nombre\\ invalido", false);
		funciona("register libro", false);
		funciona("register prestamo", false);

		funciona("remove", false);												// 'remove' erróneos.
		funciona("remove usuario", false);
		funciona("remove libro", false);
		funciona("remove prestamo", false);

		funciona("list usuarios", true);										// Pruebas de 'list'.
		funciona("list usuarios", true);
		funciona("list libros", true);
		funciona("list prestamos", true);
		funciona("list", false);

		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.
	}
}