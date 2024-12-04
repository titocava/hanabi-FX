package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JuegoMostrable implements Serializable {
    private final JuegoHanabi juegoHanabi;


    public JuegoMostrable(JuegoHanabi juegoHanabi) {
        this.juegoHanabi = juegoHanabi;


    }

    public List<Carta> obtenerManoJugador1(Jugador jugador) {

        return jugador.getMano();
    }

    public List<Carta> obtenerManoJugadorVisible(Jugador jugador) {
        List<Carta> manoVisible = new ArrayList<>();
        for (Carta carta : jugador.getMano()) {
            Carta cartaVisible = new Carta(carta.getColor(), carta.getNumero());
            cartaVisible.revelar();
            manoVisible.add(cartaVisible);
        }
        return manoVisible;
    }





    public List<Map<Jugador, List<Carta>>> obtenerManosRestantesJugadores(Jugador jugadorInstanciado, List<Jugador> listaJugadores) {
        List<Map<Jugador, List<Carta>>> manosVisiblesRestantes = new ArrayList<>();
        for (Jugador jugador : listaJugadores) {
            if (jugador.equals(jugadorInstanciado)) {
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





    public int obtenerCartasRestantesEnMazo() {
        return juegoHanabi.getTablero().getMazoActual();
    }

    public int obtenerFichasDeVida() {
        return juegoHanabi.getTablero().obtenerFichasDeVida();
    }

    public int obtenerFichasDePistaUsadas() {
        return juegoHanabi.getTablero().obtenerFichasDePistaUsadas();
    }

    public int obtenerFichasDePista() {
        return juegoHanabi.getTablero().obtenerFichasDePista();
    }

    public List<CastilloDeCartas> obtenerCastillos() {
        return juegoHanabi.getTablero().getCastillos();
    }
    //public List<Jugador> listaJugadores() {return juegoHanabi.getJugadores();}



}
