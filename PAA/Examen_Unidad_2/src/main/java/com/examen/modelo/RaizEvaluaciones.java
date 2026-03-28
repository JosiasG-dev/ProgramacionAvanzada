package com.examen.modelo;

import java.util.ArrayList;
import java.util.List;

public class RaizEvaluaciones {

    private List<Evaluacion> evaluaciones;

    public RaizEvaluaciones() {
        this.evaluaciones = new ArrayList<>();
    }

    public List<Evaluacion> getEvaluaciones() { return evaluaciones; }
    public void setEvaluaciones(List<Evaluacion> evaluaciones) { this.evaluaciones = evaluaciones; }
}
