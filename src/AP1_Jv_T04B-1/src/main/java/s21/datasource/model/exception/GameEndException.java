package s21.datasource.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import s21.datasource.model.CurrentGameDAO;

@RequiredArgsConstructor
public class GameEndException extends Exception {
    @Getter
    private final CurrentGameDAO currentGame;

    public GameEndException(CurrentGameDAO currentGame, String message) {
        super(message);
        this.currentGame = currentGame;
    }
}
