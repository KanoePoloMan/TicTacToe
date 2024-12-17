package s21.web.model;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import s21.domain.service.AuthorizationService;

@Component
public class AuthFilter extends GenericFilterBean {
    @Nonnull
    private final AuthorizationService authorizationService;

    public AuthFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String authentication = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if(authentication != null && authentication.startsWith("Basic ")) {
            final String rawToken = authentication.replaceAll("^Basic ", "");

            if(authorizationService.authorization(rawToken) == null) {
                ((HttpServletResponse)response).sendError(401, "Bad authorization");
                return;
            }
        }
        chain.doFilter(request, response);
    }

}
