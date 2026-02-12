package dam.biblioteca;

/**
 *	<p>
 *		Clase que recopila funciones estáticas útiles
 *		para mostrar listas en un terminal.
 *	</p>
 *	<p>
 *		No se pueden crear instancias de la clase ni
 *		crear ninguna subclase que la extienda.
 *	</p>
 * 
 *	@author		Rubén Benítez Soler
 *	@author		César Gutiérrez Pérez
 *	@version	0
 */
public final class Listas {

	// Constructor

	/**
	 *	Constructor privado que impide que se cree
	 *	ninguna instancia de la clase.
	 */
	private Listas() {}

	// Funciones

	/**
	 *	<p>
	 *		Función privada que comprueba si una lista
	 *		tiene elementos.
	 *	</p>
	 *	<ul>
	 *		<li>
	 *			Si la lista es nula, devuelve un texto
	 *			que indica que la lista no está inicializada.
	 *		</li>
	 *		<li>
	 *			Si la lista está vacía, devuelve un texto
	 *			que indica que lo está.
	 *		</li>
	 *		<li>
	 *			En cualquier otro caso, devuelve {@code null}.
	 *		</li>
	 *	</ul>
	 * 
	 *	@param	lista	Lista a examinar.
	 *	@return	Texto que informa sobre el estado de la lista.
	 */
	private static String comprobarListaVacia(Object[] lista) {
		if(lista == null) return "[Lista no inicializada]";
		if(lista.length == 0) return "[Lista vacía]";

		return null;
	}

	/**
	 *	<p>
	 *		Enumera los elementos de una lista.
	 *	</p>
	 *	<p>
	 *		Si la lista está compuesta por los elementos
	 *		{@code a}, {@code b} y {@code c}, se devolverá
	 *		el texto {@code "a, b, c."}.
	 *	</p>
	 *	<p>
	 *		Si la lista es nula o está vacía, se devolverá
	 *		un texto que indique su estado.
	 *	</p>
	 * 
	 *	@param	lista	Lista a enumerar.
	 *	@return	Texto que enumera los elementos de la lista.
	 */
	public static String enumeracion(Object[] lista) {
		String vacia = comprobarListaVacia(lista);
		if(vacia != null) return vacia;

		String out = "";
		for(int i = 0; i < lista.length; i++) {
			if(i != 0) out += ", ";
			out += lista[i];
		}

		return out + '.';
	}

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
	 *		sus elementos.
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
		String vacia = comprobarListaVacia(lista);
		if(vacia != null) return vacia;

		String out = "";
		for(int i = 0; i < lista.length; i++) {
			if(i != 0) out += "\n";

			if(ordenada) out += " " + i + ". ";
			else out += "  - ";

			out += lista[i];
		}

		return out;
	}
}