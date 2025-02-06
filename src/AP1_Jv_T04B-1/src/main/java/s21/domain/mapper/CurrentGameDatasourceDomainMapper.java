package s21.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.datasource.model.CurrentGameDAO;
import s21.domain.model.CurrentGame;
import s21.domain.model.GameState;

@Mapper
public interface CurrentGameDatasourceDomainMapper {
    CurrentGameDatasourceDomainMapper INSTANCE = Mappers.getMapper(CurrentGameDatasourceDomainMapper.class);
    GameFieldDatasourceDomainMapper gameFieldToDomain = GameFieldDatasourceDomainMapper.INSTANCE;

    default CurrentGame datasourceToDomain(CurrentGameDAO datasource) {
        if(datasource == null) return null;
        return new CurrentGame(
                    datasource.getUuid(), 
                    datasource.getX(),
                    datasource.getO(),
                    gameFieldToDomain.datasourceToDomain(datasource.getField()),
                    GameState.valueOf(datasource.getState()));
    }
}
