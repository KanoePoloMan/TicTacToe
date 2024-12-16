package s21.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import s21.datasource.model.ServiceImpl;
import s21.datasource.repository.Repository;
import s21.domain.mapper.CurrentGameDatasourceDomainMapper;
import s21.domain.model.CurrentGame;
import s21.domain.model.GameField;

@Component
public class GameFieldLogic {
    @Autowired
    private Repository repository;
    @Autowired
    private ServiceImpl service;

    CurrentGameDatasourceDomainMapper mapper = CurrentGameDatasourceDomainMapper.INSTANCE;

    public CurrentGame createNewGame() {
        CurrentGame game = new CurrentGame(new GameField());
        repository.saveGame(mapper.domainToDatasource(game));
        return game;
    }
    public CurrentGame updateFieldAndGetNextStep(CurrentGame newField) throws Exception {
        service.setGame(mapper.domainToDatasource(newField));
        CurrentGame result = 
            new CurrentGame(newField.getUuid(), 
                            new GameField(service.nextStep()));
        repository.updateGame(mapper.domainToDatasource(result));

        return result;
    } 
    public void deleteGame(UUID uuid) {
        repository.deleteGame(uuid);
    }
    public List<UUID> getGamesList() {
        return repository.getAllGamesUUID();
    }
}
