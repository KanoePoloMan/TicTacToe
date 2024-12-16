package s21.domain.model;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurrentGame {
    private final UUID uuid;
    private final GameField gameField;

    public CurrentGame(GameField gameField) {
        this.uuid = UUID.randomUUID();
        this.gameField = gameField;
    }
}
