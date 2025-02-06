package s21.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import s21.domain.mapper.CurrentGameAIWebDomainMapper;
import s21.domain.mapper.CurrentGameWebDomainMapper;
import s21.domain.model.GameProcessor;
import s21.domain.model.GameState;
import s21.web.mapper.CurrentGameAIDomainWebMapper;
import s21.web.mapper.CurrentGameDomainWebMapper;
import s21.web.model.CurrentGameAIDTO;
import s21.web.model.CurrentGameDTO;
import s21.web.model.GameFieldDTO;

@Component
public class DomainController {
    @Autowired
    private GameProcessor logic;

    private final CurrentGameDomainWebMapper toWebMapper = CurrentGameDomainWebMapper.INSTANCE;
    private final CurrentGameAIDomainWebMapper toWebMapperAI = CurrentGameAIDomainWebMapper.INSTANCE;
    private final CurrentGameWebDomainMapper toDomainMapper = CurrentGameWebDomainMapper.INSTANCE;
    private final CurrentGameAIWebDomainMapper toDomainMapperAI = CurrentGameAIWebDomainMapper.INSTANCE;


    public CurrentGameDTO findGameWithPlayer(String nickname) {
        return toWebMapper.domainToWeb(logic.findGame(nickname));
    }
    public CurrentGameAIDTO findGameWithAI(String nickname) {
        return toWebMapperAI.domainToWeb(logic.createGameWithAI(nickname));
    }
    public CurrentGameAIDTO updateFieldAndGetNextStep(CurrentGameAIDTO newField) throws Exception {
        checkGameStateAI(newField);
        newField.setField(new GameFieldDTO(logic.nextStep(toDomainMapperAI.webToDomain(newField))));
        logic.updateGame(toDomainMapperAI.webToDomain(newField));
        return newField;
    }
    public CurrentGameDTO updateMultiplayerField(CurrentGameDTO newField) throws Exception {
        logic.updateGame(toDomainMapper.webToDomain(newField));
        return newField;
    }
    public List<String> getGames() {
        return logic.getAllGames();
    }
    public List<String> getMultiplayerGames() {
        return logic.getMultiplayerGames();
    }
    public List<String> getAIGames() {
        return logic.getAIGames();
    }
    public CurrentGameAIDTO initStartGame(UUID uuid) throws Exception {
        CurrentGameAIDTO game = toWebMapperAI.domainToWeb(logic.getAIGameByUUID(uuid));
        if(game.isX()) return game;
        else return updateFieldAndGetNextStep(game);
    }
    private void checkGameStateAI(CurrentGameAIDTO newField) {
        switch(logic.gameIsEnded(newField.getField().gameField())) {
            case GameProcessor.NOTHING_CODE -> {return;}
            case GameProcessor.CROSS_CODE -> newField.setState(GameState.WIN_X);
            case GameProcessor.ZERO_CODE -> newField.setState(GameState.WIN_O);
            case GameProcessor.DRAW -> newField.setState(GameState.DRAW);
        }
    }
    public UUID checkInFoundedList(String nickname) {
        return logic.checkInFoundedList(nickname);
    }
    public CurrentGameDTO getMultiplayerGameByUUID(String uuid) {
        return toWebMapper.domainToWeb(logic.getMultiplayerGameByUUID(UUID.fromString(uuid)));
    }
}
