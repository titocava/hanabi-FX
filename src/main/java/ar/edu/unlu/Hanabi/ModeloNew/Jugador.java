package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Jugador implements Serializable {
    private List<Carta> mano;
    //private boolean turno;
    private final String nombre;
    private final String id;
    @Serial
    private static final long serialVersionUID = 3L;


    public Jugador(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        //this.turno = false;
        this.id = generarIdDesdeNombre(nombre);
    }


    public String getId() {
        return id;
    }

    public void agregarCartaACartasEnMano(Carta carta) {
        if (carta != null) {
            mano.add(carta);
        }
    }

    public void setMano(List <Carta> mano) {
        this.mano = mano;
    }

    public void eliminarCartaDeLaMano(Carta carta) {
        mano.remove(carta);
    }

    public List<Carta> getMano() {
        return mano;
    }

    /*public void setJugadorTurno(boolean turno) {
        this.turno = turno;
    }*/

    public String getNombre() {
        return nombre;
    }

    private String generarIdDesdeNombre(String nombre) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(nombre.getBytes(StandardCharsets.UTF_8));
            StringBuilder idBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                idBuilder.append(String.format("%02x", b));
            }
            return idBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar ID desde el nombre", e);
        }
    }
}
