package game2048.application;

import game2048.enums.Direction;
import game2048.enums.GameStatus;
import game2048.logic.GameLogic;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameManager {
    private final GameLogic logic = new GameLogic();
    private final BoardCanvas board = new BoardCanvas();
    private final ScoreboardCanvas scoreboard = new ScoreboardCanvas();
    private final ButtonCanvas button = new ButtonCanvas("New Game");
    private final TitleCanvas title = new TitleCanvas();
    private final StackPane root = new StackPane();
    private final Group container = new Group();

    public GameManager() {
        container.getChildren().add(board);
        container.getChildren().add(scoreboard);
        container.getChildren().add(button);
        container.getChildren().add(title);
        root.getChildren().add(container);

        title.setLayoutX(0);
        title.setLayoutY(0);

        button.setLayoutX(board.getWidth() - button.getWidth());
        button.setLayoutY(0);

        scoreboard.setLayoutX(board.getWidth() - scoreboard.getWidth());
        scoreboard.setLayoutY(button.getHeight() + 8);

        board.setLayoutY(scoreboard.getHeight() + button.getHeight() + 16);
        board.setLayoutX(0);

        Bounds bounds = container.getLayoutBounds();
        ChangeListener<Number> resize = (observable, oldValue, newValue) -> {
            double scale = Math.min((root.getWidth() - 32.0) / bounds.getWidth(),
                    (root.getHeight() - 32.0) / bounds.getHeight());

            container.setScaleX(scale);
            container.setScaleY(scale);
        };

        root.widthProperty().addListener(resize);
        root.heightProperty().addListener(resize);

        button.setOnMouseClicked(event -> {
            logic.reset();
            board.paint();
            scoreboard.paint();
        });

        board.paint();
        scoreboard.paint();
        button.paint();
        title.paint();
    }

    public Group getContainer() {
        return container;
    }

    public StackPane getRoot() {
        return root;
    }

    public void move(KeyCode keyCode) {
        if (keyCode.isArrowKey() && logic.getGameStatus() == GameStatus.RUNNING) {
                logic.move(Direction.valueOf(keyCode));
                board.paint();
                scoreboard.paint();

        }
    }

    private static class TitleCanvas extends Canvas {
        private static final int WIDTH = 400;
        private static final int HEIGHT = 132;

        TitleCanvas() {
            super(WIDTH, HEIGHT);
        }

        public void paint() {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, WIDTH, HEIGHT);

            // 2048
            gc.setFont(Font.font("Clear Sans", FontWeight.BOLD, 90));
            gc.setTextBaseline(VPos.TOP);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText("2048", 0, -12);

            // Goal: get to 2048!
            gc.setFont(Font.font("Clear Sans", FontWeight.NORMAL, 22));
            gc.setTextBaseline(VPos.TOP);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText("Goal: get to 2048!", 0, 100);
        }
    }


    private static class ButtonCanvas extends Canvas {
        private static final int WIDTH = 160;
        private static final int HEIGHT = 64;
        private final String label;
        private boolean hovered;

        ButtonCanvas(String label) {
            super(WIDTH, HEIGHT);
            hovered = false;
            this.label = label;

            setOnMouseEntered(event -> {
                getScene().setCursor(Cursor.HAND);
                hovered = true;
                paint();
            });

            setOnMouseExited(event -> {
                getScene().setCursor(Cursor.DEFAULT);
                hovered = false;
                paint();
            });

        }

        void paint() {
            GraphicsContext gc = getGraphicsContext2D();

            // Background
            gc.setFill(hovered ? Color.rgb(106, 106, 106) : Color.rgb(146, 146, 146));
            gc.fillRect(0, 0, getWidth(), getHeight());

            // Button
            gc.setFill(Color.rgb(0, 0, 0));
            gc.setFont(Font.font("Clear Sans", FontWeight.BOLD, 22));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(label, 0.5 * getWidth(), 0.5 * getHeight());
        }
    }

    private class ScoreboardCanvas extends Canvas {
        private static final int WIDTH = 160;
        private static final int HEIGHT = 64;

        ScoreboardCanvas() {
            super(WIDTH, HEIGHT);
        }

        void paint() {
            GraphicsContext gc = getGraphicsContext2D();

            // Background
            gc.setFill(Color.rgb(146, 146, 146));
            gc.fillRect(0, 0, getWidth(), getHeight());

            // Title
            {
                gc.setFill(Color.rgb(0, 0, 0));
                gc.setFont(Font.font("Clear Sans", FontWeight.BOLD, 16));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.TOP);
                gc.fillText("Score", 0.5 * getWidth(), 0);
            }

            // Score
            {
                gc.setFill(Color.rgb(0, 0, 0));
                gc.setFont(Font.font("Clear Sans", FontWeight.BOLD, 36));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.BOTTOM);
                gc.fillText(String.valueOf(logic.getScore()), 0.5 * getWidth(), getHeight());
            }

        }

    }

    private class BoardCanvas extends Canvas {
        private static final int TILE_SIZE = 128;
        private static final int TILE_GAP = 16;
        private static final int SIZE = TILE_GAP + 4 * (TILE_SIZE + TILE_GAP); // 592

        BoardCanvas() {
            super(SIZE, SIZE);
        }

        void paint() {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            // Background
            {
                gc.setFill(Color.rgb(146, 146, 146));
                gc.fillRect(0, 0, getWidth(), getHeight());
            }

            // Board
            {
                // Tiles
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        int x = TILE_GAP + (TILE_GAP + TILE_SIZE) * j;
                        int y = TILE_GAP + (TILE_GAP + TILE_SIZE) * i;
                        int value = logic.getValue(i, j);
                        Color tileColor = Color.rgb(255, 255, 255);
                        gc.setFill(tileColor);
                        gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                        if (value == 0)
                            continue;

                        Color textColor = Color.rgb(0, 0, 0);
                        Font font = Font.font("Clear Sans", FontWeight.BOLD, 60);
                        gc.setFill(textColor);
                        gc.setFont(font);
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.setTextBaseline(VPos.CENTER);
                        gc.fillText(String.valueOf(value), x + 0.5 * TILE_SIZE, y + 0.5 * TILE_SIZE);
                    }
                }

                // Overlay
                switch (logic.getGameStatus()) {
                    case GAME_OVER:
                        // Background
                        gc.setFill(Color.rgb(255, 255, 255, 0.75));
                        gc.fillRect(0, 0, getWidth(), getHeight());

                        // Text
                        gc.setFill(Color.rgb(0, 0, 0));
                        gc.setFont(Font.font("Clear Sans", FontWeight.BOLD, 70));
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.setTextBaseline(VPos.CENTER);
                        gc.fillText("Game Over!", 0.5 * getWidth(), 0.5 * getHeight());
                        break;
                    case WIN:
                        // Background
                        gc.setFill(Color.rgb(255, 255, 255, 0.75));
                        gc.fillRect(0, 0, getWidth(), getHeight());

                        // Text
                        gc.setFill(Color.rgb(0, 0, 0));
                        gc.setFont(Font.font("Clear Sans", FontWeight.BOLD, 70));
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.setTextBaseline(VPos.CENTER);
                        gc.fillText("You Win!", 0.5 * getWidth(), 0.5 * getHeight());
                        break;
                    case RUNNING:
                        break;
                }
            }
        }

    }

}
