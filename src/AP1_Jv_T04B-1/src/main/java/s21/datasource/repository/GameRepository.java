package s21.datasource.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import s21.datasource.model.CurrentGameDAO;


public interface GameRepository extends CrudRepository<CurrentGameDAO, UUID> {
    Optional<CurrentGameDAO> findByUuid(UUID uuid);
    // Optional<CurrentGameDAO> findByState(String ended);

    List<CurrentGameDAO> findByState(String ended);

    List<CurrentGameDAO> findByX(UUID login);
    List<CurrentGameDAO> findByO(UUID login);
}
