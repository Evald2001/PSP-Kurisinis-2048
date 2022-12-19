package game2048.enums;

import javafx.scene.input.KeyCode;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Direction valueOf(KeyCode keyCode) {
        return valueOf(keyCode.name());
    }

}
