package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegistrarRestauranteUI extends JFrame {
    private JTextField idField;
    private JTextField nombreField;
    private JTextField direccionField;
    private JTextField correoField;
    private JTextField categoriaField;
    private JTextArea errorArea;

    public RegistrarRestauranteUI() {
        setTitle("Registrar Restaurante");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        panel.add(new JLabel("Identificador del restaurante:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Nombre del restaurante:"));
        nombreField = new JTextField();
        panel.add(nombreField);

        panel.add(new JLabel("Dirección del restaurante:"));
        direccionField = new JTextField();
        panel.add(direccionField);

        panel.add(new JLabel("Correo electrónico (Restaurante):"));
        correoField = new JTextField();
        panel.add(correoField);

        panel.add(new JLabel("Categoría:"));
        categoriaField = new JTextField();
        panel.add(categoriaField);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar registro");
        panel.add(btnRegistrar);
        panel.add(btnCancelar);

        errorArea = new JTextArea();
        errorArea.setForeground(Color.RED);
        errorArea.setEditable(false);
        errorArea.setLineWrap(true);
        errorArea.setWrapStyleWord(true);
        errorArea.setOpaque(false);  // Hacer que el fondo sea transparente
        errorArea.setFocusable(false);  // Hacer que no sea focalizable
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(errorArea);

        add(panel);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarRestaurante();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuUI mainMenuUI = new MainMenuUI();
                mainMenuUI.setVisible(true);
                dispose();
            }
        });
    }

    private void registrarRestaurante() {
        String idText = idField.getText();
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        String correo = correoField.getText();
        String categoria = categoriaField.getText();

        if (!idText.matches("\\d+")) {
            errorArea.setText("Solo se permiten números en el identificador");
            return;
        }

        int id = Integer.parseInt(idText);

        try {
            Restaurante existente = Restaurante.cargarDesdeBaseDatos(id);
            if (existente != null) {
                errorArea.setText("Identificador en uso, ingrese uno diferente");
                return;
            }

            Restaurante restaurante = new Restaurante(id, nombre, direccion, correo, categoria);
            restaurante.guardarEnBaseDatos();
            JOptionPane.showMessageDialog(this, "El restaurante " + nombre + " se registró exitosamente", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            MainMenuUI mainMenuUI = new MainMenuUI();
            mainMenuUI.setVisible(true);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el restaurante: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

