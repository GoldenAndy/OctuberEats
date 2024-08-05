package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorPedidos {
    private List<Pedido> pedidos;
    private List<PedidoListener> pedidoListeners;

    public GestorPedidos() {
        this.pedidos = new ArrayList<>();
        this.pedidoListeners = new ArrayList<>();
        cargarPedidosDesdeBaseDatos();
    }

    private void cargarPedidosDesdeBaseDatos() {
        List<Pedido> pedidosCargados = new ArrayList<>();
        // Aquí cargaríamos todos los pedidos desde la base de datos
        // por ahora simulamos con un pedido vacío
        // Ejemplo: pedidosCargados.add(Pedido.cargarDesdeBaseDatos(1));
        this.pedidos = pedidosCargados;
    }

    public void addPedidoListener(PedidoListener listener) {
        pedidoListeners.add(listener);
    }

    public void realizarPedido(Usuario cliente, Negocio restaurante, double precio) {
        Pedido pedido = new Pedido(0, cliente, restaurante, precio);
        try {
            pedido.guardarEnBaseDatos();
            pedidos.add(pedido);
            notifyPedidoRealizado(pedido);
            // Iniciar la simulación de la entrega del pedido
            simularProcesoEntrega(pedido);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEstado(int idPedido, String nuevoEstado) {
        for (Pedido pedido : pedidos) {
            if (pedido.getIdPedido() == idPedido) {
                pedido.setEstado(nuevoEstado);
                try {
                    pedido.guardarEnBaseDatos();
                    notifyEstadoPedidoActualizado(pedido);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public List<Pedido> getPedidosUsuario(Usuario usuario) {
        List<Pedido> pedidosUsuario = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente().equals(usuario)) {
                pedidosUsuario.add(pedido);
            }
        }
        return pedidosUsuario;
    }

    private void notifyPedidoRealizado(Pedido pedido) {
        for (PedidoListener listener : pedidoListeners) {
            listener.onPedidoRealizado(pedido);
        }
    }

    private void notifyEstadoPedidoActualizado(Pedido pedido) {
        for (PedidoListener listener : pedidoListeners) {
            listener.onEstadoPedidoActualizado(pedido);
        }
    }

    private void simularProcesoEntrega(Pedido pedido) {
        new Thread(() -> {
            try {
                String[] estados = {"aceptado", "en camino", "entregado"};
                for (String estado : estados) {
                    Thread.sleep(10000); // Esperar 10 segundos entre estados
                    actualizarEstado(pedido.getIdPedido(), estado);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface PedidoListener {
        void onPedidoRealizado(Pedido pedido);
        void onEstadoPedidoActualizado(Pedido pedido);
    }
}
