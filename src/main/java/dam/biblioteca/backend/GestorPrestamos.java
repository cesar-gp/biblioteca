package dam.biblioteca.backend;

public class GestorPrestamos {

	// Propiedades no estáticas

	private Prestamo[] lista;

	// Funciones

	public Prestamo[] getPrestamos() {
		return this.lista;
	}

	public int getTotalPrestamos(boolean activo) {
		// Contar número de préstamos que cumplen la condición.
		int len = 0;
		for(int i = 0; i < this.lista.length; i++)
			if(this.lista[i].isActivo() == activo) len++;

		// Devolver el número.
		return len;
	}

	public Libro[] getTopLibros(int limite) {
		// TODO: no soportado.
		return new Libro[0];
	}

	public Usuario[] getTopUsuarios(int limite) {
		// TODO: no soportado.
		return new Usuario[0];
	}

	public int registrar(Usuario usuario, Libro libro) {
		// ¿No hay espacio para más prestamos? Error 1.
		if(this.lista.length == Integer.MAX_VALUE) return 1;

		// ¿Usuario o libro nulos? Error 2.
		if(usuario == null || libro == null) return 2;

		// ¿Préstamo repetido? Error 3.
		for(int i = 0; i < this.lista.length; i++)
			if(this.lista[i].getUsuario().getNombre().equalsIgnoreCase(usuario.getNombre()) &&
				this.lista[i].getLibro().getIsbn().equalsIgnoreCase(libro.getIsbn()))
				return 3;

		// Crear copia de la lista con un hueco adicional.
		Prestamo[] copia = new Prestamo[this.lista.length + 1];

		// Llenar la copia y meter el nuevo préstamo al final.
		for(int i = 0; i < this.lista.length; i++)
			copia[i] = this.lista[i];
		copia[this.lista.length] = new Prestamo(usuario, libro);

		// Cambiar lista actual por la copia.
		this.lista = copia;

		// Operación realizada.
		return 0;
	}

	public int eliminar(Usuario usuario, Libro libro) {
		// ¿Usuario o libro nulos? Error 1 (nulo o no registrado).
		if(usuario == null || libro == null) return 1;

		// Conseguir índice del préstamo en la lista.
		int indice = -1;
		for(int i = 0; i < this.lista.length && indice == -1; i++)
			if(this.lista[i].getUsuario() == usuario &&
				this.lista[i].getLibro() == libro)
				indice = i;

		// ¿No está registrado? Error 1 (nulo o no registrado).
		if(indice == -1) return 1;

		// Crear copia de la lista sin el elemento en ese índice
		// y sustituir la lista original por la copia.
		Prestamo[] copia = new Prestamo[this.lista.length - 1];
		for(int i = 0; i < indice; i++)
			copia[i] = this.lista[i];

		for(int i = indice; i < this.lista.length - 1; i++)
			copia[i] = this.lista[i + 1];

		this.lista = copia;

		// Operación realizada.
		return 0;
	}
}