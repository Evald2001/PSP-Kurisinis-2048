package game2048.logic;

import game2048.enums.Direction;
import game2048.enums.GameStatus;

import java.security.SecureRandom;
import java.util.List;

public class GameLogic {
    private final SecureRandom random = new SecureRandom();
    private final Board board = new Board(4);
    private int score = 0;

    public GameLogic() {
        reset();
    }

    public int getValue(int x, int y) {
        return board.getValue(x, y);
    }

    public void reset() {
        board.reset();
        placeTile();
        placeTile();
        score = 0;
    }

    public void move(Direction direction) {
        boolean placeTile = false;

        for (int i = 0; i < 4; i++) {
            int[] initial = board.getVector(direction, i);
            int[] vector = board.getVector(direction, i);
            int[] newVector = new int[]{0, 0, 0, 0};
            int index = 0;

            switch (direction) {
                case UP:
                case LEFT: {
                    index = 0;
                    for (int j = 0; j < 4; j++) {
                        if (vector[j] != 0) {
                            newVector[index] = vector[j];
                            index++;
                        }
                    }
                    break;
                }

                case DOWN:
                case RIGHT: {
                    index = 3;
                    for (int j = 3; j >= 0; j--) {
                        if (vector[j] != 0) {
                            newVector[index] = vector[j];
                            index--;
                        }
                    }
                    break;
                }
            }

            board.setVector(direction, i, newVector);
            vector = newVector;
            newVector = new int[]{0, 0, 0, 0};

            switch (direction) {
                case UP:
                case LEFT: {
                    index = 0;
                    for (int j = 0; j < 4; j++) {
                        if (vector[j] == 0)
                            break;

                        newVector[index] = vector[j];

                        if (j + 1 >= 4)
                            break;

                        if (vector[j] == vector[j + 1]) {
                            newVector[index] = 2 * vector[j];
                            score += newVector[index];
                            j++;
                        }

                        index++;
                    }
                    break;
                }

                case DOWN:
                case RIGHT: {
                    index = 3;
                    for (int j = 3; j >= 0; j--) {
                        if (vector[j] == 0)
                            break;

                        newVector[index] = vector[j];

                        if (j - 1 < 0)
                            break;

                        if (vector[j] == vector[j - 1]) {
                            newVector[index] = 2 * vector[j];
                            score += newVector[index];
                            j--;
                        }

                        index--;
                    }
                    break;
                }
            }

            board.setVector(direction, i, newVector);

            for (int j = 0; j < 4; j++) {
                if (newVector[j] != initial[j]) {
                    placeTile = true;
                    break;
                }
            }
        }

        if (placeTile)
            placeTile();
    }

    public GameStatus getGameStatus() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board.getValue(i, j) == 2048)
                    return GameStatus.WIN;
            }
        }

        if (board.emptyCells().size() > 0) {
            return GameStatus.RUNNING;
        } else {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.getValue(i, j) == board.getValue(i, j + 1))
                        return GameStatus.RUNNING;
                }
            }

            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 3; i++) {
                    if (board.getValue(i, j) == board.getValue(i + 1, j))
                        return GameStatus.RUNNING;
                }
            }
        }

        return GameStatus.GAME_OVER;
    }

    private boolean placeTile() {
        List<Cell> emptyCells = board.emptyCells();
        if (emptyCells.size() == 0)
            return false;

        Cell emptyCell = emptyCells.get(getRandom(emptyCells.size()));
        board.setValue(emptyCell, getRandom(2) == 0 ? 4 : 2);
        return true;
    }

    public int getScore() {
        return score;
    }

    private int getRandom(int bound) {
        return random.nextInt(bound);
    }

}
