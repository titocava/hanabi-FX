package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Jugador implements Serializable {
    private final List<Carta> mano;
    private boolean turno;
    private final String nombre;
    private final String id;


    public Jugador(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.turno = false;
        this.id = generarIdUnico();
    }

    private String generarIdUnico() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
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
