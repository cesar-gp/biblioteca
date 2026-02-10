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

	// Método principal

	public static void main(String[] args) {
		Usuario u = new Usuario("nombre1", "contra1", true);
		Usuario v = new Usuario("nombre2", "contra", false);
		Usuario w = new Usuario("nombre3", "contr", true);
		Usuario x = new Usuario("nombre4", "cont", true);
		Usuario y = new Usuario("nombre5", "con", true);

		u.registrar();
		v.registrar();
		w.registrar();
		x.registrar();
		y.registrar();

		System.out.println("Hay " + Usuario.getUsuarios().length + " usuarios.");
		for(int i = 0; i < Usuario.getUsuarios().length; i++)
			System.out.println(" " + i + " -> " + Usuario.getUsuarios()[i].getNombre());
		
		w.eliminar();
		y.eliminar();

		System.out.println("Hay " + Usuario.getUsuarios().length + " usuarios.");
		for(int i = 0; i < Usuario.getUsuarios().length; i++)
			System.out.println(" " + i + " -> " + Usuario.getUsuarios()[i].getNombre());
	}
}