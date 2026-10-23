package lahiru_rajakaruna.retail_manager.Common;

import lahiru_rajakaruna.retail_manager.Authentication.DTOs.AuthSession;


public interface SessionManager {
    public AuthSession get(String token);

    public void set(String token, AuthSession session);

    public void set(String token, AuthSession session, int ttlInHours);

    public AuthSession delete(String token);
}
