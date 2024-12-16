package s21.datasource.model;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Component
@Entity
@Table(name = "userdao")
public class UserDAO {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String uuid;
    private String login;
    private String password;

    public UserDAO(String uuid, String login, String password) {
        this.uuid = uuid;
        this.login = login;
        this.password = password;
    }
}
