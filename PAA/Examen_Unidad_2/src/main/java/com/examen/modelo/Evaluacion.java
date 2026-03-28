package com.examen.modelo;

import java.util.List;
import java.util.Map;

public class Evaluacion {

    private String id;
    private String asignatura;
    private String profesor;
    private String grupo;
    private Map<String, Object> datosInstrumento;
    private List<Equipo> equipos;

    public Evaluacion() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }
    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public Map<String, Object> getDatosInstrumento() { return datosInstrumento; }
    public void setDatosInstrumento(Map<String, Object> datosInstrumento) { this.datosInstrumento = datosInstrumento; }
    public List<Equipo> getEquipos() { return equipos; }
    public void setEquipos(List<Equipo> equipos) { this.equipos = equipos; }
}
