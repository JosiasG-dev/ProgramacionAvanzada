package com.examen.modelo;

import java.util.List;

public class Equipo {

    private List<String> nombres;
    private double calificacionRubrica;

    public Equipo() {}

    public Equipo(List<String> nombres, double calificacionRubrica) {
        this.nombres = nombres;
        this.calificacionRubrica = calificacionRubrica;
    }

    public List<String> getNombres() { return nombres; }
    public void setNombres(List<String> nombres) { this.nombres = nombres; }
    public double getCalificacionRubrica() { return calificacionRubrica; }
    public void setCalificacionRubrica(double calificacionRubrica) { this.calificacionRubrica = calificacionRubrica; }
}
