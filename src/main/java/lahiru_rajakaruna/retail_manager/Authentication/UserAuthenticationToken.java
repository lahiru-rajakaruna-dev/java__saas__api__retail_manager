package lahiru_rajakaruna.retail_manager.Authentication;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationToken extends AbstractAuthenticationToken {
    private final UserCredentials credentials;
    private final String principal;

    public UserAuthenticationToken(String principal, UserCredentials credentials, Collection<GrantedAuthority> grantedAuthorities) {
        super(grantedAuthorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public @Nullable UserCredentials getCredentials() {
        return credentials;
    }

    @Override
    public @Nullable String getPrincipal() {
        return principal;
    }
}
