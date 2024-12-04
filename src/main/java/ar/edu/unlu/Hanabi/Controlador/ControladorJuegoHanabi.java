package ar.edu.unlu.Hanabi.Controlador;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import ar.edu.unlu.Hanabi.Vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;





public class ControladorJuegoHanabi implements IControladorRemoto{

    private IJuegoHanabiRemoto modeloRemoto;
    private IJuegoHanabiRemoto juegoHanabi;
    private final JuegoMostrable juegoMostrable;
    private IVista vista;


    public ControladorJuegoHanabi(JuegoHanabi juegoHanabi, JuegoMostrable juegoMostrable) {
        this.juegoHanabi = juegoHanabi;
        this.juegoMostrable = juegoMostrable;


    }
    public void setVista(IVista vista) {

        //vistas.add(vista);
        this.vista = vista;
    }


    public void iniciarJuego() {

        try {
            juegoHanabi.iniciarJuego();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void iniciarTurno() {

        try {
            juegoHanabi.iniciarTurno();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }



    public int obtenerPuntuacion(){
        try {
            return juegoHanabi.puntuacion();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int obtenerFichasDePistaDisponibles() {
        return juegoMostrable.obtenerFichasDePista();
    }

    public int obtenerFichasDeVidaDisponibles() {
        return juegoMostrable.obtenerFichasDeVida();
    }

    public int obtenerFichasDePistaUsadas() {
        return juegoMostrable.obtenerFichasDePistaUsadas();
    }

    public List<CastilloDeCartas> ObtenerCastilloDeCartas() {return juegoMostrable.obtenerCastillos();}

    public List<Jugador> obtenerListaJugadores() {
        try {
            return juegoHanabi.getJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Carta> obtenerManoJugadorNoVisible(Jugador jugadorInstanciado) {
        return juegoMostrable.obtenerManoJugador1(jugadorInstanciado);
    }
    public List<Carta> obtenerManoJugadorVisible(Jugador jugadorInstanciado) {

        return juegoMostrable.obtenerManoJugadorVisible(jugadorInstanciado);
    }

    public List<Map<Jugador, List<Carta>>> retornarManosVisiblesJugadores(Jugador jugadorInstanciado, List<Jugador> lista) {

        return juegoMostrable.obtenerManosRestantesJugadores(jugadorInstanciado, lista);
    }
    public List<Object> obtenerDatosTablero() {
        List<Object> datosTablero = new ArrayList<>();
        datosTablero.add(juegoMostrable.obtenerCartasRestantesEnMazo());
        datosTablero.add(juegoMostrable.obtenerFichasDeVida());
        datosTablero.add(juegoMostrable.obtenerFichasDePistaUsadas());
        datosTablero.add(juegoMostrable.obtenerFichasDePista());
        datosTablero.add(juegoMostrable.obtenerCastillos());

        return datosTablero;
    }


    public  int obtenerMazo(){
        return juegoMostrable.obtenerCartasRestantesEnMazo();
    }

    public Jugador obtenerJugadorActual() {
        try {
            return juegoHanabi.getJugadorActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public String obtenerNombreJugadorReturn(Jugador jugador) {
        return jugador.getNombre();
    }

    public Pista crearPista(TipoPista tipoPista, Object valor) {
        try {
            return juegoHanabi.crearPista(tipoPista, valor);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void jugadorJuegaCarta(Jugador jugador, Carta carta) {


        try {
            juegoHanabi.jugadorJuegaCarta(jugador, carta);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

    public Jugador registrarJugador(String nombreJugador) {

        try {
            return juegoHanabi.registrarJugador(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

    public void jugadorDaPista(Jugador objetivo, Pista pista) {

        try {
            juegoHanabi.jugadorDaPista(objetivo, pista);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void jugadorDescartaCarta(Jugador jugador, Carta carta){

        try {
            juegoHanabi.jugadorDescartaCarta(jugador, carta);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /*@Override
    public void actualizar(Eventos evento) {
        //for (IVista vista : vistas) {
            switch (evento) {
                case INICIAR_JUEGO:
                    vista.actualizarVista();
                    vista.mostrarMenuDeAccion();
                    break;

                case CAMBIO_TURNO:
                    vista.mostrarMenuDeAccion();
                    break;

                case JUGADOR_JUGO_CARTA:
                    vista.actualizarVista();
                    break;

                case NO_HAY_PISTAS_USADAS:
                    vista.mostrarMensaje("Aún no tiene fichas de pistas usadas para descartar.");
                    break;

                case PISTA_DADA:
                    vista.mostrarMensaje("¡Pista dada!");
                    vista.actualizarVista();
                    break;

                case JUGADOR_DESCARTO_CARTA:
                    vista.mostrarMensaje("¡Has descartado una carta!");
                    vista.actualizarVista();
                    break;

                case VICTORIA:
                    vista.mostrarMensaje("¡Victoria!");
                    vista.mostrarPuntuacion();
                    break;

                case DERROTA:
                    System.out.println("DERROTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAasdadadasdas" );
                    vista.mostrarMensaje("¡Derrota!");
                    vista.mostrarPuntuacion();
                    System.out.println("puntos" + obtenerPuntuacion() );
                    break;

                case PUNTOS:
                default:
                    vista.mostrarMensaje("Evento no manejado: " + evento);
                    break;
            }
        //}
    }*/







    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juegoHanabi = (IJuegoHanabiRemoto) modeloRemoto; // es necesario castear el modelo remoto
    }

    @Override
    public void actualizar(IObservableRemoto arg0, Object arg1) throws RemoteException {
        System.out.println("actualizar");
        if (!(arg1 instanceof Eventos)) {
            System.out.println("Evento no válido: " + arg1);
            return;
        }
        Eventos evento = (Eventos) arg1;
        System.out.println(evento);
        switch (evento) {
            case INICIAR_JUEGO:
                System.out.println("bandera iniciar partida controlador" );
                vista.actualizarVista();
                vista.mostrarMenuDeAccion();
                break;

            case CAMBIO_TURNO:
                vista.mostrarMenuDeAccion();
                break;

            case JUGADOR_JUGO_CARTA:
                vista.actualizarVista();
                break;

            case NO_HAY_PISTAS_USADAS:
                vista.mostrarMensaje("Aún no tiene fichas de pistas usadas para descartar.");
                break;

            case PISTA_DADA:
                vista.mostrarMensaje("¡Pista dada!");
                vista.actualizarVista();
                break;

            case JUGADOR_DESCARTO_CARTA:
                vista.mostrarMensaje("¡Has descartado una carta!");
                vista.actualizarVista();
                break;

            case VICTORIA:
                vista.mostrarMensaje("¡Victoria!");
                vista.mostrarPuntuacion();
                break;

            case DERROTA:
                System.out.println("DERROTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAasdadadasdas" );
                vista.mostrarMensaje("¡Derrota!");
                vista.mostrarPuntuacion();
                System.out.println("puntos" + obtenerPuntuacion() );
                break;

            case PUNTOS:
                break;
            case JUGADOR_CREADO:
                System.out.println("bandera de jugador" );
                break;
            default:
                vista.mostrarMensaje("Evento no manejado: " + evento);
                break;
        }
    }





}