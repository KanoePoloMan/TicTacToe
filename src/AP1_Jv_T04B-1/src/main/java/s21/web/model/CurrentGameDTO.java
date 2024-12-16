package s21.web.model;

import java.util.UUID;

public record CurrentGameDTO (
    UUID uuid,
    GameFieldDTO gameField,
    String error
) {}
