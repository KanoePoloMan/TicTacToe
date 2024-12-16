package s21.datasource.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.Getter;
import s21.datasource.model.CurrentGameDAO;

@Component
@Getter
public class GameContainer {
    private final Map<UUID, CurrentGameDAO> games = new ConcurrentHashMap<>();
}
