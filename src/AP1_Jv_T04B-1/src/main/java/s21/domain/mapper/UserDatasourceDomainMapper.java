package s21.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.datasource.model.UserDAO;
import s21.domain.model.Role;
import s21.domain.model.User;

@Mapper
public interface UserDatasourceDomainMapper {
    UserDatasourceDomainMapper INSTANCE = Mappers.getMapper(UserDatasourceDomainMapper.class);

    default User datasourceToDomain(UserDAO datasource) {
        if(datasource == null) return null;
        return new User(List.of(Role.USER), 
                        datasource.getUuid() , 
                        datasource.getLogin(), 
                        datasource.getPassword()
                    );
    }
    default List<User> datasourceToDomainList(List<UserDAO> datasource) {
        return datasource.stream().map(this::datasourceToDomain).toList();
    }
}
