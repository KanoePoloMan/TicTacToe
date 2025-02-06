package s21.domain.service;

import s21.domain.model.CurrentGame;
import s21.domain.model.CurrentGameAI;

public interface GameService {
    int[][] nextStep(CurrentGame game) throws Exception;
    int[][] nextStep(CurrentGameAI game) throws Exception;
    boolean fieldValidationIsOk(CurrentGame game);
    boolean fieldValidationIsOk(CurrentGameAI game);
    boolean gameIsEnded(CurrentGame game);
    boolean gameIsEnded(CurrentGameAI game);
}
