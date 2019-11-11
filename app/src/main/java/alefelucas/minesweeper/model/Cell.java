package alefelucas.minesweeper.model;

import static alefelucas.minesweeper.model.CellType.EMPTY;

/**
 * Estrutura para os quadrados do campo minado.
 * Cada célula tem um tipo de célula ({@link CellType}) e um booleano
 * revealed que diz se a célula foi revelada ou não.
 *
 * @author Álefe Lucas
 */
public class Cell {

    private CellType cellType;
    private boolean revealed;
    private boolean marked;

    Cell() {
        this.cellType = EMPTY;
    }

    CellType getCellType() {
        return cellType;
    }

    void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    boolean isRevealed() {
        return revealed;
    }

    void reveal() {
        this.revealed = true;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
