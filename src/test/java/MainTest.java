import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dam.biblioteca.backend.Usuario;
import dam.biblioteca.frontend.Shell;

public class MainTest {

	// Constantes

	public static final Shell SHELL = new Shell();

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
		boolean funciona = SHELL.ejecutar(cmd) == Shell.ERR_COMANDO;
		assertEquals(funciona, valor);
	}

	private void seConecta(String nombre, String contrasena, boolean valor, boolean comprobar) {
		boolean seConecta =
			SHELL.getSesion().abrir(Usuario.getUsuario(nombre), contrasena) == 0;

		assertEquals(seConecta, valor);

		if(!comprobar) return;

		Usuario con = SHELL.getSesion().getUsuario();
		boolean estaConectado = (nombre == null && con == null) ||
			(nombre != null && con != null && nombre.equals(con.getNombre()));

		assertEquals(estaConectado, valor);
	}

	private void sesionCerrada(boolean valor) {
		boolean cerrada = SHELL.getSesion().getUsuario() == null;
		assertEquals(cerrada, valor);
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
		assertNotNull(Usuario.getUsuario(TEMP_NM));								// 'temp' existe.
		funciona("register usuario " + TEMP_NM + " prueba y", false);			// Registrar a 'temp' (no).

		funciona("remove usuario " + TEMP_NM + " " + TEMP_PW, true);			// Eliminar a 'temp'.
		assertNull(Usuario.getUsuario(TEMP_NM));								// 'temp' no existe.
		funciona("remove usuario " + ROOT_NM, false);							// Eliminar a 'root' (no).
		assertNotNull(Usuario.getUsuario(ROOT_NM));								// 'root' existe.

		funciona("set contraseña", true);										// Eliminar contraseña.
		sesionCerrada(true);													// Sesión cerrada.
		seConecta(ROOT_NM, null, true, true);									// Abrir sesión: root.

		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.

		assertNotEquals(SHELL.getSesion().cerrar(), 0);							// Cerrar sesión (no).
		funciona("help", false);												// Sesión inválida.
	}

	@Test
	public void testUsuariosNuevos() {
		seConecta(ROOT_NM, null, true, true);									// Abrir sesión: 'root'.
		funciona("register usuario " + SUDO_NM + " " + SUDO_PW + " y", true);	// Registrar a 'sudo'.
		assertNotNull(Usuario.getUsuario(SUDO_NM));								// 'sudo' existe.
		funciona("register usuario " + USER_NM + " " + USER_PW + " y", true);	// Registrar a 'user'.
		assertNotNull(Usuario.getUsuario(USER_NM));								// 'user' existe.
		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta(SUDO_NM, SUDO_PW, true, true);								// Abrir sesión: 'sudo'.
		funciona("set rol usuario " + ROOT_NM, false);							// Cambiar rol de 'root' (no).
		assertTrue(Usuario.getUsuario(ROOT_NM).isAdmin());						// Cambio no producido.
		funciona("remove usuario " + ROOT_NM, false);							// Eliminar 'root'.
		assertNotNull(Usuario.getUsuario(ROOT_NM));								// 'root' existe.

		funciona("set rol usuario " + USER_NM, true);							// Degradar a 'user'.
		assertFalse(Usuario.getUsuario(USER_NM).isAdmin());						// Cambio producido.

		funciona("set nombre prueba", true);									// Cambiar nombre.
		assertNull(Usuario.getUsuario(SUDO_NM));								// 'sudo' no existe.
		assertNotNull(Usuario.getUsuario("prueba"));							// 'prueba' existe.
		funciona("set contraseña insegura", true);								// Cambiar contraseña.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta("prueba", "insegura", true, true);							// Abrir sesión: 'prueba'.
		funciona("set nombre " + SUDO_NM, true);								// Cambiar nombre.
		assertNull(Usuario.getUsuario("prueba"));								// 'prueba' no existe.
		assertNotNull(Usuario.getUsuario(SUDO_NM));								// 'sudo' existe.
		funciona("set contraseña " + SUDO_PW, true);							// Cambiar contraseña.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta(SUDO_NM, SUDO_PW, true, true);								// Abrir sesión.
		funciona("set nombre " + USER_NM, false);								// Cambiar nombre a 'user' (no).
		assertNotEquals(SHELL.getSesion().getUsuario().getNombre(), USER_NM);	// No se llama 'user'.
		assertEquals(SHELL.getSesion().getUsuario().getNombre(), SUDO_NM);		// Se llama 'sudo'.
		funciona("set nombre", false);											// Eliminar nombre (no).
		assertEquals(SHELL.getSesion().getUsuario().getNombre(), SUDO_NM);		// Se llama 'sudo'.

		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.

		seConecta(USER_NM, USER_PW, true, true);								// Abrir sesión: 'user'.
		funciona("register usuario prueba contrasena n", false);				// Registrar a 'prueba' (no).
		assertNull(Usuario.getUsuario("prueba"));								// 'prueba' no existe.
		funciona("remove usuario " + SUDO_NM, false);							// Eliminar a 'sudo' (no).
		assertNotNull(Usuario.getUsuario(SUDO_NM));								// 'sudo' existe.
		funciona("list usuarios", false);										// Listar usuarios (no).

		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.
	}

	@Test
	public void testComandos() {
		seConecta(ROOT_NM, null, true, true);									// Abrir sesión: 'root'.

		funciona("help", true);													// Probar comandos.
		funciona("error", true);
		funciona("exit", true);

		funciona("register", false);											// 'register' erróneos.
		funciona("register usuario", false);
		funciona("register libro", false);
		funciona("register prestamo", false);

		funciona("remove", false);												// 'remove' erróneos.
		funciona("remove usuario", false);
		funciona("remove libro", false);
		funciona("remove prestamo", false);

		funciona("list usuarios", true);										// Pruebas de 'list'.
		funciona("list libros", true);
		funciona("list prestamos", true);
		funciona("list", false);

		funciona("logout", true);												// Cerrar sesión.
		sesionCerrada(true);													// Sesión cerrada.
	}
}