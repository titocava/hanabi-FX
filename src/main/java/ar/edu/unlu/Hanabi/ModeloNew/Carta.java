package ar.edu.unlu.Hanabi.ModeloNew;


import java.io.Serializable;
import java.util.Objects;

public class Carta implements Serializable {
    private final ColorCarta color;
    private final int numero;
    private boolean revelada;

    public Carta(ColorCarta color, int numero) {
        if (numero < 1 || numero > 5) {
            throw new IllegalArgumentException("Número de carta inválido: " + numero);
        }
        this.color = color;
        this.numero = numero;
        this.revelada = false;
    }

    public ColorCarta getColor() {
        return color;
    }

    public int getNumero() {
        return numero;
    }

    public boolean esRevelada() {
        return revelada;
    }

    public void revelar() {     this.revelada = true;    }

    public boolean coincideConPista(Pista pista) {
        if (pista == null || pista.getValor() == null) {
            throw new IllegalArgumentException("La pista no puede ser nula.");
        }

        if (Objects.requireNonNull(pista.getTipoPista()) == TipoPista.COLOR) {
            return this.color.equals(pista.getValor());  // Aquí 'color' es un campo de la clase que almacena el color de la carta
        } else if (pista.getTipoPista() == TipoPista.NUMERO) {
            return this.numero == (Integer) pista.getValor(); // Aquí 'numero' es un campo que almacena el número de la carta
        }
        return false;
    }

    @Override
    public String toString() {
        return color.name().toLowerCase() + " " + numero;
    }
}


