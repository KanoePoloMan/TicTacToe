package s21.domain.service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
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
        System.out.println("Custom registration func");
        try {
            userService.loadUserByUsername(request.username());
        } catch (UsernameNotFoundException e) {
            System.out.println("Custom registration");
            //register
            User newUser = new User();
            newUser.setUuid(UUID.randomUUID());
            newUser.setUsername(request.username());
            newUser.setPassword(passwordEncoder.encode(request.password()));

            userService.saveUser(newUser);

            System.out.println("registrationSuccess");

            return true;
        }

        return false;
    }
    public UUID authorization(String loginPassword) {
        if(validateUser(loginPassword)) return user.getUuid();
        else throw new BadCredentialsException("Bad password");
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
    public String getCurrentUser() {
        if(user == null) return "unknown";
        return user.getUsername();
    }
}
