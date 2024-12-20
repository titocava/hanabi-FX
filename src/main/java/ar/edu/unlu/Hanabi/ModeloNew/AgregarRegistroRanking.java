package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;

public class AgregarRegistroRanking {
    //@Serial
    //private static final long serialVersionUID = 19L;
    private int puntosTotales;
    private int partidasJugadas;

    public AgregarRegistroRanking(int puntosTotales, int partidasJugadas){
        this.puntosTotales = puntosTotales;
        this.partidasJugadas = partidasJugadas;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public void setPuntosTotales(int puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

}