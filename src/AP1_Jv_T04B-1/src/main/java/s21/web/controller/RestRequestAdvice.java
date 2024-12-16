package s21.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import s21.datasource.model.exception.GameEndException;
import s21.datasource.model.exception.InvalidFieldException;
import s21.web.mapper.CurrentGameDatasourceWebMapper;
import s21.web.model.CurrentGameDTO;

@ControllerAdvice
public class RestRequestAdvice {
    private final CurrentGameDatasourceWebMapper mapper = CurrentGameDatasourceWebMapper.INSTANCE;

    @ExceptionHandler(GameEndException.class)
    public ResponseEntity<CurrentGameDTO> handleGameEnd(GameEndException e) {
        CurrentGameDTO returned = mapper.datasourceToWeb(e.getCurrentGame(), e.getMessage());
        return new ResponseEntity<>(returned, HttpStatus.OK);
    }
    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<CurrentGameDTO> handleGameEnd(InvalidFieldException e) {
        CurrentGameDTO returned = mapper.datasourceToWeb(e.getCurrentGame(), e.getMessage());
        return new ResponseEntity<>(returned, HttpStatus.OK);
    }
}
