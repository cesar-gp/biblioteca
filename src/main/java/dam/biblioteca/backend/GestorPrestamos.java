package dam.biblioteca.backend;

public class GestorPrestamos {

	// Propiedades no estáticas

	private Prestamo[] activos;
	private Prestamo[] inactivos;

	private GestorUsuarios gUsuarios;
	private GestorLibros gLibros;

	// Constructor

	public GestorPrestamos(GestorUsuarios gUsuarios, GestorLibros gLibros) {
		this.activos = new Prestamo[0];
		this.inactivos = new Prestamo[0];

		this.gUsuarios = gUsuarios;
		this.gLibros = gLibros;
	}

	// Getters

	public Prestamo[] getPrestamos() {
		// ¿El número total de préstamos excede el límite de
		// elementos que puede tener un array? Devolver solo
		// los activos.
		if(Integer.MAX_VALUE - this.activos.length < this.inactivos.length)
			return this.activos;

		// Crear lista con espacio para todos los préstamos.
		Prestamo[] out = new Prestamo[this.activos.length + this.inactivos.length];

		// Llenar la lista con préstamos activos e inactivos.
		for(int i = 0; i < this.activos.length; i++)
			out[i] = this.activos[i];

		for(int i = 0; i < this.inactivos.length; i++)
			out[i + this.activos.length] = this.inactivos[i];

		// Devolver la lista.
		return out;
	}

	public Prestamo[] getPrestamos(boolean activos) {
		if(activos) return this.activos;
		else return this.inactivos;
	}

	public Prestamo getPrestamoActivo(Usuario usuario, Libro libro) {
		for(int i = 0; i < this.activos.length; i++)
			if(this.activos[i].getUsuario().getNombre().equalsIgnoreCase(usuario.getNombre()) &&
				this.activos[i].getLibro().getTitulo().equalsIgnoreCase(libro.getTitulo()))
				return this.activos[i];

		return null;
	}

	// Funciones

	public int registrar(Usuario usuario, Libro libro) {
		// ¿No caben más préstamos activos? Error 1.
		if(this.activos.length == Integer.MAX_VALUE) return 1;

		// ¿El usuario o el libro son nulos? Error 2.
		if(usuario == null || libro == null) return 2;

		// ¿Préstamo repetido? Error 3.
		for(int i = 0; i < this.activos.length; i++)
			if(this.activos[i].getUsuario().getNombre().equalsIgnoreCase(usuario.getNombre()) &&
				this.activos[i].getLibro().getTitulo().equalsIgnoreCase(libro.getTitulo()))
				return 3;

		// Incluir préstamo en la lista de préstamos activos.
		Prestamo[] copia = new Prestamo[this.activos.length + 1];
		for(int i = 0; i < this.activos.length; i++)
			copia[i] = this.activos[i];

		copia[this.activos.length] = new Prestamo(usuario, libro);
		this.activos = copia;

		// Operación realizada.
		return 0;
	}

	public int eliminar(Prestamo prestamo) {
		// ¿El prestamo es nulo? Error 1.
		if(prestamo == null) return 1;

		// Conseguir índice del préstamo en la lista
		// de préstamos activos.
		int indice = -1;
		for(int i = 0; i < this.activos.length && indice == -1; i++)
			if(this.activos[i].getUsuario().getNombre().equalsIgnoreCase(prestamo.getUsuario().getNombre()) &&
				this.activos[i].getLibro().getTitulo().equalsIgnoreCase(prestamo.getLibro().getTitulo()))
				indice = i;

		// ¿El préstamo no está registrado? Error 2.
		if(indice == -1) return 2;

		// Eliminar préstamo de la lista de préstamos activos.
		Prestamo[] copiaAct = new Prestamo[this.activos.length - 1];
		for(int i = 0; i < indice; i++)
			copiaAct[i] = this.activos[i];

		for(int i = indice; i < this.activos.length - 1; i++)
			copiaAct[i] = this.activos[i + 1];

		this.activos = copiaAct;

		// Añadir préstamo a la lista de préstamos inactivos.
		Prestamo[] copiaInact = new Prestamo[this.inactivos.length + 1];
		for(int i = 0; i < this.inactivos.length; i++)
			copiaInact[i] = this.inactivos[i];

		copiaInact[this.inactivos.length] = prestamo;
		this.inactivos = copiaInact;

		// Operación realizada.
		return 0;
	}

	public Libro[] getTopLibros() {
		// TODO: no soportado.
		return new Libro[0];
	}

	public Usuario[] getTopUsuarios() {
		// TODO: no soportado.
		return new Usuario[0];
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
}