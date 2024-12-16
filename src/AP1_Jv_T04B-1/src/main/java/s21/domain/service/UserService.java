package s21.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import s21.datasource.mapper.UserDomainDatasourceMapper;
import s21.datasource.model.UserDAO;
import s21.datasource.repository.UserRepository;
import s21.domain.model.User;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private final UserDomainDatasourceMapper mapper = UserDomainDatasourceMapper.INSTANCE;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return mapper.datasourceToDomain(userRepository.findByLogin(username).orElse(null));
    }
    public List<User> getAll() {
        return mapper.datasourceToDomainList((List<UserDAO>)userRepository.findAll());
    }
    public void saveUser(User user) {
        userRepository.save(mapper.domainToDatasource(user));
    }
}
