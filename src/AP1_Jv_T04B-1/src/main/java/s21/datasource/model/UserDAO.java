package s21.datasource.model;

import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Table(name="users")
@Entity
public class UserDAO {
    @Id
    private UUID uuid;
    @Column(length=30)
    private String login;
    @Column(length=128)
    private String password;
}
