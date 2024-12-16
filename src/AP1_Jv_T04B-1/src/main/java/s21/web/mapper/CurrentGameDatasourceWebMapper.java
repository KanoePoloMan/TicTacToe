package s21.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.datasource.model.CurrentGameDAO;
import s21.datasource.model.GameFieldDAO;
import s21.web.model.CurrentGameDTO;
import s21.web.model.GameFieldDTO;

@Mapper
public interface CurrentGameDatasourceWebMapper {
    CurrentGameDatasourceWebMapper INSTANCE = Mappers.getMapper(CurrentGameDatasourceWebMapper.class);

    default CurrentGameDAO webToDatasource(CurrentGameDTO web) {
        return
            new CurrentGameDAO(
                web.uuid(), 
                new GameFieldDAO(
                    web.gameField().gameField()));
    }
    default CurrentGameDTO datasourceToWeb(CurrentGameDAO domain) {
        return new CurrentGameDTO(domain.getUuid(), new GameFieldDTO(domain.getGameField().getGameField()), null);
    }
    default CurrentGameDTO datasourceToWeb(CurrentGameDAO domain, String error) {
        return new CurrentGameDTO(domain.getUuid(), new GameFieldDTO(domain.getGameField().getGameField()), error);
    }
}
