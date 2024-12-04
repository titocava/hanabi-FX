package ar.edu.unlu.Hanabi;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.JuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.JuegoMostrable;
import ar.edu.unlu.Hanabi.ModeloNew.Jugador;
import ar.edu.unlu.Hanabi.Vista.IVista;
import ar.edu.unlu.Hanabi.Vista.VistaGrafica;
import ar.edu.unlu.Hanabi.Vista.VistaGraficaJugador;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class ClienteHanabi {
    private static int contadorClientes = 0;

    public static void main(String[] args) throws NumberFormatException {


        try {
            // Inicializar el modelo
            JuegoHanabi juegoHanabi = new JuegoHanabi();
            JuegoMostrable juegoMostrable = new JuegoMostrable(juegoHanabi);
            ControladorJuegoHanabi controlador = new ControladorJuegoHanabi(juegoHanabi, juegoMostrable);
            Jugador jugador = controlador.registrarJugador("tito");
            //IVista vista = new VistaGraficaJugador(controlador, jugador);


            //controlador.iniciarJuego(juegoHanabi.getJugadores());

            final int CLIENT_PORT = 9955 + contadorClientes;;
            contadorClientes++;
            Cliente cliente = new Cliente("127.0.0.1", CLIENT_PORT, "127.0.0.1",
                    9999);

            cliente.iniciar(controlador);


            System.out.println("Iniciando cliente [" + jugador.getNombre() + "] en puerto [" + CLIENT_PORT + "]");
            IVista vista = new VistaGraficaJugador(controlador, jugador);
            controlador.setVista(vista);
            System.out.println("Tu nombre: " + jugador.getNombre());
            vista.iniciar();

        } catch (Exception e) {
            e.printStackTrace();
        }






    }
}
