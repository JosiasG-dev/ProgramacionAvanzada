package com.examen.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class RepositorioJson {

    private static final String RUTA = "evaluaciones.json";
    private final Gson gson;

    public RepositorioJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        File archivo = new File(RUTA);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                guardarTodo(new RaizEvaluaciones());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RaizEvaluaciones cargarTodo() {
        try (FileReader lector = new FileReader(RUTA)) {
            RaizEvaluaciones raiz = gson.fromJson(lector, RaizEvaluaciones.class);
            return raiz != null ? raiz : new RaizEvaluaciones();
        } catch (IOException e) {
            return new RaizEvaluaciones();
        }
    }

    public void guardarTodo(RaizEvaluaciones raiz) {
        try (FileWriter escritor = new FileWriter(RUTA)) {
            gson.toJson(raiz, escritor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertarOActualizar(Evaluacion evaluacion) {
        RaizEvaluaciones raiz = cargarTodo();
        raiz.getEvaluaciones().removeIf(e -> e.getId().equals(evaluacion.getId()));
        raiz.getEvaluaciones().add(evaluacion);
        guardarTodo(raiz);
    }

    public void eliminar(String id) {
        RaizEvaluaciones raiz = cargarTodo();
        raiz.getEvaluaciones().removeIf(e -> e.getId().equals(id));
        guardarTodo(raiz);
    }

    public Evaluacion buscarPorId(String id) {
        return cargarTodo().getEvaluaciones().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
