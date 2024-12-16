package s21.domain.service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import s21.domain.model.User;
import s21.web.model.SignUpRequest;

@Service
public class AuthorizationService {
    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public boolean registration(SignUpRequest request) {
        final UserDetails userDetails = userService.loadUserByUsername(request.login());
        if(userDetails != null) return false;

        //register
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUsername(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));

        userService.saveUser(user);

        return true;
    }
    public UUID authorization(String loginPassword) {
        final String token = new String(Base64Coder.decode(loginPassword), StandardCharsets.UTF_8);
        final var tokens = token.split(":");

        final String login = new String(Base64Coder.encode(tokens[0].getBytes()));
        final String password = new String(Base64Coder.encode(tokens[1].getBytes()));

        final User user = (User) userService.loadUserByUsername(login);
        if(user == null) return null;

        if(passwordEncoder.encode(password).equals(user.getPassword())) return user.getUuid();

        return null;
    }
}
