package s21.domain.service;

public interface Service {
    int[][] nextStep() throws Exception;
    boolean fieldValidationIsOk();
    boolean gameIsEnded();
}
