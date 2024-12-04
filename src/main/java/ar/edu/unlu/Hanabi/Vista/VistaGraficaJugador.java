package ar.edu.unlu.Hanabi.Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

public class VistaGraficaJugador extends JFrame implements IVista {
    // Componentes principales
    private ControladorJuegoHanabi controlador;
    private final Jugador jugador;

    // Paneles principales
    private JPanel panelIzquierda;
    private JPanel panelDerecha;

    // Subpaneles
    private JPanel panelFichas;
    private JPanel panelJugadores;
    private JPanel panelMenuAccion;
    private JPanel panelManoJugador;
    private JPanel panelManosOtrosJugadores;
    private JPanel panelCastillosYMazo; // Nuevo panel para castillos y mazo
    private boolean banderaSeleccionCarta = false;
    private JDialog notificacionActiva;

    public VistaGraficaJugador(ControladorJuegoHanabi controlador, Jugador jugador) {
        this.controlador = controlador;
        this.jugador = jugador;
        //this.controlador.setVista(this);

        // Configuración inicial de la ventana
        setTitle("Hanabi - Juego de Cartas" + controlador.obtenerJugadorActual().getNombre());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicialización de paneles
        inicializarPaneles();

        // Añadir ambos paneles principales a la ventana
        add(panelIzquierda, BorderLayout.WEST);
        add(panelDerecha, BorderLayout.EAST);

        // Mostrar la ventana
        setVisible(true);
    }

    // Métodos de inicialización de paneles
    private void inicializarPaneles() {
        inicializarPanelIzquierda();
        inicializarPanelDerecha();
    }

    private void inicializarPanelIzquierda() {
        panelIzquierda = new JPanel();
        panelIzquierda.setLayout(new BoxLayout(panelIzquierda, BoxLayout.Y_AXIS));
        panelIzquierda.setPreferredSize(new Dimension(250, 600));

        // Panel para fichas, jugadores, menú de acción y la mano del jugador
        panelFichas = new JPanel();
        panelJugadores = new JPanel();
        panelMenuAccion = new JPanel();
        panelManoJugador = new JPanel();

        // Configuración del menú de acción
        configurarMenuAccion();

        // Agregar paneles al panel izquierdo
        panelIzquierda.add(panelFichas);
        panelIzquierda.add(panelJugadores);
        panelIzquierda.add(panelManoJugador);
        //panelIzquierda.add(panelMenuAccion);

    }

    private void inicializarPanelDerecha() {
        panelDerecha = new JPanel();
        panelDerecha.setLayout(new BorderLayout());
        panelDerecha.setPreferredSize(new Dimension(700, 600));

        // Panel para mostrar manos de otros jugadores
        panelManosOtrosJugadores = new JPanel();
        panelManosOtrosJugadores.setLayout(new BoxLayout(panelManosOtrosJugadores, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelManosOtrosJugadores);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Manos de Otros Jugadores"));

        // Panel para castillos y mazo (abajo)
        panelCastillosYMazo = new JPanel();
        panelCastillosYMazo.setLayout(new BoxLayout(panelCastillosYMazo, BoxLayout.Y_AXIS));
        panelCastillosYMazo.setBorder(BorderFactory.createTitledBorder("Castillos y Mazo"));

        // Agregar subpaneles al panel derecho
        panelDerecha.add(scrollPane, BorderLayout.CENTER);
        panelDerecha.add(panelCastillosYMazo, BorderLayout.SOUTH);
    }


    private void configurarMenuAccion() {
        panelMenuAccion.setLayout(new GridLayout(3, 1, 10, 10));
        panelMenuAccion.setBorder(BorderFactory.createTitledBorder("Menú de Acción"));

        // Botones del menú
        JButton btnJugarCarta = new JButton("Jugar Carta");
        JButton btnDescartarCarta = new JButton("Descartar Carta");
        JButton btnDarPista = new JButton("Dar Pista");

        btnJugarCarta.addActionListener(e -> {
            System.out.println("Menú: seleccionaste JUGAR.");
            seleccionarCartaMano(
                    controlador.obtenerManoJugadorNoVisible(jugador),
                    (callbackSeleccionCarta) -> System.out.println("Seleccionaste para jugar: " + callbackSeleccionCarta),
                    "jugar" // Acción explícita
            );
        });

        btnDescartarCarta.addActionListener(e -> {
            System.out.println("Menú: seleccionaste DESCARTAR.");
            seleccionarCartaMano(
                    controlador.obtenerManoJugadorNoVisible(jugador),
                    (callbackSeleccionCarta) -> System.out.println("Seleccionaste para descartar: " + callbackSeleccionCarta),
                    "descartar" // Acción explícita
            );
        });

        btnDarPista.addActionListener(e -> {
            System.out.println("Menú: seleccionaste DAR PISTA.");
            seleccionarJugadorYCarta(
                    controlador.retornarManosVisiblesJugadores(jugador, controlador.obtenerListaJugadores()), // Obtenemos la lista de jugadores con sus cartas visibles
                    (selectedEntry) -> {
                        // Obtenemos el jugador y la carta seleccionada
                        Jugador jugadorSeleccionado = selectedEntry.getKey();
                        Carta cartaSeleccionada = selectedEntry.getValue();

                        // Muestra en consola el jugador y la carta seleccionada
                        System.out.println("Seleccionaste la carta: " + cartaSeleccionada + " del jugador " + jugadorSeleccionado.getNombre());

                        // Interactuar con el usuario para elegir el tipo de pista
                        String[] opciones = {"Color", "Número"};
                        int opcionElegida = JOptionPane.showOptionDialog(
                                null,
                                "¿Qué tipo de pista deseas dar?",
                                "Seleccionar Tipo de Pista",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opciones,
                                opciones[0]
                        );

                        // Validar la elección del usuario
                        if (opcionElegida == 0) { // Opción "Color"
                            System.out.println("Elegiste dar una pista de COLOR.");
                            Pista pista = controlador.crearPista(TipoPista.COLOR, cartaSeleccionada.getColor());
                            controlador.jugadorDaPista(jugadorSeleccionado, pista);
                        } else if (opcionElegida == 1) { // Opción "Número"
                            System.out.println("Elegiste dar una pista de NÚMERO.");
                            Pista pista = controlador.crearPista(TipoPista.NUMERO, cartaSeleccionada.getNumero());
                            controlador.jugadorDaPista(jugadorSeleccionado, pista);
                        } else {
                            System.out.println("No seleccionaste ningún tipo de pista.");
                        }
                    }
            );
        });

        // Agregar botones al menú
        panelMenuAccion.add(btnJugarCarta);
        panelMenuAccion.add(btnDescartarCarta);
        panelMenuAccion.add(btnDarPista);
    }

    @Override
    public void iniciar() {
        actualizarVista();

    }

    public void actualizarVista() {


        actualizarFichas(
                controlador.obtenerFichasDePistaDisponibles(),
                controlador.obtenerFichasDePistaUsadas(),
                controlador.obtenerFichasDeVidaDisponibles()
        );
        actualizarCastillosYMazo(
                controlador.ObtenerCastilloDeCartas(),
                controlador.obtenerMazo()
        );
        actualizarJugadores(controlador.obtenerListaJugadores());

        actualizarCartasMano(controlador.obtenerManoJugadorNoVisible(jugador));

        actualizarManoJugadoresResto(controlador.retornarManosVisiblesJugadores(jugador, controlador.obtenerListaJugadores()));



    }



    public void actualizarFichas(int pistasDisponibles, int pistasUsadas, int vida) {
        panelFichas.removeAll();
        panelFichas.add(new JLabel("Fichas de pista disponibles: " + pistasDisponibles));
        panelFichas.add(new JLabel("Fichas de pista usadas: " + pistasUsadas));
        panelFichas.add(new JLabel("Fichas de vida: " + vida));
        panelFichas.revalidate();
        panelFichas.repaint();
    }

    public void actualizarCastillosYMazo(List<CastilloDeCartas> castillos, int cartasEnMazo) {
        panelCastillosYMazo.removeAll();

        // Mostrar castillos
        JPanel panelCastillos = new JPanel(new GridLayout(1, castillos.size()));
        for (CastilloDeCartas castillo : castillos) {
            // Crear un panel individual para cada castillo con su color de fondo
            JPanel panelCastillo = new JPanel();
            panelCastillo.setBackground(getColorFondo(castillo.getColor())); // Usa tu método getColorFondo
            panelCastillo.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Opcional: agregar borde

            // Agregar información del castillo
            panelCastillo.add(new JLabel(castillo.getColor() + ": " + castillo.getCartas().size() + " cartas"));

            panelCastillos.add(panelCastillo);
        }

        // Mostrar mazo
        JPanel panelMazo = new JPanel();
        panelMazo.add(new JLabel("Cartas en el mazo: " + cartasEnMazo));

        // Agregar subpaneles
        panelCastillosYMazo.add(panelCastillos);
        panelCastillosYMazo.add(panelMazo);

        panelCastillosYMazo.revalidate();
        panelCastillosYMazo.repaint();
    }






    public void seleccionarCartaMano(List<Carta> mano, Consumer<Carta> callback, String accion) {
        // Restablecer el estado para un nuevo turno
        banderaSeleccionCarta = false;

        // Limpiar el panel y preparar la vista
        panelManoJugador.removeAll();

        // Crear los paneles para cada carta
        for (Carta carta : mano) {
            JPanel cartaPanel = crearCartaPanel(carta, 40, 70);

            // Agregar listener para detectar clics
            cartaPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    e.consume(); // Evitar propagación de eventos

                    if (banderaSeleccionCarta) {
                        System.out.println("Carta ya seleccionada. Ignorando clic.");
                        return; // Evitar múltiples selecciones
                    }

                    System.out.println("Carta seleccionada: " + carta);

                    // Ejecutar la acción correspondiente
                    if (callback != null) {
                        callback.accept(carta);

                        switch (accion.toLowerCase()) {
                            case "jugar":
                                System.out.println("Menú: seleccionaste JUGAR.");
                                controlador.jugadorJuegaCarta(jugador, carta);
                                break;
                            case "descartar":
                                System.out.println("Menú: seleccionaste Descartar.");
                                controlador.jugadorDescartaCarta(jugador, carta);
                                break;
                            default:
                                System.out.println("Acción no reconocida: " + accion);
                        }

                        // Bloquear más selecciones y deshabilitar la interacción
                        banderaSeleccionCarta = true;
                        deshabilitarSeleccionDeCartas();
                    }
                }
            });

            // Agregar el panel de la carta al panel principal
            panelManoJugador.add(cartaPanel);
        }

        // Redibujar la vista
        panelManoJugador.revalidate();
        panelManoJugador.repaint();

        System.out.println("Panel actualizado para la nueva selección.");
    }

    public void seleccionarJugadorYCarta(List<Map<Jugador, List<Carta>>> listaJugadores, Consumer<Map.Entry<Jugador, Carta>> callback) {
        // Limpiar el panel de las manos de otros jugadores
        panelManosOtrosJugadores.removeAll();

        // Recorrer la lista de jugadores con sus cartas
        for (Map<Jugador, List<Carta>> mapa : listaJugadores) {
            // Recorrer cada entrada de la lista de jugadores
            for (Map.Entry<Jugador, List<Carta>> jugadorEntry : mapa.entrySet()) {
                Jugador jugador = jugadorEntry.getKey();
                List<Carta> cartas = jugadorEntry.getValue();

                // Crear un panel para cada jugador
                JPanel jugadorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jugadorPanel.add(new JLabel(jugador.getNombre() + ":"));

                // Crear un panel para cada carta del jugador
                for (Carta carta : cartas) {
                    JPanel cartaPanel = crearCartaPanel(carta, 70, 100);

                    // Agregar listener para detectar clics
                    cartaPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            e.consume(); // Evitar propagación de eventos

                            System.out.println("Jugador: " + jugador + " Carta seleccionada: " + carta);

                            // Ejecutar la acción correspondiente y llamar al callback con el jugador y la carta seleccionada
                            if (callback != null) {
                                callback.accept(new AbstractMap.SimpleEntry<>(jugador, carta));

                                // Bloquear más selecciones y deshabilitar la interacción
                                deshabilitarSeleccionDeCartas();
                            }
                        }
                    });

                    // Agregar la carta al panel del jugador
                    jugadorPanel.add(cartaPanel);
                }

                // Agregar el panel del jugador al panel general de manos de otros jugadores
                panelManosOtrosJugadores.add(jugadorPanel);
            }
        }

        // Redibujar la vista
        panelManosOtrosJugadores.revalidate();
        panelManosOtrosJugadores.repaint();

        System.out.println("Panel actualizado para la nueva selección.");
    }





    private void deshabilitarSeleccionDeCartas() {
        for (Component comp : panelManoJugador.getComponents()) {
            if (comp instanceof JPanel) {
                // Deshabilitar solo la interacción visual
                comp.setEnabled(false);

                // Eliminar todos los listeners asociados
                for (MouseListener listener : comp.getMouseListeners()) {
                    comp.removeMouseListener(listener);
                }
            }
        }
    }

    public void actualizarCartasMano(List<Carta> mano) {

        panelManoJugador.removeAll();
        for (Carta carta : mano) {
            JPanel cartaPanel = crearCartaPanel(carta, 40, 60);
            panelManoJugador.add(cartaPanel);
        }
        panelManoJugador.revalidate();
        panelManoJugador.repaint();
    }



    public void actualizarManoJugadoresResto(List<Map<Jugador, List<Carta>>> listaJugadoresYManos) {
        panelManosOtrosJugadores.removeAll();
        for (Map<Jugador, List<Carta>> mapa : listaJugadoresYManos) {
            for (Map.Entry<Jugador, List<Carta>> entry : mapa.entrySet()) {
                Jugador jugador = entry.getKey();
                List<Carta> cartas = entry.getValue();
                JPanel jugadorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jugadorPanel.add(new JLabel(jugador.getNombre() + ":"));
                for (Carta carta : cartas) {
                    JPanel cartaPanel = crearCartaPanel(carta, 60, 90);
                    jugadorPanel.add(cartaPanel);
                }
                panelManosOtrosJugadores.add(jugadorPanel);
            }
        }
        panelManosOtrosJugadores.revalidate();
        panelManosOtrosJugadores.repaint();
    }

    private JPanel crearCartaPanel(Carta carta, int width, int height) {
        JPanel cartaPanel = new JPanel();
        cartaPanel.setPreferredSize(new Dimension(width, height));
        cartaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cartaPanel.setLayout(new BorderLayout());
        cartaPanel.setBackground(getColorFondo(carta.getColor()));

        JLabel label = carta.esRevelada()
                ? new JLabel(String.valueOf(carta.getNumero()), SwingConstants.CENTER)
                : new JLabel("Oculta", SwingConstants.CENTER);

        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        cartaPanel.add(label, BorderLayout.CENTER);

        return cartaPanel;
    }

    private Color getColorFondo(ColorCarta colorCarta) {
        switch (colorCarta) {
            case AZUL: return new Color(58, 154, 217);
            case ROJO: return new Color(217, 74, 58);
            case VERDE: return new Color(58, 217, 87);
            case AMARILLO: return new Color(217, 212, 58);
            case BLANCO: return Color.WHITE;
            default: return Color.GRAY;
        }
    }
    @Override
    public void mostrarPuntuacion(){
        int puntos = controlador.obtenerPuntuacion();
        mostrarMensaje(puntos + " puntos");
    }

    private void actualizarJugadores(List<Jugador> listaJugadores) {
        panelJugadores.removeAll();
        for (Jugador jugador : listaJugadores) {
            panelJugadores.add(new JLabel(jugador.getNombre()));
        }
        panelJugadores.revalidate();
        panelJugadores.repaint();
    }

    @Override
    public void mostrarMenuDeAccion(){
        System.out.println("Bandera");

        if (controlador.obtenerJugadorActual() == jugador) {
            panelIzquierda.add(panelMenuAccion);
        } else {
            panelIzquierda.remove(panelMenuAccion);
        }
        mostrarMensaje("Le Toca a: " + controlador.obtenerJugadorActual().getNombre());
    }






    @Override
    public void mostrarMensaje(String mensaje) {
        mostrarNotificacionEmergente(mensaje);
    }

    @Override
    public void setControlador(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
    }


    private void mostrarNotificacionEmergente(String mensaje) {
        // Cerrar la notificación anterior si existe
        if (notificacionActiva != null && notificacionActiva.isVisible()) {
            notificacionActiva.dispose();
        }

        // Crear el nuevo diálogo
        notificacionActiva = new JDialog(this, false);
        notificacionActiva.setUndecorated(true);
        notificacionActiva.setLayout(new BorderLayout());

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(new Color(0, 128, 255));
        lblMensaje.setForeground(Color.WHITE);
        lblMensaje.setFont(lblMensaje.getFont().deriveFont(Font.BOLD, 14f));
        notificacionActiva.add(lblMensaje, BorderLayout.CENTER);

        notificacionActiva.pack();
        Point location = calcularUbicacionNotificacion(notificacionActiva);
        notificacionActiva.setLocation(location);

        notificacionActiva.setVisible(true);

        // Cerrar automáticamente la notificación después de 3 segundos
        new Timer(3000, e -> notificacionActiva.dispose()).start();
    }

    private Point calcularUbicacionNotificacion(JDialog notificacion) {
        // Obtener las dimensiones de la ventana principal y de la notificación
        int x = this.getWidth() - notificacion.getWidth() - 20;
        int y = this.getHeight() - notificacion.getHeight() - 20;

        // Convertir a coordenadas absolutas de la pantalla
        Point locationOnScreen = this.getLocationOnScreen();
        return new Point(locationOnScreen.x + x, locationOnScreen.y + y);
    }

}
