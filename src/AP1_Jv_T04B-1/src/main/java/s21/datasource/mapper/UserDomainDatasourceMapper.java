package s21.datasource.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.datasource.model.UserDAO;
import s21.domain.model.User;

@Mapper
public interface UserDomainDatasourceMapper {
    UserDomainDatasourceMapper INSTANCE = Mappers.getMapper(UserDomainDatasourceMapper.class);

    default UserDAO domainToDatasource(User domain) {
        return new UserDAO(
                        domain.getUuid(), 
                        domain.getUsername(), 
                        domain.getPassword()
                    );
    }
    
    default List<UserDAO> domainToDatasourceList(List<User> domain) {
        return domain.stream().map(this::domainToDatasource).toList();
    }
}
