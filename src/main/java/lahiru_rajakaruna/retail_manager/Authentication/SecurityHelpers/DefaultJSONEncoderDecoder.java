package lahiru_rajakaruna.retail_manager.Authentication.SecurityHelpers;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import lahiru_rajakaruna.retail_manager.Common.JSONEncoderDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Set;

@Component
public class DefaultJSONEncoderDecoder implements JSONEncoderDecoder {
    private final JWSSigner signer;
    private final JWSVerifier verifier;

    private final String ISSUER = "RETAIL_MANAGER_API";
    private final String AUDIENCE = "RETAIL_MANAGER_REACT";
    private final TemporalAmount TOKEN_LIFE_TIME = Duration.ofMinutes(30);

    public DefaultJSONEncoderDecoder(@Value("${spring.security.jwt.key}") String jwtSignKey) {
        byte[] signKey = jwtSignKey.getBytes(StandardCharsets.UTF_8);
        try {
            this.signer = new MACSigner(signKey);
            this.verifier = new MACVerifier(signKey);
        } catch (JOSEException e) {
            throw new IllegalStateException("Invalid JWT sign key (HS256 need at least 32bytes)");
        }
    }

    @Override
    public String encode(String token) {
        Instant now = Instant.now();
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).contentType("JSON")
                                                                    .type(JOSEObjectType.JWT)
                                                                    .build();

        JWTClaimsSet claimSet = new JWTClaimsSet.Builder().audience(AUDIENCE)
                                                          .issuer(ISSUER)
                                                          .issueTime(Date.from(now))
                                                          .notBeforeTime(Date.from(now))
                                                          .expirationTime(Date.from(now.plus(TOKEN_LIFE_TIME)))
                                                          .claim("token", token)
                                                          .build();

        SignedJWT jwt = new SignedJWT(header, claimSet);

        try {
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new IllegalStateException("Could not sign jwt", e);
        }

        return jwt.toString();
    }

    @Override
    public String decode(String jwt) {
        try {
            SignedJWT sjwt = SignedJWT.parse(jwt);

            if (!JWSAlgorithm.HS256.equals(sjwt.getHeader()
                                               .getAlgorithm())) {
                throw new IllegalArgumentException("Unexpected JWS Algorithm");
            }
            if (!sjwt.verify(verifier)) {
                throw new IllegalArgumentException("Invalid JWT Signature");
            }

            JWTClaimsSet claimSet = sjwt.getJWTClaimsSet();
            new DefaultJWTClaimsVerifier<>(AUDIENCE, new JWTClaimsSet.Builder().issuer(ISSUER)
                                                                               .build(), Set.of("exp", "iat", "nbf")).verify(claimSet, null);

            return claimSet.getClaimAsString("token");
        } catch (ParseException e) {
            throw new RuntimeException("Invalid token, could not parse", e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Unsigned token, could not verify", e);
        } catch (JOSEException e) {
            throw new IllegalArgumentException("Invalid token, could not verify", e);
        } catch (BadJWTException e) {
            throw new IllegalArgumentException("Bad JWT", e);
        }
    }
}
