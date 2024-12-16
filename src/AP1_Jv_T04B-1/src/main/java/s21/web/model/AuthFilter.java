package s21.web.model;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import s21.domain.service.AuthorizationService;

public class AuthFilter extends GenericFilterBean {
    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                
        final String authentication = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);

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
