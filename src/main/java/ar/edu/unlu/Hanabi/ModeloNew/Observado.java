package ar.edu.unlu.Hanabi.ModeloNew;




public interface Observado {
    void agregarObservador(Observador observador);
    void notificarObservador(Eventos evento);
    void notificarObservador(Eventos evento, Object dato);
}
