import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dam.biblioteca.backend.Usuario;

public class MainTest {

	// Constantes

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
		Usuario conectado = Usuario.getUsuarioConectado();
		return (nombre == null && conectado == null) ||
				(nombre != null && conectado != null &&
				nombre.equals(conectado.getNombre()));
	}

	@Test
	public void testUsuarioRoot() {
		// Probar si se puede conectar sin contraseña.
		assertEquals(Usuario.conectar(ROOT_NM, null), 0);
		assertTrue(comprobarConexion(ROOT_NM));

		// Intentar conectarse de nuevo a la cuenta `root`
		// y certificar que el programa NO nos deja.
		assertNotEquals(Usuario.conectar(ROOT_NM, null), 0);

		// Cambiar la contraseña de `root`, y comprobar
		// que el usuario se desconecta.
		assertEquals(Usuario.cambiarContrasena(ROOT_PW), 0);
		assertTrue(comprobarConexion(null));

		// Conectarse a la cuenta de `root` con la nueva
		// contraseña y comprobar la conexión.
		assertEquals(Usuario.conectar(ROOT_NM, ROOT_PW), 0);
		assertTrue(comprobarConexion(ROOT_NM));

		// Cambiar el nombre de `root`, y comprobar
		// que el programa NO nos deja, por lo que
		// mantenemos el mismo nombre.
		assertNotEquals(Usuario.cambiarNombre("prueba"), 0);

		// Cambiar contraseña de `root` a un valor
		// de prueba y comprobar si el usuario se
		// desconecta automáticamente.
		assertEquals(Usuario.cambiarContrasena(ROOT_PW), 0);
		assertNull(Usuario.getUsuarioConectado());

		// Volver a conectarse usando una contraseña
		// incorrecta, y comprobar que NO nos deja.
		assertNotEquals(Usuario.conectar(ROOT_NM, "incorrecta"), 0);

		// Conectarse con la contraseña correcta y
		// comprobar que ahora sí nos deja.
		assertEquals(Usuario.conectar(ROOT_NM, ROOT_PW), 0);
		assertTrue(comprobarConexion(ROOT_NM));

		// Probar permisos para registrar un usuario.
		assertEquals(Usuario.registrar(TEMP_NM, TEMP_PW, false), 0);
		assertNotNull(Usuario.getUsuario(TEMP_NM));

		// Probar permisos para eliminar un usuario.
		assertEquals(Usuario.eliminar(TEMP_NM), 0);
		assertNull(Usuario.getUsuario(TEMP_NM));

		// Probar que no se puede eliminar el usuario `root`.
		assertNotEquals(Usuario.eliminar(ROOT_NM), 0);
		assertNotNull(Usuario.getUsuario(ROOT_NM));

		// Cerrar sesión
		assertEquals(Usuario.desconectar(), 0);
	}

	@Test
	public void testUsuariosNuevos() {
		// Conectarse como `root`, crear dos usuarios de prueba.
		// y cerrar sesión despueś.
		assertEquals(Usuario.conectar(ROOT_NM, null), 0);
		assertEquals(Usuario.registrar(SUDO_NM, SUDO_PW, true), 0);
		assertEquals(Usuario.registrar(USER_NM, USER_PW, false), 0);
		assertEquals(Usuario.desconectar(), 0);
		assertTrue(comprobarConexion(null));

		// Conectarse a una cuenta de administrador.
		assertNotNull(Usuario.getUsuario(SUDO_NM));
		assertEquals(Usuario.conectar(SUDO_NM, SUDO_PW), 0);
		assertTrue(comprobarConexion(SUDO_NM));

		// Cambiar su nombre y contraseña.
		assertEquals(Usuario.cambiarNombre("prueba"), 0);
		assertNull(Usuario.getUsuario(SUDO_NM));
		assertNotNull(Usuario.getUsuario("prueba"));
		assertEquals(Usuario.cambiarContrasena("insegura"), 0);
		assertTrue(comprobarConexion(null));

		// Conectarse y volverlos a cambiar.
		assertEquals(Usuario.conectar("prueba", "insegura"), 0);
		assertEquals(Usuario.cambiarNombre(SUDO_NM), 0);
		assertNull(Usuario.getUsuario("prueba"));
		assertNotNull(Usuario.getUsuario(SUDO_NM));
		assertEquals(Usuario.cambiarContrasena(SUDO_PW), 0);
		assertTrue(comprobarConexion(null));

		// Reconectarse y probar a cambiarse el nombre
		// al de otro usuario. Debe dar error.
		assertEquals(Usuario.conectar(SUDO_NM, SUDO_PW), 0);
		assertNotEquals(Usuario.cambiarNombre(USER_NM), 0);

		// Cerrar sesión
		assertEquals(Usuario.desconectar(), 0);

		// Conectarse como el usuario sin privilegios.
		assertEquals(Usuario.conectar(USER_NM, USER_PW), 0);

		// Ejecutar acciones de administrador. Debe dar error.
		assertNotEquals(Usuario.registrar("prueba", "permisos", false), 0);
		assertNotEquals(Usuario.eliminar(SUDO_NM), 0);

		// Hacer que elimine su propia cuenta.
		assertEquals(Usuario.eliminar(USER_NM), 0);
		assertNull(Usuario.getUsuario(USER_NM));

		// Intentar cerrar sesión. Debe dar error.
		assertNotEquals(Usuario.desconectar(), 0);
	}
}