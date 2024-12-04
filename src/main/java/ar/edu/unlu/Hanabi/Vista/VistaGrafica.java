package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class VistaGrafica extends JFrame implements IVista {
    private ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;  // Área de salida de mensajes
    private final List<Jugador> jugadoresRegistrados;
    private static int contadorClientes = 0;

    public VistaGrafica(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
        this.jugadoresRegistrados = new ArrayList<>();
        this.txtSalida = new JTextArea();

        // Configura la ventana y sus componentes
        configurarVentana();
        configurarComponentes();
        mostrarMenuPrincipal();
    }

    private void configurarVentana() {
        setTitle("Hanabi - Juego Gráfico");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void configurarComponentes() {
        // Configurar el área de salida
        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtSalida);

        // Agregar el JScrollPane al contenedor
        add(scrollPane, BorderLayout.CENTER);
    }

    private void mostrarMenuPrincipal() {
        mostrarOpciones("Menú Principal", new String[]{
                "Nueva Partida",
                "Iniciar Partida",
                "Salir"
        }, this::procesarComandoMenuPrincipal);
    }

    private void procesarComandoMenuPrincipal(int opcion) {
        switch (opcion) {
            case 0 -> mostrarMenuNuevaPartida();
            case 1 -> intentarIniciarJuego();
            case 2 -> salirJuego();
            default -> mostrarMensaje("Opción no válida.");
        }
    }

    private void mostrarMenuNuevaPartida() {
        mostrarOpciones("Menú de Nueva Partida", new String[]{
                "Crear un nuevo jugador",
                "Iniciar partida"
        }, this::procesarMenuNuevaPartida);
    }

    private void procesarMenuNuevaPartida(int opcion) {
        switch (opcion) {
            case 0 -> iniciarRegistroJugador();
            case 1 -> intentarIniciarJuego();
            default -> mostrarMensaje("Opción no válida.");
        }
    }

    private void iniciarRegistroJugador() {
        if (jugadoresRegistrados.size() >= 5) {
            mostrarMensaje("Ya se alcanzó el límite máximo de 5 jugadores.");
            return;
        }
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del jugador:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            controlador.registrarJugador(nombre.trim());
            Jugador nuevoJugador = new Jugador(nombre.trim()); // Esto depende de cómo obtengas al jugador
            jugadoresRegistrados.add(nuevoJugador); // Agregar al registro local
            crearVistaPorJugador(nuevoJugador);

            // Volver al menú de nueva partida para continuar si hay espacio
            if (jugadoresRegistrados.size() < 5) {
                mostrarMenuNuevaPartida();
            } else {
                mostrarMensaje("Máximo de 5 jugadores alcanzado.");
                mostrarMenuNuevaPartida();
            }
        } else {
            mostrarMensaje("El nombre no puede estar vacío.");
        }
    }

    private void intentarIniciarJuego() {
        if (jugadoresRegistrados.size() < 2) {
            mostrarMensaje("Se necesitan al menos 2 jugadores para iniciar la partida.");
            mostrarMenuNuevaPartida();
            return;
        }
        //List<Jugador> jugadores = controlador.obtenerListaJugadores();
        controlador.iniciarJuego();
        controlador.iniciarTurno();
        mostrarMensaje("El juego ha comenzado.");
    }

    private void crearVistaPorJugador(Jugador jugador) {
        IVista vistaJugador = new VistaGraficaJugador(controlador, jugador);
        controlador.setVista(vistaJugador);
        vistaJugador.setControlador(controlador);
        vistaJugador.iniciar();
        final int CLIENT_PORT = 9955 + contadorClientes;;
        contadorClientes++;
        Cliente cliente = new Cliente("127.0.0.1", CLIENT_PORT, "127.0.0.1",
                9999);

        try {
            cliente.iniciar(controlador);
        } catch (RMIMVCException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        mostrarMensaje("Se creó una vista para el jugador: " + jugador.getNombre());
    }

    private void salirJuego() {
        mostrarMensaje("Saliendo del juego...");
        System.exit(0);
    }

    private void mostrarOpciones(String titulo, String[] opciones, OpcionSeleccionada callback) {
        JPanel panel = new JPanel(new GridLayout(opciones.length + 1, 1));
        panel.add(new JLabel(titulo, SwingConstants.CENTER));
        ButtonGroup grupoBotones = new ButtonGroup();
        JRadioButton[] botones = new JRadioButton[opciones.length];

        for (int i = 0; i < opciones.length; i++) {
            botones[i] = new JRadioButton(opciones[i]);
            grupoBotones.add(botones[i]);
            panel.add(botones[i]);
        }

        int opcionSeleccionada = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcionSeleccionada == JOptionPane.OK_OPTION) {
            for (int i = 0; i < botones.length; i++) {
                if (botones[i].isSelected()) {
                    callback.seleccionarOpcion(i);
                    return;
                }
            }
            mostrarMensaje("Por favor, seleccione una opción.");
        }
    }

    // Métodos de IVista
    @Override
    public void iniciar() {
        setVisible(true);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }

    @Override
    public  void mostrarMenuDeAccion(){

    }
    @Override
    public  void mostrarPuntuacion(){

    }
    @Override
    public void actualizarVista(){

    }
    @Override
    public void setControlador(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
    }



    // Interfaz funcional para opciones
    @FunctionalInterface
    interface OpcionSeleccionada {
        void seleccionarOpcion(int opcion);
    }


    public static void main(String[] args) {
        JuegoHanabi juegoHanabi = new JuegoHanabi();
        JuegoMostrable juegoMostrable = new JuegoMostrable(juegoHanabi);
        ControladorJuegoHanabi controladorjuego = new ControladorJuegoHanabi(juegoHanabi, juegoMostrable);

        // Iniciar la vista gráfica y pasarle el controlador
        VistaGrafica vista = new VistaGrafica(controladorjuego);

        // Establecer el controlador en la vista
        controladorjuego.setVista(vista);

        // Mostrar la ventana
        vista.iniciar();
    }

}
