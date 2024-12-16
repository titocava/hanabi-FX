package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tablero implements Serializable {
    private static final int CARTAS_MAXIMAS_EN_MANO = 5;
    private final Mazo mazo;
    private final Fichas fichas;
    private final List<CastilloDeCartas> castillos;
    @Serial
    private static final long serialVersionUID = 2L;

    public Tablero() {
        this.mazo = new Mazo();
        this.fichas = new Fichas();
        this.castillos = new ArrayList<>();
        for (ColorCarta color : ColorCarta.values()) {
            castillos.add(new CastilloDeCartas(color));
        }
    }

    public void repartirCartas(List<Jugador> jugadores) {
        int cartasPorJugador = (jugadores.size() <= 3) ? 5 : 4; // 5 cartas para 2-3 jugadores, 4 para 4-5 jugadores
        for (Jugador jugador : jugadores) {
            for (int i = 0; i < cartasPorJugador; i++) {
                Carta cartaRobada = mazo.robarCarta();
                if (cartaRobada != null) {
                    jugador.agregarCartaACartasEnMano(cartaRobada);
                }
            }
        }
    }

    public int getMazoActual() {
        return mazo.cartasRestantes();
    }



    public void darPista(Jugador jugadorDestino, Pista pista) {
        if (obtenerFichasDePista() <= 0) {
            throw new IllegalStateException("No hay fichas de pista disponibles.");
        }
        if (jugadorDestino == null || pista == null) {
            throw new IllegalArgumentException("El jugador destino y la pista no pueden ser nulos.");
        }
        for (Carta carta : jugadorDestino.getMano()) {
            if (carta.coincideConPista(pista)) {
                carta.revelar();
            }
        }
    }

    public void jugarCarta(Jugador jugador, Carta carta) {
        if (jugador == null || carta == null) {
            throw new IllegalArgumentException("El jugador y la carta no pueden ser nulos.");
        }
        if (!jugador.getMano().contains(carta)) {
            throw new IllegalArgumentException("La carta no pertenece a la mano del jugador.");
        }
        jugador.eliminarCartaDeLaMano(carta);
    }

    public void descartarCarta(Jugador jugador, Carta carta) {
        if (obtenerFichasDePistaUsadas() <= 0) {
            throw new IllegalStateException("No hay fichas de pista disponibles.");
        }
        if (!jugador.getMano().contains(carta)) {
            throw new IllegalArgumentException("La carta no está en la mano del jugador.");
        }
        jugador.eliminarCartaDeLaMano(carta);

    }

    public void tomarCarta(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        if (jugador.getMano().size() >= CARTAS_MAXIMAS_EN_MANO) {
            throw new IllegalStateException("El jugador ya tiene el número máximo de cartas en la mano.");
        }
        if (mazo.estaVacio()) {
            throw new IllegalStateException("El mazo está vacío. No se pueden tomar más cartas.");
        }
        Carta cartaRobada = mazo.robarCarta();
        jugador.agregarCartaACartasEnMano(cartaRobada);
    }

    public void cartaJugada(Carta carta) {
        CastilloDeCartas castillo = null;
        for (CastilloDeCartas c : castillos) {
            if (c.getColor() == carta.getColor()) {
                castillo = c;
                break;
            }
        }
        if (castillo != null) {
            boolean apilada = castillo.apilarCarta(carta);
            if (apilada) {
                System.out.println("Carta apilada correctamente en el castillo de " + carta.getColor());
                return;
            }
        }
        desecharCarta(carta);
        reducirFichaVida();

    }


    public int calcularPuntos() {
        List<CastilloDeCartas> castillos = this.getCastillos();
        int puntosTotales = 0;
        for (CastilloDeCartas castillo : castillos) {
            puntosTotales += castillo.obtenerPuntosCastillo();
        }
        return puntosTotales;
    }


    private void desecharCarta(Carta carta) {
        System.out.println("Carta " + carta + " desechada.");
    }

    public int obtenerFichasDePista() {
        return fichas.getFichasDePista();
    }

    public int obtenerFichasDePistaUsadas() {
        return fichas.getFichasPistaUsadas();
    }


    public int obtenerFichasDeVida() {
        return fichas.getFichasDeVida();
    }


    public void reducirFichaPista() {
        fichas.usarFichaPista();
    }


    public void reducirFichaVida() {
        fichas.usarFichaVida();
    }


    public void recuperarFichaPista() {
        fichas.recuperarFichaPista();
    }

    public boolean todosLosCastillosCompletos() {
        for (CastilloDeCartas castillo : castillos) {
            if (!castillo.esCastilloCompleto()) {
                return false;
            }
        }
        return true;
    }


    public List<CastilloDeCartas> getCastillos() {
        return castillos;
    }





}
