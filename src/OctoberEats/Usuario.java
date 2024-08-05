package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario extends PersonaImpl {
    private Rol rol;

    public enum Rol {
        CLIENTE,
        REPARTIDOR
    }

    public Usuario(String userID, String direccion, String contrasena, String nombre, String correo, Rol rol) {
        super(userID, direccion, contrasena, nombre, correo);
        this.rol = rol;
    }

    // Getter y Setter para el rol
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public static Usuario loadFromDatabase(String userID) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE userID = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String direccion = resultSet.getString("direccion");
                    String contrasenaHush = resultSet.getString("contrasenaHush");
                    String nombre = resultSet.getString("nombre");
                    String correo = resultSet.getString("correo");
                    Rol rol = Rol.valueOf(resultSet.getString("rol"));
                    return new Usuario(userID, direccion, contrasenaHush, nombre, correo, rol);
                } else {
                    return null;
                }
            }
        }
    }

    public static Usuario loadByCorreo(String correo) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, correo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userID = resultSet.getString("userID");
                    String direccion = resultSet.getString("direccion");
                    String contrasenaHush = resultSet.getString("contrasenaHush");
                    String nombre = resultSet.getString("nombre");
                    Rol rol = Rol.valueOf(resultSet.getString("rol"));
                    return new Usuario(userID, direccion, contrasenaHush, nombre, correo, rol);
                } else {
                    return null;
                }
            }
        }
    }

    public boolean verifyPassword(String contrasena) {
        return this.getContrasena().equals(Seguridad.hashPassword(contrasena));
    }

    public void saveToDatabase() throws SQLException {
        String query = "INSERT INTO usuarios (userID, nombre, direccion, correo, contrasenaHush, rol) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, getUserID());
            statement.setString(2, getNombre());
            statement.setString(3, getDireccion());
            statement.setString(4, getCorreo());
            statement.setString(5, getContrasena());
            statement.setString(6, getRol().name());
            statement.executeUpdate();
        }
    }
}

