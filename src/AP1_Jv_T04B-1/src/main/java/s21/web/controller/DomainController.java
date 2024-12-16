package s21.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import s21.domain.service.GameFieldLogic;
import s21.web.mapper.CurrentGameDomainWebMapper;
import s21.web.model.CurrentGameDTO;

@Component
public class DomainController {
    @Autowired
    GameFieldLogic logic;

    CurrentGameDomainWebMapper mapper = CurrentGameDomainWebMapper.INSTANCE;

    public CurrentGameDTO createNewGame() {
        return mapper.domainToWeb(logic.createNewGame());
    }
    public CurrentGameDTO updateFieldAndGetNextStep(CurrentGameDTO newField) throws Exception {
        return mapper.domainToWeb(logic.updateFieldAndGetNextStep(mapper.webToDomain(newField)));
    }
    public void deleteGame(UUID uuid) {
        logic.deleteGame(uuid);
    }
    public List<String> getGamesList() {
        return logic.getGamesList().stream().map(uuid -> uuid.toString()).toList();
    }
}
