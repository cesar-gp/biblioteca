package dam.biblioteca;

public final class Listas {

	// Constructor privado para que no se puedan
	// crear instancias de la clase.
	private Listas() {}

	// Funciones

	private static String comprobarListaVacia(Object[] lista) {
		if(lista == null) return "[Lista no inicializada]";
		if(lista.length == 0) return "[Lista vacía]";

		return null;
	}

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