package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo implements Serializable {
    private final List<Carta> cartas;
    @Serial
    private static final long serialVersionUID = 7L;


    public Mazo() {
        this.cartas = new ArrayList<>();
        generarCartas();
        barajar();
    }


    private void generarCartas() {
        for (ColorCarta color : ColorCarta.values()) {
            // Añadir 3 cartas con número 1
            for (int i = 0; i < 3; i++) {
                cartas.add(new Carta(color, 1));
            }
            // Añadir 2 cartas con números 2, 3 y 4
            for (int i = 2; i <= 4; i++) {
                cartas.add(new Carta(color, i));
                cartas.add(new Carta(color, i));
            }
            // Añadir 1 carta con número 5
            cartas.add(new Carta(color, 5));
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta robarCarta() {
        if (cartas.isEmpty()) {
            throw new IllegalStateException("No hay cartas en el mazo.");
        }
        return cartas.removeLast();
    }


    public int cartasRestantes() {
        return cartas.size();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

}
