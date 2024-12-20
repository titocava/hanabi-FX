package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class Ranking implements Serializable {
    @Serial
    private static final long serialVersionUID = 17L;
    private final Map<String, AgregarRegistroRanking> ranking;

    public Ranking() {
        this.ranking = new HashMap<>();
    }

    public void agregarRegistroConPersistencia(List<Jugador> jugadores, int puntos) throws RemoteException {
        Ranking rankingExistente = cargarDesdeArchivo();
        String identificadorEquipo = jugadores.stream()
                .map(Jugador::getNombre)
                .sorted()
                .collect(Collectors.joining("-"));
        if (rankingExistente.ranking.containsKey(identificadorEquipo)) {
            AgregarRegistroRanking entradaExistente = rankingExistente.ranking.get(identificadorEquipo);
            entradaExistente.setPuntosTotales(entradaExistente.getPuntosTotales() + puntos);
            entradaExistente.setPartidasJugadas(entradaExistente.getPartidasJugadas() + 1);
        } else {
            AgregarRegistroRanking nuevoRegistro = new AgregarRegistroRanking(puntos, 1);
            rankingExistente.ranking.put(identificadorEquipo, nuevoRegistro);
        }
        rankingExistente.guardarEnArchivo();
    }


    public Map<String, AgregarRegistroRanking> getRankingMap() {
        return ranking;
    }


    public String mostrarRanking() throws RemoteException, ClassNotFoundException {
        Ranking ranking = cargarDesdeArchivo();

        StringBuilder sb = new StringBuilder();
        sb.append("Ranking de Equipos (ordenado por puntos):\n");

        ranking.getRankingMap().entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().getPuntosTotales(), entry1.getValue().getPuntosTotales()))
                .forEach(entry -> {
                    String identificadorEquipo = entry.getKey();
                    AgregarRegistroRanking equipo = entry.getValue();

                    sb.append("Equipo: ").append(identificadorEquipo);
                    sb.append(" -> Puntos: ").append(equipo.getPuntosTotales());
                    sb.append(" -> Partidas Jugadas: ").append(equipo.getPartidasJugadas());
                    sb.append("\n\n");
                });

        return sb.toString();
    }


    private void guardarEnArchivo() {
        String archivo = System.getProperty("user.home") + "/HanabiRanking.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Ranking cargarDesdeArchivo() {
        String archivo = System.getProperty("user.home") + "/HanabiRanking.dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Ranking) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Ranking();
        }
    }  }