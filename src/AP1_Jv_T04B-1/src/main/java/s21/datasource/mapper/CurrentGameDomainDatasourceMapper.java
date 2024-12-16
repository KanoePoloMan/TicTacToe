package s21.datasource.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.datasource.model.CurrentGameDAO;
import s21.datasource.model.GameFieldDAO;
import s21.domain.model.CurrentGame;
import s21.domain.model.GameField;

@Mapper
public interface CurrentGameDomainDatasourceMapper {
    CurrentGameDomainDatasourceMapper INSTANCE = Mappers.getMapper(CurrentGameDomainDatasourceMapper.class);

    default CurrentGame datasourceToDomain(CurrentGameDAO domain) {
        return
            new CurrentGame(
                domain.getUuid(), 
                new GameField(
                    domain.getGameField().getGameField()));
    }
    default CurrentGameDAO domainToDatasource(CurrentGame domain) {
        return new CurrentGameDAO(domain.getUuid(), new GameFieldDAO(domain.getGameField().getGameField()));
    }
}
