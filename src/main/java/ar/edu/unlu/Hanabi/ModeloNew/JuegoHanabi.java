package ar.edu.unlu.Hanabi.ModeloNew;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class JuegoHanabi extends ObservableRemoto implements IJuegoHanabiRemoto, Serializable {
    private List<Jugador> jugadores;
    private Tablero tablero;
    private int indiceTurnoActual;
    @Serial
    private static final long serialVersionUID = 1L;


    public JuegoHanabi()  {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero();
        this.indiceTurnoActual = 0;
    }

    @Override
    public Jugador registrarJugador(String nombreJugador)throws RemoteException {
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            notificarObservadores(Eventos.ERROR_CREACION_JUGADOR);
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío.");}
        if (jugadores.size() >= 5) {
            notificarObservadores(Eventos.ERROR_CREACION_JUGADOR);
            throw new IllegalStateException("No se pueden registrar más de 5 jugadores.");
        }
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                throw new IllegalArgumentException("El nombre '" + nombreJugador + "' ya está en uso.");
            }}
        Jugador nuevoJugador = new Jugador(nombreJugador);
        jugadores.add(nuevoJugador);
        notificarObservadores(Eventos.JUGADOR_CREADO);
        return nuevoJugador;
    }


    @Override
    public void iniciarJuego() throws RemoteException {
        if (jugadores.size() < 2) {
            throw new IllegalStateException("No se puede iniciar el juego sin al menos 2 jugadores.");
        }
        tablero.repartirCartas(jugadores);
        notificarObservadores(Eventos.INICIAR_JUEGO);
        iniciarTurno();
        System.out.println("Juego iniciado con los jugadores: " + jugadores);
    }

    public void guardarEstadoJuego(String archivo) throws RemoteException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            JuegoHanabiEstado estado = new JuegoHanabiEstado(
                    getJugadores(),
                    getTablero()
            );
            oos.writeObject(estado);
            System.out.println("Estado del juego guardado exitosamente en " + archivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarEstadoJuego(String archivo) throws RemoteException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            JuegoHanabiEstado estadoCargado = (JuegoHanabiEstado) ois.readObject();
            this.setEstado(estadoCargado);
            notificarObservadores(Eventos.INICIAR_JUEGO);
            System.out.println("Estado del juego cargado exitosamente: " + estadoCargado);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RemoteException("Error al cargar el juego", e);
        }
    }
    public void setEstado(JuegoHanabiEstado estado) {
        this.jugadores = estado.getJugadores();
        this.tablero = estado.getTablero();
    }








    public void iniciarTurno() throws RemoteException {
        if (jugadores.isEmpty()) {
            throw new IllegalStateException("No hay jugadores registrados.");
        }
        Jugador jugadorEnTurno = jugadores.get(indiceTurnoActual);
        jugadorEnTurno.setJugadorTurno(true);
        System.out.println("Es el turno de: " + jugadorEnTurno.getNombre());
    }


    private void cambiarTurno() throws RemoteException {
        jugadores.get(indiceTurnoActual).setJugadorTurno(false);
        indiceTurnoActual = (indiceTurnoActual + 1) % jugadores.size();
        jugadores.get(indiceTurnoActual).setJugadorTurno(true);
        System.out.println("Ahora es el turno de: " + jugadores.get(indiceTurnoActual).getNombre());
        notificarObservadores(Eventos.CAMBIO_TURNO);
        verificarFinDeJuego();
    }

    public void verificarFinDeJuego() throws RemoteException {
        if (tablero.todosLosCastillosCompletos()) {
            notificarObservadores(Eventos.VICTORIA);
        } else if (tablero.getMazoActual() == 0 || tablero.obtenerFichasDeVida() == 0) {
            notificarObservadores(Eventos.DERROTA);
        }

    }


    public List<Carta> obtenerManoJugador(String idJugador) throws RemoteException {
        Jugador jugador = jugadores.stream()
                .filter(j -> j.getId().equals(idJugador))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado con ID: " + idJugador));
        System.out.println("obtenerManoJugador " + jugador.getNombre());
        return jugador.getMano();
    }




    public List<Map<Jugador, List<Carta>>> obtenerManosRestantesJugadores(String idJugadorInstanciado, List<Jugador> listaJugadores) throws RemoteException {
        List<Map<Jugador, List<Carta>>> manosVisiblesRestantes = new ArrayList<>();

        for (Jugador jugador : listaJugadores) {
            if (jugador.getId().equals(idJugadorInstanciado)) {
                continue;
            }
            List<Carta> manoRevelada = new ArrayList<>();
            for (Carta carta : jugador.getMano()) {
                Carta cartaRevelada = new Carta(carta.getColor(), carta.getNumero());
                cartaRevelada.revelar();
                manoRevelada.add(cartaRevelada);
            }
            Map<Jugador, List<Carta>> jugadorYMano = new HashMap<>();
            jugadorYMano.put(jugador, manoRevelada);
            manosVisiblesRestantes.add(jugadorYMano);
        }
        return manosVisiblesRestantes;
    }

    public Jugador obtenerJugadorPorId(String idJugador) {
        return jugadores.stream()
                .filter(j -> j.getId().equals(idJugador))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado con ID: " + idJugador));
    }

    public Carta obtenerCartaDeMano(String idJugador, Carta cartaBuscada) {
        Jugador jugador = obtenerJugadorPorId(idJugador);

        return jugador.getMano().stream()
                .filter(c -> c.getColor().equals(cartaBuscada.getColor()) && c.getNumero() == cartaBuscada.getNumero())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La carta no pertenece a la mano del jugador."));
    }

    public List<Object> obtenerDatosTablero() throws RemoteException {
        List<Object> datosTablero = new ArrayList<>();
        datosTablero.add(tablero.getMazoActual());
        datosTablero.add(tablero.obtenerFichasDeVida());
        datosTablero.add(tablero.obtenerFichasDePistaUsadas());
        datosTablero.add(tablero.obtenerFichasDePista());
        datosTablero.add(tablero.getCastillos());

        return datosTablero;
    }


    @Override
    public String getJugadorActual() throws RemoteException {
        return jugadores.get(indiceTurnoActual).getId();
    }


    @Override
    public void jugadorJuegaCarta(String idJugador, Carta carta) throws RemoteException {
        if (idJugador == null || carta == null) {
            notificarObservadores(Eventos.NO_ES_TURNO);
            throw new IllegalArgumentException("No es una carta jugable.");
        }
        Jugador jugador = obtenerJugadorPorId(idJugador);
        Carta cartaBuscada = obtenerCartaDeMano(idJugador, carta);
        if (jugador == null) {
            notificarObservadores(Eventos.NO_ES_TURNO);
            throw new IllegalArgumentException("Jugador no encontrado.");
        }
        tablero.jugarCarta(jugador, cartaBuscada);
        tablero.cartaJugada(cartaBuscada);
        tablero.tomarCarta(jugador);

        notificarObservadores(Eventos.JUGADOR_JUGO_CARTA);
        cambiarTurno();

    }


    @Override
    public void jugadorDaPista(String idJugadorObjetivo, Pista pista) throws RemoteException {
        if (idJugadorObjetivo == null || pista == null) {
            throw new IllegalArgumentException("El jugador objetivo y la pista no pueden ser nulos.");
        }
        Jugador jugadorObjetivo = obtenerJugadorPorId(idJugadorObjetivo);
        tablero.darPista(jugadorObjetivo, pista);
        tablero.reducirFichaPista();
        notificarObservadores(Eventos.PISTA_DADA);
        cambiarTurno();

    }

    @Override
    public void jugadorDescartaCarta(String idJugador, Carta carta) throws RemoteException {
        if (idJugador == null || carta == null) {
            notificarObservadores(Eventos.NO_ES_TURNO);
            throw new IllegalArgumentException("No es una carta jugable.");
        }
        if (tablero.obtenerFichasDePistaUsadas() <= 0) {
            notificarObservadores(Eventos.NO_HAY_PISTAS_USADAS);
            return;
        }
        Jugador jugador = obtenerJugadorPorId(idJugador);
        Carta cartaBuscada = obtenerCartaDeMano(idJugador, carta);

        if (jugador == null || cartaBuscada == null) {
            notificarObservadores(Eventos.NO_ES_TURNO);
            throw new IllegalStateException("No puedes descartar una carta porque no hay fichas de pista disponibles.");
        }
        tablero.descartarCarta(jugador, cartaBuscada);
        tablero.recuperarFichaPista();
        tablero.tomarCarta(jugador);

        notificarObservadores(Eventos.JUGADOR_DESCARTO_CARTA);
        cambiarTurno();
    }

    @Override
    public List<Jugador> getJugadores() throws RemoteException {
        return jugadores;
    }

    public Tablero getTablero()throws RemoteException {
        return tablero;
    }

    public Pista crearPista(TipoPista tipoPista, Object valor)  {
        return new Pista(tipoPista, valor);


    }

    public int puntuacion() throws RemoteException {
        notificarObservadores(Eventos.PUNTOS);
        return tablero.calcularPuntos();
    }


}





