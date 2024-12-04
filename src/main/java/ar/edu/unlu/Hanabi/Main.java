package ar.edu.unlu.Hanabi;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;
import ar.edu.unlu.Hanabi.Vista.IVista;
import ar.edu.unlu.Hanabi.Vista.VistaGraficaJugador;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.util.List;
import java.util.ArrayList;


public class Main {
    private static int contadorClientes = 0;

    public static void main(String[] args) {
        try {
            JuegoHanabi juegoHanabi = new JuegoHanabi();
            JuegoMostrable juegoMostrable = new JuegoMostrable(juegoHanabi);
            List<Jugador> listaJugadores = new ArrayList<>();
            String[] nombresJugadores = {"Tito", "Tito2", "Tito3"};

            for (String nombre : nombresJugadores) {
                Jugador nuevoJugador = juegoHanabi.registrarJugador(nombre);
                listaJugadores.add((Jugador) nuevoJugador);

            }
            //List<Jugador> listaJugadores = juegoHanabi.getJugadores();
            for (Jugador jugador : listaJugadores) {

                ControladorJuegoHanabi controlador = new ControladorJuegoHanabi(juegoHanabi, juegoMostrable);
                IVista vista = new VistaGraficaJugador(controlador, jugador);
                controlador.setVista(vista);

                final int CLIENT_PORT = 9955 + contadorClientes;
                System.out.println("Puerto cliente: " + CLIENT_PORT + "\n");
                contadorClientes++;

                Cliente cliente = new Cliente("127.0.0.1", CLIENT_PORT, "127.0.0.1", 9999);
                cliente.iniciar(controlador);

                vista.iniciar();
            }

            // Iniciar el juego con la lista de jugadores
            juegoHanabi.iniciarJuego();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

