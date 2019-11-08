package alefelucas.minefield.model;

import java.util.Random;

import static alefelucas.minefield.model.GameStatus.LOST;
import static alefelucas.minefield.model.GameStatus.PLAYING;
import static alefelucas.minefield.model.GameStatus.WON;

public class Minefield {

    private final int width;
    private final int height;

    private GameStatus status;

    private Cell[][] cells;
    private boolean initialized;

    private static final int MINE_QUANTITY = 15;
    private int hiddenCellsQuantity;

    public Minefield(int width, int height) {
        this.width = width;
        this.height = height;

        this.cells = new Cell[width][height];
        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < this.cells[i].length; j++) {
                this.cells[i][j] = new Cell();
            }
        }
        this.hiddenCellsQuantity = width * height;
        this.status = PLAYING;
    }

    public GameStatus reveal(int x, int y) {
        if (!this.initialized) {
            init(x, y);
        }

        this.cells[x][y].reveal();
        this.hiddenCellsQuantity--;
        if (this.cells[x][y].isMine()) {
            return LOST;
        } else if (hiddenCellsQuantity == 0) {
            return WON;
        } else {
            return PLAYING;
        }
    }

    public char getCellLabel(int x, int y) {
        if (this.cells[x][y].isMine()) return '*';
        else {
            char neighboringBombs = '0';
            int neighborsDelta[] = {-1, -1, 0, -1, 1, -1, -1, 0, 1, 0, -1, 1, 0, 1, 1, 1};
            for (int i = 0; i < neighborsDelta.length; i += 2) {
                int neighborX = x + neighborsDelta[i];
                int neighborY = y + neighborsDelta[i + 1];
                if (neighborX >= 0 && neighborX < this.cells.length &&
                        neighborY >= 0 && neighborY <= this.cells[neighborX].length &&
                        this.cells[neighborX][neighborY].isMine()) {
                    neighboringBombs++;
                }
            }
            return neighboringBombs;
        }

    }

    private void init(int initialX, int initialY) {
        Random random = new Random();
        for (int i = 0; i < MINE_QUANTITY; i++) {

            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            while ((x == initialX && y == initialY) || this.cells[x][y].isMine()) {
                x = random.nextInt(this.width);
                y = random.nextInt(this.height);
            }

            this.cells[x][y].placeMine();

        }
    }
}
