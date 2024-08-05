package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainMenuUI extends JFrame {
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JButton btnRegistrarRestaurante;
    private JButton btnSalir;

    public MainMenuUI() {
        setTitle("October Eats - Menú Principal");
        setSize(600, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear panel de botones con GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarRestaurante = new JButton("Registrar Restaurante");
        btnSalir = new JButton("Salir");

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnIniciarSesion, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(btnRegistrarse, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        buttonPanel.add(btnRegistrarRestaurante, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        buttonPanel.add(btnSalir, gbc);

        // Crear panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Añadir panel principal al frame
        add(mainPanel);

        // Añadir listeners a los botones
        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        btnRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        btnRegistrarRestaurante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarParaRegistrarRestaurante();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void iniciarSesion() {
        JTextField correoField = new JTextField();
        JPasswordField contrasenaField = new JPasswordField();
        Object[] message = {
            "Correo:", correoField,
            "Contraseña:", contrasenaField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Iniciar Sesión", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String correo = correoField.getText();
            String contrasena = new String(contrasenaField.getPassword());

            try {
                Usuario usuario = Usuario.loadByCorreo(correo);
                if (usuario != null && usuario.verifyPassword(contrasena)) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    MenuUI menuUI = new MenuUI(usuario);
                    menuUI.setVisible(true);
                    MainMenuUI.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al iniciar sesión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void registrarUsuario() {
        RegistoUserUI registoUserUI = new RegistoUserUI();
        registoUserUI.setVisible(true);
        this.dispose();
    }

    private void autenticarParaRegistrarRestaurante() {
        JTextField correoField = new JTextField();
        JPasswordField contrasenaField = new JPasswordField();
        Object[] message = {
            "Correo:", correoField,
            "Contraseña:", contrasenaField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Autenticación para Registrar Restaurante", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String correo = correoField.getText();
            String contrasena = new String(contrasenaField.getPassword());

            try {
                Usuario usuario = Usuario.loadByCorreo(correo);
                if (usuario != null && usuario.verifyPassword(contrasena)) {
                    JOptionPane.showMessageDialog(null, "Autenticación exitosa", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    RegistrarRestauranteUI registrarRestauranteUI = new RegistrarRestauranteUI();
                    registrarRestauranteUI.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error en la autenticación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}


