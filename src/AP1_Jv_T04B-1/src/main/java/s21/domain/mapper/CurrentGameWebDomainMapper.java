package s21.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.domain.model.CurrentGame;
import s21.domain.model.GameField;
import s21.web.model.CurrentGameDTO;
import s21.web.model.GameFieldDTO;

@Mapper
public interface CurrentGameWebDomainMapper {
    CurrentGameWebDomainMapper INSTANCE = Mappers.getMapper(CurrentGameWebDomainMapper.class);

    default CurrentGameDTO domainToWeb(CurrentGame domain) {
        return
            new CurrentGameDTO(
                domain.getUuid(), 
                new GameFieldDTO(
                    domain.getGameField().getGameField()), 
                null);
    }
    default CurrentGame webToDomain(CurrentGameDTO web) {
        return new CurrentGame(web.uuid(), new GameField(web.gameField().gameField()));
    }
}
