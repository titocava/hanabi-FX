package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jugador implements Serializable {
    private final List<Carta> mano;
    private boolean turno;
    private final String nombre;

    public Jugador(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }
        this.nombre = nombre.trim();
        this.mano = new ArrayList<>();
        this.turno = false;
    }

    public void agregarCartaACartasEnMano(Carta carta) {
        if (carta != null) {
            mano.add(carta);
        }
    }


    public boolean eliminarCartaDeLaMano(Carta carta) {
        return mano.remove(carta);
    }

    public List<Carta> getMano() {
        return mano;
    }

    public void setJugadorTurno(boolean turno) {
        this.turno = turno;
    }

    public String getNombre() {
        return nombre;
    }

    public int obtenerCantidadDeCartasEnMano() {
        return mano.size();
    }
}

