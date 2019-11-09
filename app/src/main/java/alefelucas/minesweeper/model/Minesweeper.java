package alefelucas.minesweeper.model;

import java.util.ArrayList;
import java.util.Random;

import static alefelucas.minesweeper.model.CellType.*;
import static alefelucas.minesweeper.model.GameStatus.LOST;
import static alefelucas.minesweeper.model.GameStatus.PLAYING;
import static alefelucas.minesweeper.model.GameStatus.WON;

/**
 * Estrutura principal do jogo campo minado. Toda lógica básica do
 * jogo se encontra encapsulada nesta classe.
 *
 * @author Álefe Lucas
 */
public class Minesweeper {

    private final int width;
    private final int height;

    private GameStatus status;

    private Cell[][] cells;
    private boolean initialized;

    private int mineQuantity;
    private static final int NEUTRALIZERS = 2;

    private int revealedCellsQuantity;


    /**
     * Constrói um objeto {@link Minesweeper} dado a altura e largura do jogo (quantidade de quadrados), e a quantidade de minas.
     *
     * @param height altura
     * @param width  largura
     * @param mineQuantity quantidade de minas
     */
    public Minesweeper(int height, int width, int mineQuantity) {
        this.width = width;
        this.height = height;
        this.mineQuantity = mineQuantity;

        this.cells = new Cell[height][width];

        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < this.cells[i].length; j++) {
                this.cells[i][j] = new Cell();
            }
        }
        this.revealedCellsQuantity = 0;
        this.status = PLAYING;
    }


    /**
     * Revela um quadrado do jogo, na posição dada. Se for um quadrado que não possui minas na vizinhança,
     * é revelado os quadrados adjascentes.
     *
     * @return Lista de inteiros contendo as posições reveladas.
     */
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

        if (this.cells[y][x].getCellType() == ACTIVE_MINE) {
            this.status = LOST;
        } else if (this.revealedCellsQuantity >= this.width * this.height - (mineQuantity)) {
            this.status = WON;
        } else {
            this.status = PLAYING;
            if (this.getCellLabel(x, y) == ' ') {
                revealed.addAll(spread(x, y));
            }
        }
        return revealed;
    }

    /**
     * Chama o método reveal para cada um dos vizinhos da posição dada.
     *
     * @return Lista de inteiros contendo as posições reveladas.
     */
    private ArrayList<Integer> spread(int x, int y) {
        int[] neighborsDelta = {0, -1, -1, 0, 1, 0, 0, 1};
        ArrayList<Integer> revealed = new ArrayList<>();
        for (int i = 0; i < neighborsDelta.length; i += 2) {
            int neighborX = x + neighborsDelta[i];
            int neighborY = y + neighborsDelta[i + 1];
            if (neighborX >= 0 && neighborX < this.cells[0].length && neighborY >= 0 &&
                    neighborY < this.cells.length &&
                    !this.isRevealed(neighborX, neighborY)) {
                revealed.addAll(reveal(neighborX, neighborY));
            }
        }
        return revealed;
    }

    /**
     * Retorna se a posição dada está revelada.
     */
    public boolean isRevealed(int x, int y) {
        return this.cells[y][x].isRevealed();
    }

    /**
     * Obtém o rótulo exibido na posição dada, seja vazio (espaço), número, asterísco ou a letra 'N'.
     */
    public char getCellLabel(int x, int y) {
        if (this.cells[y][x].getCellType() == ACTIVE_MINE || this.cells[y][x].getCellType() == DEAD_MINE)
            return '*';
        else if (this.cells[y][x].getCellType() == NEUTRALIZER) return 'N';
        else {
            char neighboringMines = countNeighboringMines(x, y);
            return neighboringMines == '0' ? ' ' : neighboringMines;
        }

    }

    /**
     * Retorna a quantidade de minas na vizinhança do ponto dado.
     */
    private char countNeighboringMines(int x, int y) {
        char neighboringMines = '0';
        int[] neighborsDelta = {-1, -1, 0, -1, 1, -1, -1, 0, 1, 0, -1, 1, 0, 1, 1, 1};
        for (int i = 0; i < neighborsDelta.length; i += 2) {
            int neighborX = x + neighborsDelta[i];
            int neighborY = y + neighborsDelta[i + 1];
            if (neighborX >= 0 && neighborX < this.cells[0].length) {
                if (neighborY >= 0 && neighborY < this.cells.length) {
                    if (this.cells[neighborY][neighborX].getCellType() == ACTIVE_MINE || this.cells[neighborY][neighborX].getCellType() == DEAD_MINE) {
                        neighboringMines++;
                    }
                }
            }
        }
        return neighboringMines;
    }

    /**
     * Distribui as minas e neutralizadores pelo espaço do jogo, assegurando que a posição dada será vazia.
     */
    private void init(int initialX, int initialY) {
        putMines(initialX, initialY);
        putNeutralizers(initialX, initialY);
        this.initialized = true;
    }

    /**
     * Distribui os neutralizadores pelo espaço do jogo.
     */
    private void putNeutralizers(int initialX, int initialY) {
        Random random = new Random();
        for (int i = 0; i < NEUTRALIZERS; i++) {
            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            while ((x == initialX && y == initialY) || this.cells[y][x].getCellType() == ACTIVE_MINE || this.cells[y][x].getCellType() == DEAD_MINE) {
                x = random.nextInt(this.width);
                y = random.nextInt(this.height);
            }

            this.cells[y][x].setCellType(NEUTRALIZER);

            neutralize(x, y);

        }
    }

    /**
     * Neutraliza as minas ao redor da posição dada.
     */
    private void neutralize(int x, int y) {
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

    /**
     * Distribui as minas pelo espaço do jogo.
     */
    private void putMines(int initialX, int initialY) {
        Random random = new Random();
        for (int i = 0; i < mineQuantity; i++) {
            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            while ((x == initialX && y == initialY) ||
                    this.cells[y][x].getCellType() == ACTIVE_MINE || this.cells[y][x].getCellType() == DEAD_MINE ||
                    ((Math.abs(x - initialX) <= 1 && Math.abs(y - initialY) <= 1))) {
                x = random.nextInt(this.width);
                y = random.nextInt(this.height);
            }

            this.cells[y][x].setCellType(ACTIVE_MINE);

        }
    }

    /**
     * Retorna o status do jogo.
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Retorna a largura do jogo.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retorna a altura do jogo.
     */
    public int getHeight() {
        return height;
    }
}
