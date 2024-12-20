package ar.edu.unlu.Hanabi.Controlador;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import ar.edu.unlu.Hanabi.Vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;


import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


public class ControladorJuegoHanabi implements IControladorRemoto{
    private IJuegoHanabiRemoto juegoHanabi;
        private IVista vista;


    public ControladorJuegoHanabi() {
    }
    public void setVista(IVista vista) {
        this.vista = vista;
    }


    public void iniciarJuego() {

        try {
            juegoHanabi.iniciarJuego();
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
        try {
            return juegoHanabi.getTablero().obtenerFichasDePista();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int obtenerFichasDeVidaDisponibles() {
        try {
            return juegoHanabi.getTablero().obtenerFichasDeVida();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int obtenerFichasDePistaUsadas() {
        try {
            return juegoHanabi.getTablero().obtenerFichasDePistaUsadas();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CastilloDeCartas> ObtenerCastilloDeCartas() {
        try {
            return juegoHanabi.getTablero().getCastillos();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Jugador> obtenerListaJugadores() {
        try {
            return juegoHanabi.getJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Carta> obtenerManoJugadorNoVisible(String IdJugador) {
        try {
            return juegoHanabi.obtenerManoJugador(IdJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Map<Jugador, List<Carta>>> retornarManosVisiblesJugadores(String IdJugador, List<Jugador> lista) {

        try {
            return juegoHanabi.obtenerManosRestantesJugadores(IdJugador, lista);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }



    public  int obtenerMazo(){
        try {
            return juegoHanabi.getTablero().getMazoActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String obtenerJugadorActual() {
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


    public void jugadorJuegaCarta(String IdJugador, Carta carta) {
        try {
            juegoHanabi.jugadorJuegaCarta(IdJugador, carta);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }}

    public Jugador registrarJugador(String nombreJugador) {
        try {
            return juegoHanabi.registrarJugador(nombreJugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }}

    public void jugadorDaPista(String jugadorObjetivo, Pista pista) {
        try {
            juegoHanabi.jugadorDaPista(jugadorObjetivo, pista);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }}

    public void jugadorDescartaCarta(String idJugador, Carta carta){
        try {
            juegoHanabi.jugadorDescartaCarta(idJugador, carta);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }}

    public String obtenerIdJugador(Jugador jugadorVista) {
        try {
            for (Jugador jugador : juegoHanabi.getJugadores()) {
                if (jugador.getId().equals(jugadorVista.getId())) {
                    return jugador.getId();
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public List<Object> obtenerDatosTablero(){
        try {
            return juegoHanabi.obtenerDatosTablero();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarJuego() {
        try {
            juegoHanabi.guardarEstadoJuego();
            System.out.println("Juego guardado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al guardar el juego: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void cargarJuego() {
        try {
            juegoHanabi.cargarEstadoJuego();
            System.out.println("Juego cargado y estado restaurado exitosamente desde ");
        } catch (RemoteException e) {
            System.err.println("Error al cargar el juego: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void guardarHistorialJuego() {
        try {
            juegoHanabi.guardarHistorialJuego();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

   public String mostrarRanking() throws RemoteException {
        try {
            return juegoHanabi.mostrarRanking();

        }catch (RemoteException e) {
            throw new RuntimeException(e);
        }


   }


    public List<Object[]>  cargarHistorialJuego() {
        try {
            return juegoHanabi.leerHistorialJuego();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juegoHanabi = (IJuegoHanabiRemoto) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto arg0, Object arg1) throws RemoteException {
        Eventos evento = (Eventos) arg1;
        System.out.println(evento);
        switch (evento) {
            case INICIAR_JUEGO:
                vista.actualizarVista();
                vista.mostrarMenuDeAccion();
                break;

            case CAMBIO_TURNO:
                vista.mostrarMenuDeAccion();
                break;

            case JUGADOR_JUGO_CARTA:
                vista.mostrarMensaje("¡Has jugado una carta!");
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
                vista.deshabilitarMenuAccion();
                vista.actualizarVista();
                vista.mostrarMensaje("¡Victoria!");
                vista.mostrarPuntuacion();
                break;

            case DERROTA:
                vista.actualizarVista();
                vista.deshabilitarMenuAccion();
                System.out.println("DERROTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" );
                vista.mostrarMensaje("¡Derrota!");
                vista.mostrarPuntuacion();
                break;

            case PUNTOS:

                break;
            case JUGADOR_CREADO:
                System.out.println("Jugador Creado" );
                break;
            default:
                vista.mostrarMensaje("Evento no manejado: " + evento);
                break;
        }
    }

}