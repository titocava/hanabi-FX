package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serializable;
import java.util.List;


public class JuegoHanabiEstado implements Serializable {
    private static final long serialVersionUID = 10L;
    private List<Jugador> jugadores;
    private Tablero tablero;

    public JuegoHanabiEstado(List<Jugador> jugadores, Tablero tablero) {
        this.jugadores = jugadores;
        this.tablero = tablero;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Tablero getTablero() {
        return tablero;
    }


}