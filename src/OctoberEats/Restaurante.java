package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Restaurante implements Negocio {
    private int idRestaurante;
    private String nombre;
    private String direccion;
    private String correo;
    private String categoria;

    public Restaurante(int idRestaurante, String nombre, String direccion, String correo, String categoria) {
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.direccion = direccion;
        this.correo = correo;
        this.categoria = categoria;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDireccion() {
        return direccion;
    }

    @Override
    public String getEmail() {
        return correo;
    }

    @Override
    public String getCategoria() {
        return categoria;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public static Restaurante cargarDesdeBaseDatos(int idRestaurante) throws SQLException {
        String query = "SELECT * FROM restaurantes WHERE restauranteID = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idRestaurante);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nombre = resultSet.getString("nombre");
                    String direccion = resultSet.getString("direccion");
                    String correo = resultSet.getString("email");
                    String categoria = resultSet.getString("categoria");
                    return new Restaurante(idRestaurante, nombre, direccion, correo, categoria);
                } else {
                    return null;
                }
            }
        }
    }

    public static List<Restaurante> cargarTodosDesdeBaseDatos() throws SQLException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String query = "SELECT * FROM restaurantes";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int idRestaurante = resultSet.getInt("restauranteID");
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String correo = resultSet.getString("email");
                String categoria = resultSet.getString("categoria");
                restaurantes.add(new Restaurante(idRestaurante, nombre, direccion, correo, categoria));
            }
        }
        return restaurantes;
    }

    public void guardarEnBaseDatos() throws SQLException {
        String query = "INSERT INTO restaurantes (nombre, direccion, email, categoria) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, getNombre());
            statement.setString(2, getDireccion());
            statement.setString(3, getEmail());
            statement.setString(4, getCategoria());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.idRestaurante = generatedKeys.getInt(1);
                }
            }
        }
    }
}

