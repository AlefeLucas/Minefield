package alefelucas.minefield.model;

public class Cell {

    private boolean mine;
    private boolean revealed;

    public boolean isMine() {
        return mine;
    }

    public void placeMine() {
        this.mine = true;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal(){
        this.revealed = true;
    }


}
