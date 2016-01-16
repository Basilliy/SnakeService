package Project.SentObjects;

import java.io.Serializable;

public class Board implements Serializable {
    private final int[][] board;

    public Board(int[][] board) {
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }
}
