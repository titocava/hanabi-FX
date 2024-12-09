package ar.edu.unlu.Hanabi;

import ar.edu.unlu.Hanabi.ModeloNew.IJuegoHanabiRemoto;
import ar.edu.unlu.Hanabi.ModeloNew.JuegoHanabi;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.servidor.Servidor;


import java.rmi.RemoteException;



public class ServidorHanabi {
    public static void main(String[] args) {
        System.out.println("Iniciando servidor RMI...");
        IJuegoHanabiRemoto juegoHanabiRemoto = new JuegoHanabi();
        Servidor servidor = new Servidor("127.0.0.1", 9999);
        try {
            servidor.iniciar(juegoHanabiRemoto);
            System.out.println("Servidor iniciado y esperando conexiones de clientes...");
        } catch (RemoteException | RMIMVCException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


