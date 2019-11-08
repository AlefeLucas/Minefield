package alefelucas.minefield.model;

import static alefelucas.minefield.model.CellType.EMPTY;

public class Cell {

    private CellType cellType;
    private boolean revealed;

    public Cell() {
        this.cellType = EMPTY;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal(){
        this.revealed = true;
    }

}
