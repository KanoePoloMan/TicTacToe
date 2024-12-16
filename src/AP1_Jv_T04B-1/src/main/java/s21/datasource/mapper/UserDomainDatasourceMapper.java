package s21.datasource.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import s21.datasource.model.UserDAO;
import s21.domain.model.Role;
import s21.domain.model.User;

@Mapper
public interface UserDomainDatasourceMapper {
    UserDomainDatasourceMapper INSTANCE = Mappers.getMapper(UserDomainDatasourceMapper.class);

    default User datasourceToDomain(UserDAO datasource) {
        return new User(List.of(Role.USER), UUID.fromString(datasource.getUuid()) , datasource.getLogin(), datasource.getPassword());
    }
    default UserDAO domainToDatasource(User domain) {
        return new UserDAO(domain.getUuid().toString(), domain.getUsername(), domain.getPassword());
    }
    default List<User> datasourceToDomainList(List<UserDAO> datasource) {
        return datasource.stream().map(this::datasourceToDomain).toList();
    }
    default List<UserDAO> domainToDatasourceList(List<User> domain) {
        return domain.stream().map(this::domainToDatasource).toList();
    }
}
