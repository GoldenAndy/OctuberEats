package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MenuUI extends JFrame {
    private JTextField barraBusqueda;
    private JButton searchButton;
    private JButton btnInicio;
    private JButton btnCarrito;
    private JButton btnPedidos;
    private JButton btnCuenta;
    private JPanel resultsPanel;
    private Usuario usuario;
    private GestorPedidos gestorPedidos;
    private List<Pedido> carrito;

    public MenuUI(Usuario usuario) {
        this.usuario = usuario;
        this.gestorPedidos = new GestorPedidos();
        this.carrito = new ArrayList<>();

        setTitle("October Eats - Menú Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear panel de búsqueda
        JPanel searchPanel = new JPanel();
        barraBusqueda = new JTextField(20);
        searchButton = new JButton("Buscar");
        searchPanel.add(barraBusqueda);
        searchPanel.add(searchButton);

        // Crear panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        btnInicio = new JButton("Inicio");
        btnCarrito = new JButton("Carrito");
        btnPedidos = new JButton("Pedidos");
        btnCuenta = new JButton("Cuenta");
        buttonPanel.add(btnInicio);
        buttonPanel.add(btnCarrito);
        buttonPanel.add(btnPedidos);
        buttonPanel.add(btnCuenta);

        // Crear panel de resultados
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);

        // Añadir componentes al panel principal
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Añadir panel principal al frame
        add(mainPanel);

        // Añadir listeners a los botones
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRestaurantes();
            }
        });

        btnInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInicio();
            }
        });

        btnCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCarrito();
            }
        });

        btnPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPedidos();
            }
        });

        btnCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCuenta();
            }
        });

        // Mostrar todos los restaurantes al inicio
        mostrarTodosLosRestaurantes();

        // Agregar listeners para manejar eventos de pedidos
        gestorPedidos.addPedidoListener(new GestorPedidos.PedidoListener() {
            @Override
            public void onPedidoRealizado(Pedido pedido) {
                System.out.println("Nuevo pedido realizado: #" + pedido.getIdPedido());
            }

            @Override
            public void onEstadoPedidoActualizado(Pedido pedido) {
                System.out.println("Estado del pedido #" + pedido.getIdPedido() + " actualizado a: " + pedido.getEstado());
                mostrarPedidos();
            }
        });
    }

    private void buscarRestaurantes() {
        String busqueda = barraBusqueda.getText();
        resultsPanel.removeAll();
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            try {
                List<Restaurante> resultados = Restaurante.cargarTodosDesdeBaseDatos().stream()
                        .filter(r -> r.getNombre().toLowerCase().contains(busqueda.toLowerCase())
                                || r.getCategoria().toLowerCase().contains(busqueda.toLowerCase()))
                        .collect(Collectors.toList());

                if (resultados.isEmpty()) {
                    resultsPanel.add(new JLabel("No se encontraron restaurantes"));
                } else {
                    mostrarRestaurantes(resultados);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                resultsPanel.add(new JLabel("Error al buscar restaurantes"));
            }
        } else {
            resultsPanel.add(new JLabel("Input inválido"));
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void mostrarInicio() {
        JOptionPane.showMessageDialog(null, "Bienvenido al menú principal");
    }

    private void mostrarCarrito() {
        resultsPanel.removeAll();
        if (carrito.isEmpty()) {
            resultsPanel.add(new JLabel("El carrito está vacío"));
        } else {
            for (Pedido pedido : carrito) {
                JPanel pedidoPanel = new JPanel(new GridLayout(0, 1));
                JTextArea pedidoInfo = new JTextArea();
                pedidoInfo.setText("Pedido ID: " + pedido.getIdPedido() + "\n" +
                        "Restaurante: " + pedido.getRestaurante().getNombre() + "\n" +
                        "Estado: " + pedido.getEstado() + "\n" +
                        "Precio: $" + pedido.getPrecio());
                pedidoInfo.setEditable(false);
                JButton btnRealizarPedido = new JButton("Realizar Pedido");
                btnRealizarPedido.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gestorPedidos.realizarPedido(usuario, pedido.getRestaurante(), pedido.getPrecio());
                        carrito.remove(pedido);
                        mostrarCarrito();
                    }
                });
                pedidoPanel.add(pedidoInfo);
                pedidoPanel.add(btnRealizarPedido);
                resultsPanel.add(pedidoPanel);
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void mostrarPedidos() {
        resultsPanel.removeAll();
        List<Pedido> pedidosUsuario = gestorPedidos.getPedidosUsuario(usuario);
        if (pedidosUsuario.isEmpty()) {
            resultsPanel.add(new JLabel("No tienes pedidos en este momento"));
        } else {
            for (Pedido pedido : pedidosUsuario) {
                JPanel pedidoPanel = new JPanel(new GridLayout(0, 1));
                JTextArea pedidoInfo = new JTextArea();
                pedidoInfo.setText("Pedido ID: " + pedido.getIdPedido() + "\n" +
                        "Restaurante: " + pedido.getRestaurante().getNombre() + "\n" +
                        "Estado: " + pedido.getEstado() + "\n" +
                        "Precio: $" + pedido.getPrecio());
                pedidoInfo.setEditable(false);
                pedidoPanel.add(pedidoInfo);
                resultsPanel.add(pedidoPanel);
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void mostrarCuenta() {
        JOptionPane.showMessageDialog(this, "Usuario: " + usuario.getNombre() + "\nCorreo: " + usuario.getCorreo() + "\nDirección: " + usuario.getDireccion());
    }

    private void mostrarTodosLosRestaurantes() {
        try {
            mostrarRestaurantes(Restaurante.cargarTodosDesdeBaseDatos());
        } catch (SQLException e) {
            e.printStackTrace();
            resultsPanel.add(new JLabel("Error al cargar restaurantes"));
        }
    }

    private void mostrarRestaurantes(List<Restaurante> restaurantes) {
        resultsPanel.removeAll();
        for (Restaurante restaurante : restaurantes) {
            JPanel restaurantePanel = new JPanel(new GridLayout(0, 1));
            JTextArea restauranteInfo = new JTextArea();
            restauranteInfo.setText("Nombre: " + restaurante.getNombre() + "\n" +
                    "Direccion: " + restaurante.getDireccion() + "\n" +
                    "Email: " + restaurante.getEmail() + "\n" +
                    "Categoría: " + restaurante.getCategoria());
            restauranteInfo.setEditable(false);
            JButton btnAgregarCarrito = new JButton("Agregar al carrito");
            btnAgregarCarrito.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    agregarAlCarrito(restaurante);
                }
            });
            restaurantePanel.add(restauranteInfo);
            restaurantePanel.add(btnAgregarCarrito);
            resultsPanel.add(restaurantePanel);
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void agregarAlCarrito(Restaurante restaurante) {
        Random random = new Random();
        double precio = 10 + (40 - 10) * random.nextDouble(); // Precio aleatorio
        Pedido pedido = new Pedido(carrito.size() + 1, usuario, restaurante, precio);
        carrito.add(pedido);
        JOptionPane.showMessageDialog(this, "Pedido agregado al carrito: " + restaurante.getNombre() + " - $" + precio);
    }
}
