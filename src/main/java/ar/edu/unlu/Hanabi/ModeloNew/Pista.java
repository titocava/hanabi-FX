package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serializable;

public class Pista implements Serializable {
    private final TipoPista tipo;           // El tipo de la pista (COLOR o NUMERO)
    private final Object valor;             // El valor de la pista (puede ser un color o un número)

    public Pista(TipoPista tipo, Object valor) {
        if (tipo == null || valor == null) {
            throw new IllegalArgumentException("Los parámetros tipo y valor no pueden ser nulos.");
        }

        if (tipo == TipoPista.COLOR && !(valor instanceof ColorCarta)) {
            throw new IllegalArgumentException("El valor debe ser un ColorCarta para el tipo de pista COLOR.");
        } else if (tipo == TipoPista.NUMERO && !(valor instanceof Integer)) {
            throw new IllegalArgumentException("El valor debe ser un Integer para el tipo de pista NUMERO.");
        }

        this.tipo = tipo;
        this.valor = valor;
    }

    public TipoPista getTipoPista() {
        return tipo;
    }

    public Object getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Pista{" +
                "tipo=" + tipo +
                ", valor=" + valor +
                '}';
    }
}