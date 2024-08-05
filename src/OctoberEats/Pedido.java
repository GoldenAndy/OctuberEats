package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pedido {
    private int idPedido;
    private Usuario cliente;
    private Negocio restaurante;
    private String estado;
    private double precio;

    public Pedido(int idPedido, Usuario cliente, Negocio restaurante, double precio) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.estado = "pendiente";
        this.precio = precio;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public Negocio getRestaurante() {
        return restaurante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPrecio() {
        return precio;
    }

    public static Pedido cargarDesdeBaseDatos(int idPedido) throws SQLException {
        String query = "SELECT * FROM pedidos WHERE pedidoID = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPedido);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userID = resultSet.getString("userID");
                    int restauranteID = resultSet.getInt("restauranteID");
                    String estado = resultSet.getString("estado");
                    double precio = resultSet.getDouble("precio");

                    Usuario cliente = Usuario.loadFromDatabase(userID);
                    Restaurante restaurante = Restaurante.cargarDesdeBaseDatos(restauranteID);

                    Pedido pedido = new Pedido(idPedido, cliente, restaurante, precio);
                    pedido.setEstado(estado);
                    return pedido;
                } else {
                    return null;
                }
            }
        }
    }

    public void guardarEnBaseDatos() throws SQLException {
        String query = "INSERT INTO pedidos (userID, restauranteID, estado, precio) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, getCliente().getUserID());
            statement.setInt(2, ((Restaurante) getRestaurante()).getIdRestaurante());
            statement.setString(3, getEstado());
            statement.setDouble(4, getPrecio());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.idPedido = generatedKeys.getInt(1);
                }
            }
        }
    }
}


