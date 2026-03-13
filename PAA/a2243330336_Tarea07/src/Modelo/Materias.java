package Modelo;

import java.util.Objects;

public class Materias {
	private String materia;
	private String carrera;

	public Materias(String materia, String carrera) {
		this.materia = materia;
		this.carrera = carrera;
	}

	@Override
	public String toString() {
		return this.getMateria();
	}

	@Override
	public int hashCode() {
		return Objects.hash(materia, carrera);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Materias other = (Materias) obj;
		return Objects.equals(materia, other.materia) && Objects.equals(carrera, other.carrera);
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getCarrera() {
		return carrera;
	}

	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}
}
