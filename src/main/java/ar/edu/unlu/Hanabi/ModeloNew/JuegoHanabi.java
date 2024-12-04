package ar.edu.unlu.Hanabi.ModeloNew;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JuegoHanabi extends ObservableRemoto implements IJuegoHanabiRemoto {
    private final List<Jugador> jugadores;
    private Tablero tablero;
    private int indiceTurnoActual;
    //private ArrayList<Observador> observadores = new ArrayList<>();

    public JuegoHanabi()  {
        this.jugadores = new ArrayList<>();
        this.tablero = new Tablero(jugadores);
        this.indiceTurnoActual = 1;

    }

    @Override
    public Jugador registrarJugador(String nombreJugador)throws RemoteException {
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            notificarObservadores(Eventos.ERROR_CREACION_JUGADOR);
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío.");

        }

        if (jugadores.size() >= 5) {
            notificarObservadores(Eventos.ERROR_CREACION_JUGADOR);
            throw new IllegalStateException("No se pueden registrar más de 5 jugadores.");

        }

        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                throw new IllegalArgumentException("El nombre '" + nombreJugador + "' ya está en uso.");
            }
        }

        Jugador nuevoJugador = new Jugador(nombreJugador);
        jugadores.add(nuevoJugador);

        notificarObservadores(new Evento(Eventos.JUGADOR_CREADO));
        System.out.println("asdadadasd" );
        return nuevoJugador;
    }


    private boolean juegoListoParaIniciar(){
        return jugadores.size() >= 2;
    }

    @Override
    public void iniciarJuego() throws RemoteException {
        if (jugadores == null || jugadores.size() < 2) {
            throw new IllegalStateException("No se puede iniciar el juego sin al menos 2 jugadores.");
        }

        //this.jugadores = new ArrayList<>(listaJugadores); // Asigna la lista recibida
        //this.tablero = new Tablero(jugadores);
        tablero.repartirCartas(jugadores);

        notificarObservadores(Eventos.INICIAR_JUEGO);
        iniciarTurno();
        System.out.println("Juego iniciado con los jugadores: " + jugadores);
    }


    public void iniciarTurno() throws RemoteException {
        if (jugadores.isEmpty()) {
            throw new IllegalStateException("No hay jugadores registrados.");
        }
        Jugador jugadorEnTurno = jugadores.get(indiceTurnoActual);
        jugadorEnTurno.setJugadorTurno(true);
        System.out.println("Es el turno de: " + jugadorEnTurno.getNombre());
    }

    public void cambiarTurno() throws RemoteException {
        jugadores.get(indiceTurnoActual).setJugadorTurno(false);
        indiceTurnoActual = (indiceTurnoActual + 1) % jugadores.size();
        jugadores.get(indiceTurnoActual).setJugadorTurno(true);
        System.out.println("Ahora es el turno de: " + jugadores.get(indiceTurnoActual).getNombre());
        notificarObservadores(Eventos.CAMBIO_TURNO);
    }

    public void verificarFinDeJuego() throws RemoteException {
        if (tablero.todosLosCastillosCompletos()) {
            notificarObservadores(Eventos.VICTORIA);
        } else if (tablero.getMazoActual() == 0 || tablero.obtenerFichasDeVida() == 0) {
            notificarObservadores(Eventos.DERROTA);
        }

    }



    @Override
    public Jugador getJugadorActual() throws RemoteException {
        return jugadores.get(indiceTurnoActual);
    }

    @Override
    public void jugadorJuegaCarta(Jugador jugador, Carta carta) throws RemoteException {
        if (jugador == null || carta == null) {
            notificarObservadores(Eventos.NO_ES_TURNO);
            throw new IllegalArgumentException("No es una carta jugable.");
        }
        tablero.jugarCarta(jugador, carta);
        tablero.cartaJugada(carta);
        tablero.tomarCarta(jugador);
        notificarObservadores(Eventos.JUGADOR_JUGO_CARTA);
        verificarFinDeJuego();
        cambiarTurno();


    }

    @Override
    public void jugadorDaPista(Jugador jugadorObjetivo, Pista pista) throws RemoteException {
        if (jugadorObjetivo == null || pista == null) {
            notificarObservadores(Eventos.NO_HAY_PISTA_DIPONIBLE);
            throw new IllegalArgumentException("El jugador objetivo y la pista no pueden ser nulos.");
        }
        tablero.darPista(jugadorObjetivo, pista);
        tablero.reducirFichaPista();
        notificarObservadores(Eventos.PISTA_DADA);
        cambiarTurno();

    }

    @Override
    public void jugadorDescartaCarta(Jugador jugador, Carta carta) throws RemoteException {
        if (jugador == null || carta == null) {
            notificarObservadores(Eventos.NO_ES_TURNO);
            throw new IllegalStateException("No puedes descartar una carta porque no hay fichas de pista disponibles.");
        }
        if (tablero.obtenerFichasDePistaUsadas() <= 0) {
            notificarObservadores(Eventos.NO_HAY_PISTAS_USADAS);
            return;
        }

        tablero.descartarCarta(jugador, carta);
        tablero.recuperarFichaPista();
        tablero.tomarCarta(jugador);
        notificarObservadores(Eventos.JUGADOR_DESCARTO_CARTA);
        verificarFinDeJuego();
        cambiarTurno();
    }

    @Override
    public List<Jugador> getJugadores() throws RemoteException {
        return new ArrayList<>(jugadores);
    }

    public Tablero getTablero() {
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





