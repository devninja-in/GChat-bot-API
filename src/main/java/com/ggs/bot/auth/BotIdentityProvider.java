package com.ggs.bot.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.ws.rs.ForbiddenException;

@ApplicationScoped
public class BotIdentityProvider implements IdentityProvider<TokenAuthenticationRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotIdentityProvider.class);

    // Bearer Tokens received by bots will always specify this issuer.
    private static final String CHAT_ISSUER = "chat@system.gserviceaccount.com";
    // Url to obtain the public certificate for the issuer.
    private static final String PUBLIC_CERT_URL_PREFIX = "https://www.googleapis.com/service_accounts/v1/metadata/x509/";
    // Intended audience of the token, which will be the project number of the bot.
    @ConfigProperty(name = "bot.project.number") private Instance<String> botProjectNumber;

    private final JsonFactory factory = new JacksonFactory();
    private GoogleIdTokenVerifier verifier = null;

    public BotIdentityProvider(
        @ConfigProperty(name = "bot.allowed.service.account", defaultValue = "") List<String> allowedServiceAccounts)
        throws Exception {

        LOGGER.info("Adding GoogleIdTokenVerifier for {}", CHAT_ISSUER);
        verifier = buildGoogleIdTokenVerifier(CHAT_ISSUER);
    }

    @Override
    public Class<TokenAuthenticationRequest> getRequestType() {
        return TokenAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(TokenAuthenticationRequest request,
        AuthenticationRequestContext context) {

        try {
            GoogleIdToken idToken = GoogleIdToken.parse(factory, request.getToken().getToken());
            if (idToken == null) {
                LOGGER.error("GoogleIdToken is null");
                throw new ForbiddenException("You are not authorized to access this endpoint");
            }

            verifyIdToken(idToken, verifier);
            LOGGER.info("shadowbot request authenticated successfully");
            return buildSecurityIdentity(StringUtils.isNotBlank(idToken.getPayload().getEmail()) ?
                idToken.getPayload().getEmail() :
                idToken.getPayload().getIssuer());
        }
        catch (AuthenticationFailedException exception) {
            throw exception;
        }
        catch (Exception exception) {
            LOGGER.warn("Authentication failed", exception);
            throw new AuthenticationFailedException("You are not authorized to access this endpoint");
        }

    }

    private boolean isIssuerMatch(String email, String issuer) {
        List<String> parts = Arrays.asList(email.split("@"));
        return issuer.endsWith("@" + parts.get(parts.size() - 1));
    }

    private Uni<SecurityIdentity> buildSecurityIdentity(String email) {
        QuarkusSecurityIdentity.Builder builder = new QuarkusSecurityIdentity.Builder();
        builder.setPrincipal(new QuarkusPrincipal(email));
        QuarkusSecurityIdentity quarkusSecurityIdentity = builder.build();
        Uni<SecurityIdentity> securityIdentity = Uni.createFrom().item(quarkusSecurityIdentity);
        return securityIdentity;
    }

    private void verifyIdToken(GoogleIdToken idToken, GoogleIdTokenVerifier verifier) throws Exception {
        if (!verifier.verify(idToken) ||
            !idToken.verifyAudience(Collections.singletonList(botProjectNumber.get())) ||
            !(System.currentTimeMillis() < idToken.getPayload().getExpirationTimeSeconds() * 1000)) {
            LOGGER.error("Invalid token {}", idToken);
            throw new AuthenticationFailedException("Invalid token");
        }
    }

    private GoogleIdTokenVerifier buildGoogleIdTokenVerifier(String issuer) {
        GooglePublicKeysManager.Builder keyManagerBuilder = new GooglePublicKeysManager.Builder(
            new ApacheHttpTransport(), factory);

        String certUrl = PUBLIC_CERT_URL_PREFIX + issuer;
        keyManagerBuilder.setPublicCertsEncodedUrl(certUrl);

        GoogleIdTokenVerifier.Builder verifierBuilder = new GoogleIdTokenVerifier.Builder(
            keyManagerBuilder.build());
        verifierBuilder.setIssuer(issuer);
        return verifierBuilder.build();
    }

}
