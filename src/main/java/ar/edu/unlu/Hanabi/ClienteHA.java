package ar.edu.unlu.Hanabi;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.Jugador;
import ar.edu.unlu.Hanabi.Vista.IVista;
import ar.edu.unlu.Hanabi.Vista.VistaConsolaGraficaJugador;
import ar.edu.unlu.Hanabi.Vista.VistaGraficaJugador;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Random;
import javax.swing.plaf.FontUIResource;

public class ClienteHA {

    public static void main(String[] args) throws NumberFormatException, IOException {
        ControladorJuegoHanabi controladorJuego = new ControladorJuegoHanabi();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUIFont(new FontUIResource("Arial", Font.PLAIN, 12));
        String nombreJugador = null;
        while (nombreJugador == null || nombreJugador.length() == 0) {
            nombreJugador = JOptionPane.showInputDialog("Ingrese su nombre:");
        }
        final int CLIENT_PORT = nuevoPuerto();
        Cliente cliente = new Cliente("127.0.0.1", CLIENT_PORT, "127.0.0.1", 9999);
        try {
            cliente.iniciar(controladorJuego);
        } catch (RemoteException | RMIMVCException e) {
            e.printStackTrace();
        }
        Jugador jugador = controladorJuego.registrarJugador(nombreJugador);
        if (jugador == null) {
            System.out.println("El jugador no pudo ser registrado.");
            System.exit(0);
        }
        IVista vista = new VistaGraficaJugador(controladorJuego, jugador);
        controladorJuego.setVista(vista);
        vista.iniciar();
        int continuar = JOptionPane.showConfirmDialog(
                null,
                "¿Desea registrar más jugadores?",
                "Registrar más jugadores",
                JOptionPane.YES_NO_OPTION
        );
        if (continuar == JOptionPane.NO_OPTION) {
            controladorJuego.iniciarJuego();
            JOptionPane.showMessageDialog(null, "Iniciando el juego en el servidor.");
        } else {
            JOptionPane.showMessageDialog(null, "Ejecute nuevamente para registrar otro jugador.");
        }
    }

    private static void setUIFont(FontUIResource fontUIResource) {
        Enumeration<?> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource originalFont = (FontUIResource) value;
                Font font = new Font(fontUIResource.getFontName(), originalFont.getStyle(), fontUIResource.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }

    private static int nuevoPuerto() {
        Random random = new Random();
        int port = 9900 + random.nextInt(9998 - 9900 + 1);
        return port;
    }
}
