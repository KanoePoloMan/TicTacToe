package s21.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.datasource.model.CurrentGameDAO;
import s21.datasource.model.GameFieldDAO;
import s21.domain.model.CurrentGame;
import s21.domain.model.GameField;

@Mapper
public interface CurrentGameDatasourceDomainMapper {
    CurrentGameDatasourceDomainMapper INSTANCE = Mappers.getMapper(CurrentGameDatasourceDomainMapper.class);

    default CurrentGameDAO domainToDatasource(CurrentGame domain) {
        return
            new CurrentGameDAO(
                domain.getUuid(), 
                new GameFieldDAO(
                    domain.getGameField().getGameField()));
    }
    default CurrentGame datasourceToDomain(CurrentGameDAO datasource) {
        return new CurrentGame(datasource.getUuid(), new GameField(datasource.getGameField().getGameField()));
    }
}