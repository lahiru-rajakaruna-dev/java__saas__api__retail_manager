package lahiru_rajakaruna.retail_manager.Authentication.DTOs;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken implements Authentication {
    private final Collection<? extends GrantedAuthority> authorities;
    private final String sessionToken;
    private boolean authenticated;

    public JWTAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String sessionToken) {
        this.authorities = authorities;
        this.sessionToken = sessionToken;
        this.authenticated = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                          .toList();
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return null;
    }

    @Override
    public @Nullable String getPrincipal() {
        return sessionToken;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }
}
