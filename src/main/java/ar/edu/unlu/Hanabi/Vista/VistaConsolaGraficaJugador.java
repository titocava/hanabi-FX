package ar.edu.unlu.Hanabi.Vista;
import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.stream.Collectors;


public class VistaConsolaGraficaJugador  extends JFrame implements IVista {
    private ControladorJuegoHanabi controlador;
    private final JTextArea txtSalida;
    private final JTextField txtEntrada;
    private final JButton btnEnter;
    private EstadoVistaConsola estado;
    private final Jugador jugador;

    private List<?> listaActual;
    private Consumer<Object> callbackActual;

    private Jugador jugadorSeleccionadoParaPista;
    private TipoPista tipoPista;
    private Object valorPista;


    public VistaConsolaGraficaJugador(ControladorJuegoHanabi controlador, Jugador jugador) {
        this.controlador = controlador;
        this.jugador = jugador;
        this.controlador.setVista(this);
        this.txtSalida = new JTextArea();
        this.txtEntrada = new JTextField();
        this.btnEnter = new JButton("Enter");


        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtSalida);
        add(scrollPane, BorderLayout.CENTER);
        configurarVentana();
        configurarComponentes();
        configurarEventos();


    }

    private void configurarVentana() {
        setTitle("Hanabi - Juego en Consola - " + jugador.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void configurarComponentes() {
        JScrollPane scrollPane = new JScrollPane(txtSalida);
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(txtEntrada, BorderLayout.CENTER);
        panelInferior.add(btnEnter, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        // Configura el evento de clic en el botón
        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Procesar la entrada cuando se presiona Enter o se hace clic en el botón
                procesarEntrada(txtEntrada.getText().trim());
                txtEntrada.setText("");  // Limpia el campo de texto después de procesar
            }
        });

        // Configura el evento de presionar Enter en el campo de texto directamente
        txtEntrada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Procesar la entrada cuando el usuario presiona Enter
                procesarEntrada(txtEntrada.getText().trim());
                txtEntrada.setText("");  // Limpia el campo de texto
            }
        });

        // Asegura que el foco vuelva al campo de entrada
        txtEntrada.requestFocusInWindow();
    }






    private void procesarEntrada(String entrada) {
        txtEntrada.setText(""); // Limpia el campo de texto
        if (entrada.isEmpty()) {
            mostrarMensaje("Por favor, ingrese un comando.123123132131321");
            return;
        }

        // Procesar comandos según el estado actual
        switch (estado) {
            case MENU_ACCIONES_JUGADOR -> procesarComandoAccionesJugador(entrada);
            case MENU_PISTA -> ProcesarComandoSeleccionPista(entrada);
            case SELECCIONAR_OBJETO -> procesarSeleccionObjeto(entrada, EstadoVistaConsola.SELECCIONAR_OBJETO);
            case SELECCIONAR_PISTA -> procesarSeleccionObjeto(entrada, EstadoVistaConsola.SELECCIONAR_PISTA);

            default -> mostrarMensaje("Comando no reconocido.");
        }

        txtEntrada.setText("");
        txtEntrada.requestFocusInWindow();
    }


    private void procesarComandoAccionesJugador(String comando) {
        switch (comando) {
            case "1" -> jugarCarta();
            case "2" -> mostrarMenuSeleccionPista();
            case "3" -> descartarCarta();
            case "4" -> mostrarManoDeJugador();
            case "5" -> mostrarMenuDeAccion();
            default -> mostrarMensaje("Opción no válida. Elija '1', '2', '3', '4' o '5'.");
        }
    }

    private void ProcesarComandoSeleccionPista(String comando) {
        switch (comando) {
            case "1" -> seleccionarJugador();
            case "2" -> seleccionarTipoDePista();
            case "3" -> seleccionarValorPista();
            case "4" -> darPista();


            default -> mostrarMensaje("Opción no válida. Elija '1', '2', '3','4'.");
        }
    }

    private void mostrarMenuSeleccionPista() {
        mostrarMensaje("Selecciona una opción para dar una pista:");
        mostrarMensaje("1. Seleccionar jugador");
        mostrarMensaje("2. Seleccionar tipo de pista");
        mostrarMensaje("3. Seleccionar valor de la pista");
        mostrarMensaje("4. Dar la Pista");
        mostrarMensaje("Ingrese una opción (1, 2, 3, 4):");
        estado = EstadoVistaConsola.MENU_PISTA;
    }

    private void jugarCarta() {
        List<Carta> manoJugador = controlador.obtenerManoJugadorNoVisible(controlador.obtenerIdJugador(jugador));

        if (manoJugador == null || manoJugador.isEmpty()) {
            mostrarMensaje("No hay cartas disponibles para jugar.");
            return;
        }
        iniciarSelector("Seleccione una carta para jugar:", manoJugador, cartaSeleccionada -> {
            mostrarMensaje("Has seleccionado: " + cartaSeleccionada);
            controlador.jugadorJuegaCarta(controlador.obtenerIdJugador(jugador), (Carta) cartaSeleccionada);

        });
    }

    private void darPista() {
        Pista pistita = controlador.crearPista(tipoPista, valorPista);
        mostrarMensaje("bandera" + jugadorSeleccionadoParaPista.getNombre());
        controlador.jugadorDaPista(controlador.obtenerIdJugador(jugadorSeleccionadoParaPista), pistita );
        mostrarMensaje("bandera1");

    }

    private void descartarCarta() {
        if (controlador.obtenerFichasDePistaUsadas() >= 1) {
            mostrarMensaje("Selecciona una carta para descartar.");
            List<Carta> manoJugador = controlador.obtenerManoJugadorNoVisible(controlador.obtenerIdJugador(jugador));
            iniciarSelector("Seleccione una carta para jugar:", manoJugador, cartaSeleccionada -> {
                        mostrarMensaje("Has seleccionado: " + cartaSeleccionada);
                        controlador.jugadorDescartaCarta(controlador.obtenerIdJugador(jugador), (Carta) cartaSeleccionada);
                    }
            );
        } else {
            mostrarMensaje("Aún no tiene fichas de pistas usadas para descartar.");
            mostrarMenuDeAccion();
        }
    }


    public void mostrarInformacionDeJugadoresInicial() {
        procesarDatosTablero(controlador.obtenerDatosTablero());
        procesarMostrarManos(controlador.retornarManosVisiblesJugadores(controlador.obtenerIdJugador(jugador), controlador.obtenerListaJugadores()));
        procesarMostrarCartasDeMano(controlador.obtenerManoJugadorNoVisible(controlador.obtenerIdJugador(jugador)));
    }

    public void mostrarManoDeJugador(){
        procesarMostrarCartasDeMano(controlador.obtenerManoJugadorNoVisible(controlador.obtenerIdJugador(jugador)));
        procesarMostrarManos(controlador.retornarManosVisiblesJugadores(controlador.obtenerIdJugador(jugador), controlador.obtenerListaJugadores()));

    }


    public void procesarDatosTablero(List<Object> datos) {
        mostrarMensaje("Cartas restantes en el mazo: " + datos.get(0));
        mostrarMensaje("Fichas de vida: " + datos.get(1));
        mostrarMensaje("Fichas de pista usadas: " + datos.get(2));
        mostrarMensaje("Fichas de pista: " + datos.get(3));

        List<CastilloDeCartas> castillos = (List<CastilloDeCartas>) datos.get(4);
        if (castillos.isEmpty()) {
            mostrarMensaje("No hay castillos en el tablero.");
        } else {
            for (CastilloDeCartas castillo : castillos) {
                mostrarMensaje("Castillo para color " + castillo.getColor() + ": " + castillo.getCartas());
            }
        }
    }
    public void mostrarMenuDeAccion() {
        String IdjugadorActual = controlador.obtenerJugadorActual();
        if (IdjugadorActual.equals(this.jugador.getId())) {
            mostrarMensaje("Menú principal:\n1. Jugar carta\n2. Dar pista\n3. Descartar carta\n4. Ver tu mano\n5. Salir");
            estado = EstadoVistaConsola.MENU_ACCIONES_JUGADOR;
        }else{
            mostrarMensaje("Espera tu turno.");
        }
    }

    @Override
    public void mostrarPuntuacion(){
        int puntos = controlador.obtenerPuntuacion();
        mostrarMensaje(puntos + " puntos");
        System.out.println("puntos" + puntos );
    }

    @Override
    public void setControlador(ControladorJuegoHanabi controlador) {
        this.controlador = controlador;
    }


    @Override
    public void mostrarMensaje(String mensaje) {
        txtSalida.append(mensaje + "\n");
        txtSalida.setCaretPosition(txtSalida.getDocument().getLength());
    }

    @Override
    public void actualizarVista() {
        txtSalida.setText("");
        mostrarInformacionDeJugadoresInicial();

    }

    @Override
    public void iniciar() {
        setVisible(true);
    }
    @Override
    public void deshabilitarMenuAccion() {

    }



    public void procesarMostrarManos(List<Map<Jugador, List<Carta>>> listaManos) {
        if (listaManos.isEmpty()) {
            mostrarMensaje("No hay manos visibles para mostrar.");
            return;}
        for (Map<Jugador, List<Carta>> manos : listaManos) {
            for (Map.Entry<Jugador, List<Carta>> entry : manos.entrySet()) {
                Jugador jugador = entry.getKey();
                List<Carta> mano = entry.getValue();
                mostrarMensaje("Mano de " + jugador.getNombre() + ":");
                if (mano.isEmpty()) {
                    mostrarMensaje("  Sin cartas.");
                } else {
                    for (Carta carta : mano) {
                        mostrarMensaje("  " + carta.toString());
                    }
                }
                mostrarMensaje("====================");
            }
        }
    }



    public void procesarMostrarCartasDeMano(List<Carta> cartas) {
        if (cartas.isEmpty()) {
            mostrarMensaje("La mano está vacía.");
            return;
        }
        mostrarMensaje("Cartas Mano:");
        for (int i = 0; i < cartas.size(); i++) {
            Carta carta = cartas.get(i);
            if (carta.esRevelada()) {
                mostrarMensaje((i + 1) + ". " + carta.toString());
            } else {
                mostrarMensaje((i + 1) + ". Carta no visible");
            }
        }
        mostrarMensaje("--------------------");
    }

    private void iniciarSelector(String mensaje, List<?> lista, Consumer<Object> callback) {
        if (lista == null || lista.isEmpty()) {
            mostrarMensaje("No hay elementos disponibles para seleccionar.");
            return;}
        this.listaActual = lista;
        this.callbackActual = callback;
        estado = EstadoVistaConsola.SELECCIONAR_OBJETO;
        StringBuilder opciones = new StringBuilder(mensaje + "\n");
        for (int i = 0; i < lista.size(); i++) {
            Object item = lista.get(i);
            if (item instanceof Carta) {
                Carta carta = (Carta) item;
                if (carta.esRevelada()) {
                    opciones.append(i + 1).append(". ").append(carta.toString()).append("\n");
                } else {
                    opciones.append(i + 1).append(". Carta oculta\n");
                }
            } else {
                opciones.append(i + 1).append(". ").append(item.toString()).append("\n");
            }
        }
        opciones.append("Seleccione una opción:");
        mostrarMensaje(opciones.toString());
    }


    private void iniciarSelectorPista(String mensaje, List<?> lista, Consumer<Object> callback) {
        if (lista == null || lista.isEmpty()) {
            mostrarMensaje("No hay elementos disponibles para seleccionar.");
            return;
        }
        this.listaActual = lista;
        this.callbackActual = callback;
        estado = EstadoVistaConsola.SELECCIONAR_PISTA;
        StringBuilder opciones = new StringBuilder(mensaje + "\n");
        for (int i = 0; i < lista.size(); i++) {
            opciones.append(i + 1).append(". ").append(lista.get(i).toString()).append("\n");
        }
        opciones.append("Seleccione una opción:");
        mostrarMensaje(opciones.toString());
    }

    private void procesarSeleccionObjeto(String entrada, EstadoVistaConsola estado) {
        try {
            int opcion = Integer.parseInt(entrada.trim()) - 1;
            if (opcion >= 0 && opcion < listaActual.size()) {
                Object seleccionado = listaActual.get(opcion);
                mostrarMensaje("Has seleccionado: " + seleccionado.toString());
                callbackActual.accept(seleccionado);
                if (estado == EstadoVistaConsola.SELECCIONAR_OBJETO) {
                    mostrarMenuDeAccion();
                } else if (estado == EstadoVistaConsola.SELECCIONAR_PISTA) {
                    mostrarMenuSeleccionPista();
                }
            } else {
                mostrarMensaje("Número fuera de rango. Intente nuevamente.");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Entrada no válida. Por favor, ingrese un número.");
        }
    }




    private List<Jugador> obtenerListaDeJugadoresExcluyendoActual(Jugador jugadorActual) {
        List<Jugador> listaDeJugadores = controlador.obtenerListaJugadores();
        String idJugadorActual = controlador.obtenerIdJugador(jugadorActual);
        return listaDeJugadores.stream()
                .filter(jugador -> !controlador.obtenerIdJugador(jugador).equals(idJugadorActual))
                .collect(Collectors.toList());
    }





    private void seleccionarJugador() {
        List<Jugador> listaDeJugadores = obtenerListaDeJugadoresExcluyendoActual(jugador);
        iniciarSelectorPista("Seleccione un jugador para dar una pista:", listaDeJugadores, jugadorSeleccionado -> {
            mostrarMensaje("Jugador seleccionado: " + controlador.obtenerNombreJugadorReturn((Jugador) jugadorSeleccionado));
            jugadorSeleccionadoParaPista = (Jugador) ((Jugador) jugadorSeleccionado);
        });


    }

    private void seleccionarTipoDePista() {
        List<TipoPista> opcionesTipoPista = Arrays.asList(TipoPista.values());
        iniciarSelectorPista("Seleccione el tipo de pista (COLOR o NUMERO):", opcionesTipoPista, tiposeleccionado -> {
            mostrarMensaje("Tipo de pista seleccionado: " + tiposeleccionado);
            tipoPista = (TipoPista) tiposeleccionado;
        });
    }


    private void seleccionarValorPista() {
        List<Carta> manoJugadorSeleccionado = controlador.obtenerManoJugadorNoVisible(controlador.obtenerIdJugador(jugadorSeleccionadoParaPista));

        iniciarSelectorPista("Seleccione una carta de la mano del jugador para dar una pista:", manoJugadorSeleccionado, cartaSeleccionada -> {
            if (!(cartaSeleccionada instanceof Carta carta)) {
                mostrarMensaje("Selección inválida. Por favor, seleccione una carta válida.");
                return;
            }
            Object valorDePistaSeleccionado;
            if (tipoPista == TipoPista.COLOR) {
                valorDePistaSeleccionado = carta.getColor();
            } else if (tipoPista == TipoPista.NUMERO) {
                valorDePistaSeleccionado = carta.getNumero();
            } else {
                mostrarMensaje("Error: Tipo de pista no definido.");
                return;
            }
            mostrarMensaje("Valor de pista seleccionado: " + valorDePistaSeleccionado);
            valorPista = valorDePistaSeleccionado;
        });
    }

}

