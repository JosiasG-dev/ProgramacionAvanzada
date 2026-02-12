package Controlador;
import Modelo.*;
import Vista.*;

import java.awt.event.*;
import javax.swing.JOptionPane;
public class CPractica01_Tarea01 implements ActionListener {

    private VPractica01_Tarea01 vista;
    private ModeloInsumo modelo;
    private ListaInsumo lista;

    public CPractica01_Tarea01() {

        vista = new VPractica01_Tarea01();
        modelo = new ModeloInsumo();
        lista = new ListaInsumo();

        vista.getCategoria().setModel(modelo.getModeloCategoria());
        vista.getLista().setModel(modelo.getModeloLista());

        vista.getBagregar().addActionListener(this);
        vista.getBeliminar().addActionListener(this);
        vista.getBsalir().addActionListener(this);

        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // AGREGAR
        if (e.getSource() == vista.getBagregar()) {

            try {
                int id = Integer.parseInt(vista.getId());
                String insumo = vista.getInsumo().trim();
                String cat = vista.getCategoria().getSelectedItem().toString();

                if (insumo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos");
                    return;
                }

                Insumo i = new Insumo(id, insumo, cat);
                lista.agregar(i);
                modelo.agregarLista(i.toString());

                vista.limpiar();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID debe ser numerico");
            }
        }

        if (e.getSource() == vista.getBeliminar()) {

            int pos = vista.getLista().getSelectedIndex();

            if (pos < 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento");
                return;
            }

            lista.eliminar(pos);
            modelo.eliminarLista(pos);
        }

        if (e.getSource() == vista.getBsalir()) {
            int op = JOptionPane.showConfirmDialog(null, "¿Desea salir?");
            if (op == 0) System.exit(0);
        }
    }
}
