package Parte3;

import Libreria.Archivotxt;
import Libreria.Librerias;
import Modelo.Categoria;
import Modelo.ListaCategorias;
import Modelo.ListaInsumos;
import Vista.ProductoVista;
import Controlador.ProductoControlador;

import javax.swing.*;

public class Practica06_b {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Archivotxt archivoCategorias = new Archivotxt("categorias.txt");
            Archivotxt archivoInsumos = new Archivotxt("insumos.txt");
            Librerias libreria = new Librerias();

            ListaCategorias listaCategorias = new ListaCategorias();
            ListaInsumos listaInsumos = new ListaInsumos();

            if (archivoCategorias.existe()) {
                listaCategorias.cargarCategorias(archivoCategorias.cargar());
            } else {
                listaCategorias.agregarCategoria(new Categoria("01", "Materiales"));
                listaCategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
                listaCategorias.agregarCategoria(new Categoria("03", "Maquinaria y Equipo"));
                listaCategorias.agregarCategoria(new Categoria("04", "Servicios"));
            }

            if (archivoInsumos.existe()) {
                listaInsumos.cargarInsumo(archivoInsumos.cargar());
            }

            ProductoVista vista = new ProductoVista(listaCategorias, listaInsumos, libreria);

            ProductoControlador controlador = new ProductoControlador(vista, listaInsumos, listaCategorias, archivoInsumos);

            vista.setVisible(true);
        });
    }
}
