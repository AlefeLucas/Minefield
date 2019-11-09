package alefelucas.minesweeper.model;

/**
 * Tipos de célula: mina ativa, mina neutralizada, célula vazia e neutralizador.
 * A célula vazia também inclui as células com números (elas estão vazias pois
 * não possuem objetos como bombas ou neutralizadores.
 *
 * @author Álefe Lucas
 */
public enum CellType {
    ACTIVE_MINE, DEAD_MINE, EMPTY, NEUTRALIZER
}
