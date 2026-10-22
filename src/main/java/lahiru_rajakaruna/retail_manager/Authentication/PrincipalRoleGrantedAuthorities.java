package lahiru_rajakaruna.retail_manager.Authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrincipalRoleGrantedAuthorities {
    private static final String[] ADMIN = new String[]{"AUTH", "TENANT", "SALE", "INVENTORY", "CREDIT", "PAYMENT"};
    private static final String[] TENANT = new String[]{"AUTH", "SALE", "INVENTORY", "CREDIT", "PAYMENT"};
    private static final String[] GUEST = new String[]{"AUTH"};

    private PrincipalRoleGrantedAuthorities() {
    }

    public static List<GrantedAuthority> get(UserRole role) {
        switch (role) {
            case ADMIN -> {
                return Stream.of(ADMIN)
                             .map(SimpleGrantedAuthority::new)
                             .collect(Collectors.toList());
            }
            case GUEST -> {
                return Stream.of(GUEST)
                             .map(SimpleGrantedAuthority::new)
                             .collect(Collectors.toList());
            }
            case TENANT -> {
                return Stream.of(TENANT)
                             .map(SimpleGrantedAuthority::new)
                             .collect(Collectors.toList());
            }
            default -> {
                return List.of();
            }
        }
    }
}
