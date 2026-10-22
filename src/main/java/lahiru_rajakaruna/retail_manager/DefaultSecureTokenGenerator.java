package lahiru_rajakaruna.retail_manager;

import lahiru_rajakaruna.retail_manager.Common.SecureTokenGenerator;

import java.security.SecureRandom;
import java.util.Base64;


public class DefaultSecureTokenGenerator implements SecureTokenGenerator {
    private final SecureRandom sRand;
    public DefaultSecureTokenGenerator() {
        this.sRand = new SecureRandom();
    }
    @Override
    public String getToken(int byteLength) {
        byte[] bytes = new byte[byteLength];
        sRand.nextBytes(bytes);
        return Base64.getUrlEncoder()
                     .withoutPadding()
                     .encodeToString(bytes);
    }
}
