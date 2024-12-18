package ar.edu.unlu.Hanabi.ModeloNew;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface IJuegoHanabiRemoto extends IObservableRemoto {

    Jugador registrarJugador(String nombreJugador) throws RemoteException;

    void jugadorJuegaCarta(String IdJugador, Carta carta) throws RemoteException;

    void iniciarJuego() throws RemoteException;

    String getJugadorActual() throws RemoteException;

    void jugadorDaPista(String jugadorObjetivo, Pista pista) throws RemoteException;

    void jugadorDescartaCarta(String idJugador, Carta carta) throws RemoteException;

    List<Jugador> getJugadores() throws RemoteException;

    Pista crearPista(TipoPista tipoPista, Object valor) throws RemoteException;

    int puntuacion() throws RemoteException;

    //void iniciarTurno()throws RemoteException;

    Tablero getTablero() throws RemoteException;

    List<Carta> obtenerManoJugador(String IdJugador) throws RemoteException;

    List<Map<Jugador, List<Carta>>> obtenerManosRestantesJugadores(String IdJugador, List<Jugador> lista) throws RemoteException;

    List<Object> obtenerDatosTablero() throws RemoteException;

    void guardarEstadoJuego() throws RemoteException;

    void cargarEstadoJuego() throws RemoteException;
    List<Object[]> leerHistorialJuego() throws RemoteException;
    void guardarHistorialJuego() throws RemoteException;
}
