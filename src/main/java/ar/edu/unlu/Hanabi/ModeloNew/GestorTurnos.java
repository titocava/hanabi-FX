package ar.edu.unlu.Hanabi.ModeloNew;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GestorTurnos{
    private final Queue<Jugador> colaTurnos;

    public GestorTurnos() {
        this.colaTurnos = new LinkedList<>();
    }

    public void inicializarColaTurnos(List<Jugador> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) {
            throw new IllegalArgumentException("La lista de jugadores no puede estar vacía ni ser nula.");
        }
        System.out.println("Lista de jugadores recibida: " + jugadores);
        colaTurnos.clear();
        colaTurnos.addAll(jugadores);
    }


    public void cambiarTurno() {
        if (colaTurnos.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la cola de turnos.");
        }
        Jugador jugadorActual = colaTurnos.poll();
        colaTurnos.offer(jugadorActual);

    }

    public Jugador getJugadorActual() {
        if (colaTurnos.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la cola de turnos.");
        }
        return colaTurnos.peek();
    }

}
