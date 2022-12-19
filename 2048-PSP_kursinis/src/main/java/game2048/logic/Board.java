package game2048.logic;

import game2048.enums.Direction;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int[][] board;
    private final int score;

    public Board(int size) {
        this.board = new int[size][size];
        this.score = 0;

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                board[x][y] = 0;
            }
        }
    }

    public int getValue(Cell cell) {
        return board[cell.x()][cell.y()];
    }

    public void setValue(Cell cell, int value) {
        board[cell.x()][cell.y()] = value;
    }

    public int getValue(int x, int y) {
        return board[x][y];
    }

    public boolean isEmpty(Cell cell) {
        return getValue(cell) == 0;
    }

    public List<Cell> emptyCells() {
        List<Cell> result = new ArrayList<>();
        for (int x = 0; x < board.length; ++x) {
            for (int y = 0; y < board[x].length; ++y) {
                Cell cell = new Cell(x, y);
                if (isEmpty(cell)) {
                    result.add(cell);
                }
            }
        }
        return result;
    }

    public void reset() {
        for (int x = 0; x < board.length; ++x) {
            for (int y = 0; y < board[x].length; ++y) {
                board[x][y] = 0;
            }
        }
    }

    public int[] getVector(Direction direction, int index) {
        switch (direction) {
            case UP:
            case DOWN:
                return getColumn(index);
            case LEFT:
            case RIGHT:
                return getRow(index);
            default:
                return null;
        }
    }

    public void setVector(Direction direction, int index, int[] vector) {
        switch (direction) {
            case UP:
            case DOWN:
                setColumn(index, vector);
                break;
            case LEFT:
            case RIGHT:
                setRow(index, vector);
                break;
            default:
                break;
        }
    }

    private void setRow(int rowIndex, int[] row) {
        for (int y = 0; y < board.length; ++y) {
            board[rowIndex][y] = row[y];
        }
    }

    private void setColumn(int columnIndex, int[] column) {
        for (int x = 0; x < board.length; ++x) {
            board[x][columnIndex] = column[x];
        }
    }

    private int[] getRow(int rowIndex) {
        int[] row = new int[board.length];

        for (int i = 0; i < board.length; i++) {
            row[i] = board[rowIndex][i];
        }

        return row;
    }

    private int[] getColumn(int columnIndex) {
        int[] column = new int[board.length];

        for (int i = 0; i < 4; i++) {
            column[i] = board[i][columnIndex];
        }

        return column;
    }

}