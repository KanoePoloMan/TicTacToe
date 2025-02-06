package s21.datasource.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import s21.datasource.model.CurrentGameAIDAO;
import s21.datasource.model.CurrentGameDAO;
import s21.datasource.model.UserDAO;
import s21.domain.model.GameState;

@Component
public class Repository {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepositoryAI gameRepositoryAI;
    @Autowired
    private SearchQueueRepository searchRepository;
    @Autowired
    private FoundedGamesRepository foundedGamesRepository;

    public CurrentGameDAO getBlankGames() {
        return gameRepository.findByState(GameState.WAITING_PLAYERS.name()).orElse(null);
    }
    public void saveNewGamePlayer(CurrentGameDAO game) {
        gameRepository.save(game);
    }
    public void saveNewGameAI(CurrentGameAIDAO game) {
        gameRepositoryAI.save(game);
    }
    public void updateGameMultiplayer(CurrentGameDAO game) {
        gameRepository.save(game);
    }
    public void updateGameAI(CurrentGameAIDAO game) {
        gameRepositoryAI.save(game);
    }
    public List<CurrentGameDAO> getMultiplayerGames() {
        Iterable<CurrentGameDAO> iter = gameRepository.findAll();
        List<CurrentGameDAO> returned = new ArrayList<>();
        iter.forEach(returned::add);
        return returned;
    }
    public List<CurrentGameAIDAO> getAIGames() {
        Iterable<CurrentGameAIDAO> iter = gameRepositoryAI.findAll();
        List<CurrentGameAIDAO> returned = new ArrayList<>();
        iter.forEach(returned::add);
        return returned;
    }
    public List<CurrentGameDAO> getMultiplayerGamesForPlayer(String player) {
        UUID playerUUID = getUserUUIDbyLogin(player);
        List<CurrentGameDAO> returned = gameRepository.findByX(playerUUID);
        returned.addAll(gameRepository.findByO(playerUUID));
        return returned;
    }
    public List<CurrentGameAIDAO> getAIGamesForPlayer(String player) {
        UUID playerUUID = getUserUUIDbyLogin(player);
        List<CurrentGameAIDAO> returned = gameRepositoryAI.findByPlayer(playerUUID);
        return returned;
    }

    public UserDAO getUserByName(String name) {
        return userRepository.findByLogin(name).orElse(null);
    }
    public CurrentGameDAO getPlayerGameByUUID(UUID uuid) {
        return gameRepository.findByUuid(uuid).get();
    }
    public CurrentGameAIDAO getAIGameByUUID(UUID uuid) {
        return gameRepositoryAI.findByUuid(uuid).get();
    }
    public UUID getUserUUIDbyLogin(String login) {
        return userRepository.findByLogin(login).get().getUuid();
    }
    public UUID getSearchPlayer() {
        return searchRepository.getElem();
    }
    public void addSearchPlayer(UUID uuid) {
        searchRepository.add(uuid);
    }
    public void saveGameInFoundedRepository(UUID searcher, CurrentGameDAO game) {
        foundedGamesRepository.addToList(searcher, game);
    }
    public CurrentGameDAO checkInFoundedRepository(UUID player) {
        return foundedGamesRepository.checkInList(player);
    }
}
