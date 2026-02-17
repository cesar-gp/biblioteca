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

	private boolean comprobarConexion(String nombre) {
		Usuario con = SHELL.getSesion().getUsuario();
		return (nombre == null && con == null) ||
				(nombre != null && con != null &&
				nombre.equals(con.getNombre()));
	}

	@Test
	public void testUsuarioRoot() {
		// Intentar conectarse como root
		// con una contraseña incorrecta.
		assertNotEquals(SHELL.getSesion().abrir(Usuario.getUsuario(ROOT_NM), "prueba"), 0);
		assertFalse(comprobarConexion(ROOT_NM));

		// Probar conexión como root sin contraseña.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario(ROOT_NM), null), 0);
		assertTrue(comprobarConexion(ROOT_NM));

		// Intentar conectarse de nuevo a la cuenta root
		// y certificar que el programa no nos deja
		// porque ya estamos conectados.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario(ROOT_NM), null), 1);

		// Cambiar la contraseña de root, y comprobar
		// que el usuario se desconecta.
		assertEquals(SHELL.ejecutar("set contraseña " + ROOT_PW), Shell.ERR_COMANDO);
		assertTrue(comprobarConexion(null));

		// Conectarse a la cuenta de root con la nueva
		// contraseña y comprobar la conexión.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario(ROOT_NM), ROOT_PW), 0);
		assertTrue(comprobarConexion(ROOT_NM));

		// Probar permisos para registrar un usuario.
		assertEquals(SHELL.ejecutar("register usuario " + TEMP_NM + " " + TEMP_PW + " n"), Shell.ERR_COMANDO);
		assertNotNull(Usuario.getUsuario(TEMP_NM));

		// Probar permisos para eliminar un usuario.
		assertEquals(SHELL.ejecutar("remove usuario " + TEMP_NM + " " + TEMP_PW), Shell.ERR_COMANDO);
		assertNull(Usuario.getUsuario(TEMP_NM));

		// Probar que no se puede eliminar el usuario root.
		assertNotEquals(SHELL.ejecutar("remove usuario " + ROOT_NM), Shell.ERR_COMANDO);
		assertNotNull(Usuario.getUsuario(ROOT_NM));

		// Cerrar sesión
		assertEquals(SHELL.ejecutar("logout"), Shell.ERR_COMANDO);
		assertTrue(comprobarConexion(null));

		// Probar a volver a cerrar sesión. Debe dar error.
		assertNotEquals(SHELL.getSesion().cerrar(), 0);
	}

	@Test
	public void testUsuariosNuevos() {
		// Conectarse como root, crear dos usuarios de prueba.
		// y cerrar sesión después.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario(ROOT_NM), null), 0);

		assertEquals(SHELL.ejecutar("register usuario " + SUDO_NM + " " + SUDO_PW + " y"), Shell.ERR_COMANDO);
		assertNotNull(Usuario.getUsuario(SUDO_NM));

		assertEquals(SHELL.ejecutar("register usuario " + USER_NM + " " + USER_PW + " y"), Shell.ERR_COMANDO);
		assertNotNull(Usuario.getUsuario(USER_NM));

		assertEquals(SHELL.getSesion().cerrar(), 0);
		assertTrue(comprobarConexion(null));

		// Conectarse a la primera cuenta.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario(SUDO_NM), SUDO_PW), 0);
		assertTrue(comprobarConexion(SUDO_NM));

		// Intentar quitar privilegios a root. Debe dar error.
		assertNotEquals(SHELL.ejecutar("set rol usuario " + ROOT_NM), Shell.ERR_COMANDO);
		assertTrue(Usuario.getUsuario(ROOT_NM).isAdmin());

		// Intentar eliminar el usuario root. Debe dar error.
		assertNotEquals(SHELL.ejecutar("remove usuario " + ROOT_NM), Shell.ERR_COMANDO);
		assertNotNull(Usuario.getUsuario(ROOT_NM));

		// Quitar privilegios a la segunda cuenta.
		assertEquals(SHELL.ejecutar("set rol usuario " + USER_NM), Shell.ERR_COMANDO);
		assertFalse(Usuario.getUsuario(USER_NM).isAdmin());

		// Cambiar su nombre y contraseña.
		assertEquals(SHELL.ejecutar("set nombre prueba"), Shell.ERR_COMANDO);
		assertNull(Usuario.getUsuario(SUDO_NM));
		assertNotNull(Usuario.getUsuario("prueba"));
		assertEquals(SHELL.ejecutar("set contraseña insegura"), Shell.ERR_COMANDO);
		assertTrue(comprobarConexion(null));

		// Conectarse y volverlos a cambiar.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario("prueba"), "insegura"), 0);
		assertEquals(SHELL.ejecutar("set nombre " + SUDO_NM), Shell.ERR_COMANDO);
		assertNull(Usuario.getUsuario("prueba"));
		assertNotNull(Usuario.getUsuario(SUDO_NM));
		assertEquals(SHELL.ejecutar("set contraseña " + SUDO_PW), Shell.ERR_COMANDO);
		assertTrue(comprobarConexion(null));

		// Reconectarse y probar a cambiarse el nombre
		// al de otro usuario. Debe dar error.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario(SUDO_NM), SUDO_PW), 0);
		assertNotEquals(SHELL.ejecutar("set nombre " + USER_NM), Shell.ERR_COMANDO);
		assertNotEquals(SHELL.getSesion().getUsuario().getNombre(), USER_NM);
		assertEquals(SHELL.getSesion().getUsuario().getNombre(), SUDO_NM);

		// Cerrar sesión
		assertEquals(SHELL.getSesion().cerrar(), 0);

		// Conectarse como el usuario sin privilegios.
		assertEquals(SHELL.getSesion().abrir(Usuario.getUsuario(USER_NM), USER_PW), 0);

		// Ejecutar acciones de administrador. Debe dar error.
		assertNotEquals(SHELL.ejecutar("register usuario prueba contrasena n"), Shell.ERR_COMANDO);
		assertNull(Usuario.getUsuario("prueba"));

		assertNotEquals(SHELL.ejecutar("remove usuario " + SUDO_NM), Shell.ERR_COMANDO);
		assertNotNull(Usuario.getUsuario(SUDO_NM));

		assertNotEquals(SHELL.ejecutar("list usuarios"), Shell.ERR_COMANDO);

		// Cerrar sesión
		assertEquals(SHELL.getSesion().cerrar(), 0);
	}
}