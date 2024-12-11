package ar.edu.unlu.Hanabi.Vista;

import ar.edu.unlu.Hanabi.Controlador.ControladorJuegoHanabi;
import ar.edu.unlu.Hanabi.ModeloNew.*;

import java.util.List;


public interface IVista {






    void actualizarVista();




    void iniciar();


    void mostrarMensaje(String mensaje);

    void mostrarMenuDeAccion();

    void mostrarPuntuacion();

    void setControlador(ControladorJuegoHanabi controlador);

    void deshabilitarMenuAccion();
}