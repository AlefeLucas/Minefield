package alefelucas.minesweeper.model;

import java.util.ArrayList;
import java.util.Random;

import static alefelucas.minesweeper.model.CellType.*;
import static alefelucas.minesweeper.model.GameStatus.LOST;
import static alefelucas.minesweeper.model.GameStatus.PLAYING;
import static alefelucas.minesweeper.model.GameStatus.WON;

public class Minesweeper {

    private final int width;
    private final int height;

    private GameStatus status;

    private Cell[][] cells;
    private Cell[] mines;
    private boolean initialized;

    private static final int MINE_QUANTITY = 15;
    private static final int NEUTRALIZERS = 2;

    private int revealedCellsQuantity;


    public Minesweeper(int height, int width) {
        this.width = width;
        this.height = height;

        this.cells = new Cell[height][width];
        this.mines = new Cell[MINE_QUANTITY];

        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < this.cells[i].length; j++) {
                this.cells[i][j] = new Cell();
            }
        }
        this.revealedCellsQuantity = 0;
        this.status = PLAYING;
    }


    public ArrayList<Integer> reveal(int x, int y) {
        if (!this.initialized) {
            init(x, y);
        }

        ArrayList<Integer> revealed = new ArrayList<>();
        revealed.add(this.getWidth() * y + x);
        this.cells[y][x].reveal();
        if (this.cells[y][x].getCellType() != DEAD_MINE && this.cells[y][x].getCellType() != ACTIVE_MINE) {
            this.revealedCellsQuantity++;
        }
        System.out.println("Revealed: " + revealedCellsQuantity + " of " + (this.width * this.height - (MINE_QUANTITY)));

        if (this.cells[y][x].getCellType() == ACTIVE_MINE) {
            this.status = LOST;
        } else if (this.revealedCellsQuantity >= this.width * this.height - (MINE_QUANTITY)) {
            System.out.println("WON - revealed: " + revealedCellsQuantity + " of " + (this.width * this.height - (MINE_QUANTITY)));
            this.status = WON;
        } else {
            this.status = PLAYING;
            if (this.getCellLabel(x, y) == ' ') {
                revealed.addAll(spread(x, y));
            }
        }
        return revealed;
    }

    private ArrayList<Integer> spread(int x, int y) {
        int[] neighborsDelta = {0, -1, -1, 0, 1, 0, 0, 1};
        ArrayList<Integer> revealed = new ArrayList<>();
        for (int i = 0; i < neighborsDelta.length; i += 2) {
            int neighborX = x + neighborsDelta[i];
            int neighborY = y + neighborsDelta[i + 1];
            if (neighborX >= 0 && neighborX < this.cells[0].length) {
                if (neighborY >= 0 && neighborY < this.cells.length) {
                    if (!this.isRevealed(neighborX, neighborY)) {
                        revealed.addAll(reveal(neighborX, neighborY));
                    }
                }
            }
        }
        return revealed;
    }

    public boolean isRevealed(int x, int y) {
        return this.cells[y][x].isRevealed();
    }

    public char getCellLabel(int x, int y) {
        if (this.cells[y][x].getCellType() == ACTIVE_MINE || this.cells[y][x].getCellType() == DEAD_MINE)
            return '*';
        else if (this.cells[y][x].getCellType() == NEUTRALIZER) return 'N';
        else {
            char neighboringBombs = '0';
            int[] neighborsDelta = {-1, -1, 0, -1, 1, -1, -1, 0, 1, 0, -1, 1, 0, 1, 1, 1};
            for (int i = 0; i < neighborsDelta.length; i += 2) {
                int neighborX = x + neighborsDelta[i];
                int neighborY = y + neighborsDelta[i + 1];
                if (neighborX >= 0 && neighborX < this.cells[0].length) {
                    if (neighborY >= 0 && neighborY < this.cells.length) {
                        if (this.cells[neighborY][neighborX].getCellType() == ACTIVE_MINE || this.cells[neighborY][neighborX].getCellType() == DEAD_MINE) {
                            neighboringBombs++;
                        }
                    }
                }
            }
            return neighboringBombs == '0' ? ' ' : neighboringBombs;
        }

    }

    private void init(int initialX, int initialY) {
        Random random = new Random();
        for (int i = 0; i < MINE_QUANTITY; i++) {
            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            while ((x == initialX && y == initialY) || this.cells[y][x].getCellType() == ACTIVE_MINE || this.cells[y][x].getCellType() == DEAD_MINE) {
                x = random.nextInt(this.width);
                y = random.nextInt(this.height);
            }

            this.cells[y][x].setCellType(ACTIVE_MINE);
            this.mines[i] = this.cells[y][x];

        }

        for (int i = 0; i < NEUTRALIZERS; i++) {
            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            while ((x == initialX && y == initialY) || this.cells[y][x].getCellType() == ACTIVE_MINE || this.cells[y][x].getCellType() == DEAD_MINE) {
                x = random.nextInt(this.width);
                y = random.nextInt(this.height);
            }

            this.cells[y][x].setCellType(NEUTRALIZER);

            int[] neighborsDelta = {-1, -1, 0, -1, 1, -1, -1, 0, 1, 0, -1, 1, 0, 1, 1, 1};
            for (int j = 0; j < neighborsDelta.length; j += 2) {
                int neighborX = x + neighborsDelta[j];
                int neighborY = y + neighborsDelta[j + 1];
                if (neighborX >= 0 && neighborX < this.cells[0].length) {
                    if (neighborY >= 0 && neighborY < this.cells.length) {
                        if (this.cells[neighborY][neighborX].getCellType() == ACTIVE_MINE) {
                            this.cells[neighborY][neighborX].setCellType(DEAD_MINE);
                        }
                    }
                }
            }

        }

        this.initialized = true;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
