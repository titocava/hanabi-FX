package ar.edu.unlu.Hanabi.ModeloNew;

import java.io.Serializable;

public enum Eventos implements Serializable {
    // Categoría: Inicio y Fin del Juego
    INICIAR_JUEGO,
    VICTORIA,
    DERROTA,

    // Categoría: Turnos
    CAMBIO_TURNO,
    NO_ES_TURNO,
    INICIO_TURNO,


    // Categoría: Pistas
    PISTA_CREADA,
    PISTA_DADA,
    NO_HAY_PISTA_DIPONIBLE,

    // Categoría: Jugadores
    JUGADOR_CREADO,
    ERROR_CREACION_JUGADOR,
    OBTENER_NOMBRE_JUGADOR,

    // Categoría: Manos y Cartas
    MOSTRAR_MANO,
    MOSTRAR_MANOS_VISIBLES_RESTO,
    JUGADOR_JUGO_CARTA,
    JUGADOR_DESCARTO_CARTA,

    // Categoría: Tablero
    NO_HAY_PISTAS_USADAS,
    ACTUALIZAR_TABLERO,
    PUNTOS,
}


