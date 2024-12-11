package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CastilloDeCartas implements Serializable {
    private final ColorCarta color;
    private final List<Carta> cartas;
    @Serial
    private static final long serialVersionUID = 4L;

    public CastilloDeCartas(ColorCarta color) {
        this.color = color;
        this.cartas = new LinkedList<>();
    }

    public boolean apilarCarta(Carta carta) {

        if (carta.getColor() != this.color) {
            return false;
        }

        if (cartas.isEmpty()) {
            if (carta.getNumero() == 1) {
                cartas.add(carta);
                return true;
            }
        } else {

            Carta cartaSuperior = cartas.getLast();
            if (carta.getNumero() == cartaSuperior.getNumero() + 1) {
                cartas.add(carta);
                return true;
            }
        }

        return false;
    }

    public boolean esCastilloCompleto() {
        if (cartas.size() != 5) {
            return false;
        }
        for (int i = 1; i <= 5; i++) {
            if (cartas.get(i - 1).getNumero() != i) {
                return false;
            }
        }
        return true;
    }

    public int obtenerPuntosCastillo() {
        if (cartas.isEmpty()) {
            return 0;
        }
        return cartas.getLast().getNumero();
    }


    public List<Carta> getCartas() {
        return cartas;
    }

    public ColorCarta getColor() {
        return color;
    }


}


