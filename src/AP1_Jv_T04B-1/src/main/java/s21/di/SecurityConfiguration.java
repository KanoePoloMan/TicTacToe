package s21.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import s21.domain.service.AuthorizationService;

// import s21.web.model.AuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain0(HttpSecurity http) throws Exception {
        return http
            // .addFilterAfter(new AuthFilter(), AnonymousAuthenticationFilter.class)
            // .addFilterBefore(new AnonymousFilter(), AnonymousAuthenticationFilter.class)
            .authorizeHttpRequests(request -> request
                .requestMatchers(new AntPathRequestMatcher("/auth/**")).anonymous()
                .requestMatchers(new AntPathRequestMatcher("/test")).permitAll()
                .anyRequest().authenticated()                
            )
            .formLogin(Customizer.withDefaults())
            .build();
    }
    @Bean
    public AuthorizationService authorizationService() {
        return new AuthorizationService();
    }
}
