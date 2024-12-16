package s21.datasource.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import s21.datasource.model.exception.GameEndException;
import s21.datasource.model.exception.InvalidFieldException;
import s21.datasource.repository.Repository;
import s21.domain.service.Service;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class ServiceImpl implements Service {
    @Autowired
    private final Repository repository;
    
    private static final int NOTHING_CODE = 0;
    private static final int ZERO_CODE = 1;
    private static final int CROSS_CODE = 2;

    private int currentPlayer;
    private int bot;
    private int player;

    @Setter
    private CurrentGameDAO game;

    @Override
    public int[][] nextStep() throws Exception {
        if(gameIsEnded()) throw new GameEndException(game, "Game ended");
        if(!fieldValidationIsOk()) throw new InvalidFieldException(game, "Invalid field");
        findCurrentPlayer();

        int[][] returned = new int[3][3];
        arrayCopy(game.getGameField().getGameField(), returned);
        int[] bestStep = minimax(returned, true).getValue();
        returned[bestStep[0]][bestStep[1]] = currentPlayer;
        return returned;
    }

    @Override
    public boolean fieldValidationIsOk() {
        int differenceCount = 0;
        CurrentGameDAO currentGame = repository.getGame(game.getUuid());
        int[][] repoField = currentGame.getGameField().getGameField();
        int[][] currentField = game.getGameField().getGameField();
        for (int i = 0; i < repoField.length; i++)
            for(int j = 0; j < repoField[0].length; j++) {
                if(repoField[i][j] != currentField[i][j]) differenceCount++;
                // if(repoField[i][j] != ZERO_CODE && currentField[i][j] == ZERO_CODE) return false;
                if(differenceCount > 1) return false;
            }

        return true;
    }

    @Override
    public boolean gameIsEnded() {
        return checkGameEnd(game.getGameField().getGameField(), ZERO_CODE) 
            || checkGameEnd(game.getGameField().getGameField(), CROSS_CODE)
            || getNothingCells(game.getGameField().getGameField()).isEmpty();
    }
    public boolean gameIsOver(int[][] field) {
        return checkGameEnd(field, ZERO_CODE) 
            || checkGameEnd(field, CROSS_CODE)
            || getNothingCells(field).isEmpty();
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
    
    private Map.Entry<Integer, int[]> minimax(int[][] field, boolean isMax) {
        if(gameIsOver(field)) return new AbstractMap.SimpleEntry<>(evaluateGame(field), null);

        int[] bestMove = new int[2];
        int bestValue;
        int symbol;

        if(isMax) {
            bestValue = Integer.MIN_VALUE;
            symbol = ZERO_CODE;
        } else {
            bestValue = Integer.MAX_VALUE;
            symbol = CROSS_CODE;
        }
        for(int[] move : getNothingCells(field)) {
            int[][] newField = new int[3][3];
            arrayCopy(field, newField);

            newField[move[0]][move[1]] = symbol;

            int hypothetical_value = minimax(newField, !isMax).getKey();
            if(isMax && hypothetical_value > bestValue) {
                bestValue = hypothetical_value;
                bestMove = new int[]{move[0], move[1]};
            } 
            if(!isMax && hypothetical_value < bestValue) {
                bestValue = hypothetical_value;
                bestMove = new int[]{move[0], move[1]};
            }
        }
        return new AbstractMap.SimpleEntry<>(bestValue, bestMove);
    }
    private void arrayCopy(int[][] src, int[][] dest) {
        for(int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, src[i].length);
        }
    }

    private void findCurrentPlayer() {
        int[][] field = game.getGameField().getGameField();
        int x = 0;//counter X
        int o = 0;//counter O
        for (int[] field1 : field) {
            for (int j = 0; j < field[0].length; j++) {
                if (field1[j] == ZERO_CODE) o++;
                if (field1[j] == CROSS_CODE) x++;
            }
        }
        if(x == o) currentPlayer = ZERO_CODE;
        else currentPlayer = o > x ? CROSS_CODE : ZERO_CODE;

        bot = currentPlayer;
        player = currentPlayer == ZERO_CODE ? CROSS_CODE : ZERO_CODE;
    }
    private int evaluateGame(int [][] field) {
        if(checkGameEnd(field, bot)) return 1;
        else if(checkGameEnd(field, player)) return -1;
        else return 0;
    }
}
