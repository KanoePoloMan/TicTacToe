package s21.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import s21.domain.service.AuthorizationService;
import s21.web.model.AuthFilter;

// import s21.web.model.AuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AuthorizationService authorizationService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .addFilterAfter(new AuthFilter(authorizationService), AnonymousAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request
                .requestMatchers("/auth/**").anonymous()
                .requestMatchers(HttpMethod.POST, "/auth/register").anonymous()
                .requestMatchers("/test", "/anon", "/js/**", "testPost").permitAll()
                .anyRequest().authenticated()                
            )
            .formLogin(form -> form.loginPage("/auth/authorization")
                                   .defaultSuccessUrl("/"))
            .build();
    }
}
