package s21.web.model;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import s21.domain.service.AuthorizationService;

@Component
public class AuthFilter extends GenericFilterBean {
    
    @Autowired
    private AuthorizationService authorizationService;

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
                
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        System.out.println("Auth Filter");

        String method = request.getMethod();
        String contentType = request.getContentType();
        String servletPath = request.getServletPath();
                
        System.out.println("Auth Filter Method: " + method);
        System.out.println("Auth Filter Content Type: " + contentType);
        System.out.println("Auth Filter Sevlet Path: " + servletPath);

        if(method.equalsIgnoreCase("POST") && contentType.equalsIgnoreCase("application/x-www-form-urlencoded")
            && servletPath.equals("/auth")) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
                
            if(username != null) {
                try {
                    System.out.println("Auth Filter: Authorization");
                    authorizationService.authorization(Base64Coder.encodeString(username + ":" + password));
                    chain.doFilter(request, response);
                    return;
                } catch(UsernameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username: " + username);
                } catch(BadCredentialsException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid password for: " + username);
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization with null user: " + username);
            }
        } else {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
