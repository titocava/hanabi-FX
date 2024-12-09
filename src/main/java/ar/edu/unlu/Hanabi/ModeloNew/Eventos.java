package ar.edu.unlu.Hanabi.ModeloNew;

public enum Eventos {
    // Categoría: Inicio y Fin del Juego
    INICIAR_JUEGO,
    VICTORIA,
    DERROTA,

    // Categoría: Turnos
    CAMBIO_TURNO,
    NO_ES_TURNO,


    // Categoría: Pistas
    PISTA_DADA,

    // Categoría: Jugadores
    JUGADOR_CREADO,
    ERROR_CREACION_JUGADOR,

    // Categoría: Manos y Cartas
    JUGADOR_JUGO_CARTA,
    JUGADOR_DESCARTO_CARTA,

    // Categoría: Tablero
    NO_HAY_PISTAS_USADAS,
    PUNTOS,
}


