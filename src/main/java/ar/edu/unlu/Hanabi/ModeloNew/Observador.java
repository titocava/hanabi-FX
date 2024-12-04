package ar.edu.unlu.Hanabi.ModeloNew;



public interface Observador {
    void notificar(Eventos evento);
    void notificar(Eventos evento, Object datoAenviar);
}



