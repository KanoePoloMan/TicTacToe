package s21.domain.service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import s21.domain.model.User;
import s21.web.model.SignUpRequest;

@Service
public class AuthorizationService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    public boolean registration(SignUpRequest request) {
        try {
            userService.loadUserByUsername(request.username());
        } catch (UsernameNotFoundException e) {
            //register
            User newUser = new User();
            newUser.setUuid(UUID.randomUUID());
            newUser.setUsername(request.username());
            newUser.setPassword(passwordEncoder.encode(request.password()));

            userService.saveUser(newUser);

            System.out.println("registrationSeccess");

            return true;
        }

        return false;
    }
    public UUID authorization(String loginPassword) {
        if(user == null) return null;
        if(validateUser(loginPassword)) return user.getUuid();

        return null;
    }
    public boolean validateUser(String loginPassword) {
        final String token = new String(Base64Coder.decode(loginPassword), StandardCharsets.UTF_8);
        final var tokens = token.split(":");

        final String login = tokens[0];
        final String password = tokens[1];

        try {
            user = (User) userService.loadUserByUsername(login);
        } catch (UsernameNotFoundException e) {
            return false;
        }

        return passwordEncoder.matches(password, user.getPassword());
    }
}
