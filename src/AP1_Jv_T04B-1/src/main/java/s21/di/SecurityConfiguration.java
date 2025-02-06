package s21.di;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import s21.web.model.AuthFilter;

// import s21.web.model.AuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AuthFilter authFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request
                .requestMatchers("/auth", "/reg").anonymous()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()                
            )
            .formLogin(form -> form.loginPage("/auth")
                                   .defaultSuccessUrl("/menu"))
            .build();
    }
}
