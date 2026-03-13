package Modelo;

import java.util.Objects;

public class Asignaturas {
    String carrera, materia, profesor, grupo;

	public Asignaturas(String carrera, String materia, String profesor, String grupo) {
		this.carrera = carrera;
		this.materia = materia;
		this.profesor = profesor;
		this.grupo = grupo;
	}

	public String getCarrera() {
		return carrera;
	}

	public void setCarrera(String c) {
		this.carrera = c;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String m) {
		this.materia = m;
	}

	public String getProfesor() {
		return profesor;
	}

	public void setProfesor(String p) {
		this.profesor = p;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String g) {
		this.grupo = g;
	}

	@Override
	public String toString() {
		return grupo.trim() + " - " + profesor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carrera, grupo, materia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Asignaturas o = (Asignaturas) obj;
		return Objects.equals(carrera, o.carrera) && Objects.equals(grupo, o.grupo)
				&& Objects.equals(materia, o.materia);
	}
}
