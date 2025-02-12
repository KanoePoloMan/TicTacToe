package s21.domain.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import s21.datasource.mapper.CurrentGameAIDomainDatasourceMapper;
import s21.datasource.mapper.CurrentGameDomainDatasourceMapper;
import s21.datasource.model.CurrentGameAIDAO;
import s21.datasource.model.CurrentGameDAO;
import s21.datasource.repository.Repository;
import s21.domain.exception.AIGameEndException;
import s21.domain.exception.GameEndException;
import s21.domain.exception.InvalidAIFieldException;
import s21.domain.exception.InvalidFieldException;
import s21.domain.mapper.CurrentGameAIDatasourceDomainMapper;
import s21.domain.mapper.CurrentGameDatasourceDomainMapper;
import s21.domain.mapper.UserDatasourceDomainMapper;
import s21.domain.service.GameService;

@Component
public class GameProcessor implements GameService {
    @Autowired
    private Repository repository;

    private final CurrentGameDatasourceDomainMapper toDomainMapper = CurrentGameDatasourceDomainMapper.INSTANCE;
    private final CurrentGameAIDatasourceDomainMapper toDomainMapperAI = CurrentGameAIDatasourceDomainMapper.INSTANCE;
    private final CurrentGameAIDomainDatasourceMapper toDatasourceMapperAI = CurrentGameAIDomainDatasourceMapper.INSTANCE;
    private final CurrentGameDomainDatasourceMapper toDatasourceMapper = CurrentGameDomainDatasourceMapper.INSTANCE;

    private final UserDatasourceDomainMapper userMapper = UserDatasourceDomainMapper.INSTANCE;

    public static final int NOTHING_CODE = 0;
    public static final int ZERO_CODE = 1;
    public static final int CROSS_CODE = 2;
    public static final int DRAW = 3;


    /**
     * Ищет уже в очереди игроков, которые тожу ищут игру.
     * В случае нахождения создает игру и записывает в бд.
     * Если игра не найдена, записывает в очередь.
     * @param login UUID пользователя, который ищет игру
     * @return Созданная игра, или null.
     */
    public CurrentGame findGame(String login) {
        UUID searchPlayer = repository.getSearchPlayer();
        UUID currentPlayer = repository.getUserUUIDbyLogin(login);
        if(searchPlayer == null) {
            repository.addSearchPlayer(currentPlayer);
            return null;
        } else {
            CurrentGame newGame = new CurrentGame(
                                        UUID.randomUUID(),
                                        searchPlayer,
                                        currentPlayer,
                                        new GameField(),
                                        GameState.STEP_X
                                    );
            repository.saveNewGamePlayer(toDatasourceMapper.domainToDatasource(newGame));
            repository.saveGameInFoundedRepository(searchPlayer, toDatasourceMapper.domainToDatasource(newGame));
            return newGame;
        }
    }
    public CurrentGame getMultiplayerGameByUUID(UUID uuid) {
        return toDomainMapper.datasourceToDomain(repository.getPlayerGameByUUID(uuid));
    }
    public CurrentGameAI createGameWithAI(String user) {
        Random random = new Random();
        CurrentGameAI result = new CurrentGameAI(
                                    UUID.randomUUID(), 
                                    repository.getUserUUIDbyLogin(user), 
                                    random.nextBoolean(), 
                                    new GameField(),
                                    GameState.STEP_X);
        repository.saveNewGameAI(toDatasourceMapperAI.domainToDatasource(result));
        return result;
    }
    public UUID getPlayerUuidByName(String name) {
        User user = userMapper.datasourceToDomain(repository.getUserByName(name));
        if(user == null) return null;
        return user.getUuid();
    }
    public UUID checkInFoundedList(String nickname) {
        CurrentGame game = toDomainMapper.datasourceToDomain(
                                            repository.checkInFoundedRepository(repository.getUserUUIDbyLogin(nickname)));
        if(game == null) return null;
        return game.getUuid();
    }
    public CurrentGame updateGame(CurrentGame game) throws Exception {
        checkFieldChanges(game.getUuid(), game.getGameField().getGameField());

        GameState state = game.getGameState();
        if(state == GameState.DRAW || state == GameState.WIN_O || state == GameState.WIN_X)
            throw new GameEndException(game, "Game ended with: " + state.name());

        GameField field = game.getGameField();
        if(getNothingCells(field.getGameField()).isEmpty()) game.setGameState(GameState.DRAW);
        else if(checkGameEnd(field.getGameField(), CROSS_CODE)) game.setGameState(GameState.WIN_X);
        else if(checkGameEnd(field.getGameField(), ZERO_CODE)) game.setGameState(GameState.WIN_O);
        else if(isCurrentStepX(field)) game.setGameState(GameState.STEP_X);
        else game.setGameState(GameState.STEP_O);

        repository.updateGameMultiplayer(toDatasourceMapper.domainToDatasource(game));

        return game;
    }
    public void updateGame(CurrentGameAI game) {
        repository.updateGameAI(toDatasourceMapperAI.domainToDatasource(game));
    }
    public List<String> getAllGames() {
        List<String> returned = new ArrayList<>();
        returned.addAll(repository.getMultiplayerGames()
                                  .stream()
                                  .map(CurrentGameDAO::toString)
                                  .toList()
                        );
        returned.addAll(repository.getAIGames().stream().map(CurrentGameAIDAO::toString).toList());
        return returned;
    }
    public List<String> getMultiplayerGames() {
        return repository.getMultiplayerGames().stream().map(CurrentGameDAO::toString).toList();
    }
    public List<String> getAIGames() {
        return repository.getAIGames().stream().map(CurrentGameAIDAO::toString).toList();
    }

    private boolean isCurrentStepX(GameField field) {
        int X = 0;
        int O = 0;

        int fieldMatrix[][] = field.getGameField();
        for(int i[] : fieldMatrix) {
            for(int j : i) {
                if(j == CROSS_CODE) X++;
                if(j == ZERO_CODE) O++;
            }
        }
        return X == O;
    }
    @Override
    public CurrentGameAI nextStep(CurrentGameAI game) throws Exception {
        if(!fieldValidationIsOk(game)) throw new InvalidAIFieldException(game, "Invalid field");

        GameState state = game.getGameState();
        if(state == GameState.DRAW || state == GameState.WIN_O || state == GameState.WIN_X)
            throw new AIGameEndException(game, "Game ended");

        GameField field = game.getField();
        if(getNothingCells(field.getGameField()).isEmpty()) game.setGameState(GameState.DRAW);
        else if(checkGameEnd(field.getGameField(), CROSS_CODE)) game.setGameState(GameState.WIN_X);
        else if(checkGameEnd(field.getGameField(), ZERO_CODE)) game.setGameState(GameState.WIN_O);
        else game.setField(new GameField(calculateNextStep(game.getField(), game.isX())));

        return game;
    }

    private int[][] calculateNextStep(GameField field, boolean isX) {
        int[][] returned = Arrays.stream(field.getGameField()).map(int[]::clone).toArray(int[][]::new);

        int[] bestStep = minimax(returned, !isX).getValue();

        returned[bestStep[0]][bestStep[1]] = !isX == true ? CROSS_CODE : ZERO_CODE;

        return returned;
    }

    @Override
    public boolean fieldValidationIsOk(CurrentGame game) {
        int differenceCount = 0;

        int[][] currentField = game.getGameField().getGameField();
        
        int[][] repoField = toDomainMapper.datasourceToDomain(repository.getPlayerGameByUUID(game.getUuid()))
                                  .getGameField()
                                  .getGameField();

        for (int i = 0; i < repoField.length; i++)
            for(int j = 0; j < repoField[0].length; j++) {
                if(repoField[i][j] != currentField[i][j]) differenceCount++;
                // if(repoField[i][j] != ZERO_CODE && currentField[i][j] == ZERO_CODE) return false;
                if(differenceCount > 1) return false;
            }
        return true;
    }
    @Override
    public boolean fieldValidationIsOk(CurrentGameAI game) {
        int differenceCount = 0;

        int[][] currentField = game.getField().getGameField();
        
        int[][] repoField = toDomainMapperAI.datasourceToDomain(repository.getAIGameByUUID(game.getUuid()))
                                  .getField()
                                  .getGameField();

        for (int i = 0; i < repoField.length; i++)
            for(int j = 0; j < repoField[0].length; j++) {
                if(repoField[i][j] != currentField[i][j]) differenceCount++;
                // if(repoField[i][j] != ZERO_CODE && currentField[i][j] == ZERO_CODE) return false;
                if(differenceCount > 1) return false;
            }
        return true;
    }
    @Override
    public boolean gameIsEnded(CurrentGame game) {
        return gameIsEnded(game.getGameField().getGameField());
    }
    @Override
    public boolean gameIsEnded(CurrentGameAI game) {
        return gameIsEnded(game.getField().getGameField());
    }

    private boolean checkGameEnd(int[][] field, int code) {
        boolean right = true;
        boolean left = true;
        for(int i = 0; i < 3; i++) {
            right &= (field[i][i] == code);
            left &= (field[3 - i - 1][i] == code);

            boolean rows = true;
            boolean cols = true;
            for(int j = 0; j < 3; j++) {
                cols &= (field[i][j] == code);
                rows &= (field[j][i] == code);
            }
            if(cols || rows) return true;
        }
        return right || left; 
    }

    private List<int[]> getNothingCells(int[][] cur) {
        List<int[]> result = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(cur[i][j] == NOTHING_CODE) 
                    result.add(new int[]{i, j});
        return result;
    }
    private Map.Entry<Integer, int[]> minimax(int[][] field, boolean isX) {
        if(gameIsEnded(field)) return new AbstractMap.SimpleEntry<>(evaluateGame(field, isX), null);

        int[] bestMove = new int[2];
        int bestValue;
        int symbol;

        if(isX) {
            bestValue = Integer.MIN_VALUE;
            symbol = CROSS_CODE;
        } else {
            bestValue = Integer.MAX_VALUE;
            symbol = ZERO_CODE;
        }
        for(int[] move : getNothingCells(field)) {
            int[][] newField = Arrays.stream(field).map(int[]::clone).toArray(int[][]::new);

            newField[move[0]][move[1]] = symbol;

            int hypothetical_value = minimax(newField, !isX).getKey();
            if(isX && hypothetical_value > bestValue) {
                bestValue = hypothetical_value;
                bestMove = new int[]{move[0], move[1]};
            } 
            if(!isX && hypothetical_value < bestValue) {
                bestValue = hypothetical_value;
                bestMove = new int[]{move[0], move[1]};
            }
        }
        return new AbstractMap.SimpleEntry<>(bestValue, bestMove);
    }
    public boolean gameIsEnded(int [][] field) {
        return checkGameEnd(field, ZERO_CODE) 
            || checkGameEnd(field, CROSS_CODE) 
            || getNothingCells(field).isEmpty();
    }
    private int evaluateGame(int [][] field, boolean isX) {
        if(checkGameEnd(field, ZERO_CODE)) return isX ? -1 : 1;
        else if(checkGameEnd(field, CROSS_CODE)) return isX ? 1 : -1;
        else return 0;
    }
    public CurrentGameAI getAIGameByUUID(UUID uuid) {
        return toDomainMapperAI.datasourceToDomain(repository.getAIGameByUUID(uuid));
    }
    public CurrentGame getGameByUUID(UUID uuid) {
        return toDomainMapper.datasourceToDomain(repository.getPlayerGameByUUID(uuid));
    }
    public CurrentGame checkFieldChanges(UUID uuid, int[][] field) throws Exception {
        CurrentGame game = toDomainMapper.datasourceToDomain(repository.getPlayerGameByUUID(uuid));

        int changesCount = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(game.getGameField().getGameField()[i][j] != field[i][j]) changesCount++;
            }
        }
        if(changesCount > 1) throw new InvalidFieldException(game, "Bad field in multiplayer");
        return game;
    }
}
