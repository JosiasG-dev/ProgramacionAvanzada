package modelo;

public class UnidadMedida {
    private int id;
    private String nombre;       
    private String abreviatura; 
    private String tipo;      
	private boolean activa;

	public UnidadMedida() {
		this.activa = true;
	}

	public UnidadMedida(int id, String nombre, String abreviatura, String tipo) {
		this.id = id;
		this.nombre = nombre;
		this.abreviatura = abreviatura;
		this.tipo = tipo;
		this.activa = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	@Override
	public String toString() {
		return nombre + " (" + abreviatura + ")";
	}
}
