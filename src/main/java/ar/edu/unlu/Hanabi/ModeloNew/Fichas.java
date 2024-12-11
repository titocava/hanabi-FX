package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serial;

public class Fichas implements java.io.Serializable {
    private int fichasPistaDisponibles;
    private int fichasVida;
    private int fichasPistaUsadas;
    @Serial
    private static final long serialVersionUID = 6L;


    public Fichas() {
        this.fichasVida = 8;
        this.fichasPistaDisponibles = 3;
        this.fichasPistaUsadas = 0;
    }

    public int getFichasDePista() {
        return fichasPistaDisponibles;
    }

    public int getFichasDeVida() {
        return fichasVida;
    }

    public int getFichasPistaUsadas() {
        return fichasPistaUsadas;
    }

    public void usarFichaPista() {
        if (fichasPistaDisponibles <= 0) {
            throw new IllegalStateException("No hay fichas de pista disponibles.");
        }
        fichasPistaDisponibles--;
        fichasPistaUsadas++;
    }

    public void usarFichaVida() {
        if (fichasVida <= 0) {
            throw new IllegalStateException("No hay fichas de vida disponibles.");
        }
        fichasVida--;
    }

    public void recuperarFichaPista() {
        fichasPistaUsadas--;
        fichasPistaDisponibles++;
    }



    @Override
    public String toString() {
        return "Fichas de Vida: " + fichasVida + ", Fichas de Pista: " + fichasPistaDisponibles + ", Fichas de Pistas Usadas: " + fichasPistaUsadas;
    }
}
