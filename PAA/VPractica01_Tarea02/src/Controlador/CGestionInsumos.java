package Controlador;
import Vista.*;
import Modelo.*;

import javax.swing.*;
import java.awt.event.*;
public class CGestionInsumos implements ActionListener {

    private VGestionInsumos vista;
    private ModeloInsumo modelo;
    private ListaInsumo lista;

    public CGestionInsumos(VGestionInsumos v) {

        vista = v;
        modelo = new ModeloInsumo();
        lista = new ListaInsumo();

        vista.combo.setModel(modelo.getModeloCategoria());
        vista.lista.setModel(modelo.getModeloLista());

        vista.Bagregar.addActionListener(this);
        vista.Beliminar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.Bagregar) {

            int id = Integer.parseInt(vista.Tid.getText());
            String ins = vista.Tinsumo.getText();
            String cat = vista.combo.getSelectedItem().toString();

            Insumo i = new Insumo(id, ins, cat);
            lista.agregar(i);
            modelo.agregarLista(i.toString());
        }

        if (e.getSource() == vista.Beliminar) {

            int pos = vista.lista.getSelectedIndex();

            if (pos >= 0) {
                lista.eliminar(pos);
                modelo.eliminarLista(pos);
            }
        }
    }
}
