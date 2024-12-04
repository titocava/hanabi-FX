package ar.edu.unlu.Hanabi.ModeloNew;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IJuegoHanabiRemoto extends IObservableRemoto {

    Jugador registrarJugador(String nombreJugador) throws RemoteException;

    void jugadorJuegaCarta(Jugador jugador, Carta carta) throws RemoteException;

    void iniciarJuego() throws RemoteException;

    Jugador getJugadorActual() throws RemoteException;

    void jugadorDaPista(Jugador jugadorObjetivo, Pista pista) throws RemoteException;

    void jugadorDescartaCarta(Jugador jugador, Carta carta) throws RemoteException;

    List<Jugador> getJugadores() throws RemoteException;

    Pista crearPista(TipoPista tipoPista, Object valor) throws RemoteException;

    int puntuacion() throws RemoteException;

    void iniciarTurno()throws RemoteException;
}
