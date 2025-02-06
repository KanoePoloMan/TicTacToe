package s21.domain.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthentication implements Authentication {
    private final UUID uuid;
    private final String name;
    private final String credentials;
    private boolean authenticated;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomAuthentication(UUID uuid, String name, String credentials, boolean authenticated,
            Collection<? extends GrantedAuthority> authorities) {
        this.uuid = uuid;
        this.name = name;
        this.credentials = credentials;
        this.authenticated = authenticated;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return name;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
    public UUID getUserUuid() {
        return uuid;
    }

}
