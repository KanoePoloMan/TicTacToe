package s21.datasource.model.exception;

import lombok.Getter;
import s21.datasource.model.CurrentGameDAO;

public class InvalidFieldException extends Exception {
    @Getter
    private final CurrentGameDAO currentGame;

    public InvalidFieldException(CurrentGameDAO currentGame, String message) {
        super(message);
        this.currentGame = currentGame;
    }
}
