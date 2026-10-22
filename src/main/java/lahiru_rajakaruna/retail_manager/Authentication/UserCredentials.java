package lahiru_rajakaruna.retail_manager.Authentication;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record UserCredentials(@Nullable String phone, @NotNull String rawCSRFToken) {
}