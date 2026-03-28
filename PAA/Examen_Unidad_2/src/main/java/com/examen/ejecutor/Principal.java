package com.examen.ejecutor;

import com.examen.controlador.Controlador;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Principal {

    public static void main(String[] args) {
        try {
            System.setProperty("java.io.tmpdir", System.getProperty("user.dir") + "/temp");
            UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
            UIManager.put("Panel.background", new java.awt.Color(245, 245, 245));
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("FlatLaf no disponible.");
        }

        SwingUtilities.invokeLater(() -> {
            Controlador controlador = new Controlador();
            controlador.iniciar();
        });
    }
}
