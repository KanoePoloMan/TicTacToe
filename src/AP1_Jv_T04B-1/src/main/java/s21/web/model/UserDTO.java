package s21.web.model;

import java.util.UUID;

public record UserDTO(
    UUID uuid,
    String login,
    String password) {}
