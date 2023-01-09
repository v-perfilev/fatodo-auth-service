package com.persoff68.fatodo.security.oauth2.clientregistration;

import com.persoff68.fatodo.config.constant.Provider;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class AppleClientRegistrationFactory implements ClientRegistrationFactory {

    private static final long TOKEN_TTL = 86400L * 1000L * 90L; // 90 days

    @Value("${spring.security.oauth2.client.registration.apple.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.apple.redirectUri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.apple.scope}")
    private List<String> scope;
    @Value("${spring.security.oauth2.client.registration.apple.authorizationUri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.client.registration.apple.tokenUri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.registration.apple.jwkSetUri}")
    private String jwkSetUri;
    @Value("${spring.security.oauth2.client.registration.apple.appleTeamId}")
    private String appleTeamId;
    @Value("${spring.security.oauth2.client.registration.apple.appleClientId}")
    private String appleClientId;
    @Value("${spring.security.oauth2.client.registration.apple.appleKeyId}")
    private String appleKeyId;

    @Override
    public ClientRegistration create() {
        String name = Provider.APPLE.getValue();
        String registrationId = Provider.APPLE.getValue().toLowerCase();
        String clientSecret = generateClientSecret();

        return ClientRegistration
                .withRegistrationId(registrationId)
                .clientName(name)
                .clientId(clientId)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .scope(scope)
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .jwkSetUri(jwkSetUri)
                .build();
    }

    private String generateClientSecret() {
        try {
            PrivateKey pKey = generatePrivateKey();
            return Jwts.builder()
                    .setHeaderParam(JwsHeader.KEY_ID, appleKeyId)
                    .setIssuer(appleTeamId)
                    .setAudience("https://appleid.apple.com")
                    .setSubject(appleClientId)
                    .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TTL))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .signWith(SignatureAlgorithm.ES256, pKey)
                    .compact();
        } catch (IOException e) {
            log.error("Error while generating Apple client secret " + e.getMessage());
            return "";
        }
    }

    private PrivateKey generatePrivateKey() throws IOException {
        try (
                InputStream is = getClass().getClassLoader()
                        .getResourceAsStream("apple/AuthKey_" + appleKeyId + ".p8");
                Reader isReader = new InputStreamReader(is);
                PEMParser pemParser = new PEMParser(isReader)
        ) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            return converter.getPrivateKey(object);
        } catch (NullPointerException e) {
            throw new IOException("Key file 'apple/AuthKey_" + appleKeyId + ".p8' not found");
        }
    }

}
