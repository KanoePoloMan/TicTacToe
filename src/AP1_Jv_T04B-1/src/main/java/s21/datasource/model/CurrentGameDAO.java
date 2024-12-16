package s21.datasource.model;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CurrentGameDAO {
    private final UUID uuid;
    private final GameFieldDAO gameField;

    public CurrentGameDAO(GameFieldDAO gameField) {
        this.uuid = UUID.randomUUID();
        this.gameField = gameField;
    }
    public CurrentGameDAO(UUID uuid, GameFieldDAO gameField) {
        this.uuid = uuid;
        this.gameField = gameField;
    }
}
