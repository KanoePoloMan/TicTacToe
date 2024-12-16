package s21.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.domain.model.CurrentGame;
import s21.domain.model.GameField;
import s21.web.model.CurrentGameDTO;
import s21.web.model.GameFieldDTO;

@Mapper
public interface CurrentGameDomainWebMapper {
    CurrentGameDomainWebMapper INSTANCE = Mappers.getMapper(CurrentGameDomainWebMapper.class);

    default CurrentGame webToDomain(CurrentGameDTO web) {
        return
            new CurrentGame(
                web.uuid(), 
                new GameField(
                    web.gameField().gameField()));
    }
    default CurrentGameDTO domainToWeb(CurrentGame domain) {
        return new CurrentGameDTO(domain.getUuid(), new GameFieldDTO(domain.getGameField().getGameField()), null);
    }
}
