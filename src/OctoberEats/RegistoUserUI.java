package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistoUserUI extends JFrame {
    private JTextField textFieldNombre;
    private JTextField textFieldDireccion;
    private JTextField textFieldCorreo;
    private JPasswordField passwordField;
    private JComboBox<Usuario.Rol> comboBoxRol;
    private JButton btnRegistrar;

    private List<Usuario> usuarios;

    public RegistoUserUI() {
        setTitle("Registro de Usuario");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usuarios = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Nombre: "));
        textFieldNombre = new JTextField();
        panel.add(textFieldNombre);

        panel.add(new JLabel("Dirección: "));
        textFieldDireccion = new JTextField();
        panel.add(textFieldDireccion);

        panel.add(new JLabel("Correo: "));
        textFieldCorreo = new JTextField();
        panel.add(textFieldCorreo);

        panel.add(new JLabel("Contraseña: "));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Rol: "));
        comboBoxRol = new JComboBox<>(Usuario.Rol.values());
        panel.add(comboBoxRol);

        btnRegistrar = new JButton("Registrar");
        panel.add(btnRegistrar);

        add(panel);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    public void registrarUsuario() {
        String nombre = textFieldNombre.getText();
        String direccion = textFieldDireccion.getText();
        String correo = textFieldCorreo.getText();
        String contrasena = new String(passwordField.getPassword());
        Usuario.Rol rol = (Usuario.Rol) comboBoxRol.getSelectedItem();

        if (nombre.isEmpty() || direccion.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || rol == null) {
            JOptionPane.showMessageDialog(this, "Por favor, complete correctamente los campos requeridos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String userID = java.util.UUID.randomUUID().toString();
        String contrasenaHush = Seguridad.hashPassword(contrasena);

        Usuario usuario = new Usuario(userID, direccion, contrasenaHush, nombre, correo, rol);
        try {
            usuario.saveToDatabase();
            usuarios.add(usuario);
            JOptionPane.showMessageDialog(this, "Usuario registrado con éxito", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            textFieldNombre.setText("");
            textFieldDireccion.setText("");
            textFieldCorreo.setText("");
            passwordField.setText("");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MenuUI menuUI = new MenuUI(usuario);
                    menuUI.setVisible(true);
                }
            });

            this.dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
