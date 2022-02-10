package com.ggs.bot.filter;

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

import javax.enterprise.inject.Instance;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Provider
public class GChatBotRequestAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GChatBotRequestAuthenticationFilter.class);

    // Bearer Tokens received by bots will always specify this issuer.
    private static final String CHAT_ISSUER = "chat@system.gserviceaccount.com";

    // Url to obtain the public certificate for the issuer.
    private static final String PUBLIC_CERT_URL_PREFIX = "https://www.googleapis.com/service_accounts/v1/metadata/x509/";

    // Intended audience of the token, which will be the project number of the bot.
    @ConfigProperty(name = "bot.project.number") private Instance<String> audience;

    @ConfigProperty(name = "bot.gchat.request.authentication.check", defaultValue = "TRUE") private Instance<String> botRequestAuthCheck;

    private final GoogleIdTokenVerifier verifier;

    private final JsonFactory factory = new JacksonFactory();

    public GChatBotRequestAuthenticationFilter() {
        GooglePublicKeysManager.Builder keyManagerBuilder = new GooglePublicKeysManager.Builder(
            new ApacheHttpTransport(), factory);

        String certUrl = PUBLIC_CERT_URL_PREFIX + CHAT_ISSUER;
        keyManagerBuilder.setPublicCertsEncodedUrl(certUrl);

        GoogleIdTokenVerifier.Builder verifierBuilder = new GoogleIdTokenVerifier.Builder(
            keyManagerBuilder.build());
        verifierBuilder.setIssuer(CHAT_ISSUER);
        verifier = verifierBuilder.build();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorization = requestContext.getHeaderString("Authorization");
        //TODO: Remove this before prod push
        LOGGER.info("Authorization: {}", authorization);

        if (!"/gchat/event".equalsIgnoreCase(requestContext.getUriInfo().getPath())) {
            return;
        }

        if (!"TRUE".equalsIgnoreCase(botRequestAuthCheck.get())) {
            LOGGER.info("Skipping authorization as botRequestAuthCheck is {}", botRequestAuthCheck);
            return;
        }

        if (StringUtils.isBlank(authorization)) {
            LOGGER.error("Authorization header is missing");
            throw new ForbiddenException("You are not authorized to access this endpoint");
        }

        String token = authorization.split(" ")[1];
        GoogleIdToken idToken = GoogleIdToken.parse(factory, token);
        if (idToken == null) {
            LOGGER.error("GoogleIdToken is null");
            throw new ForbiddenException("You are not authorized to access this endpoint");
        }

        // Verify valid token, signed by CHAT_ISSUER.
        try {
            if (!verifier.verify(idToken) ||
                !idToken.verifyAudience(Collections.singletonList(audience.get())) ||
                !idToken.verifyIssuer(CHAT_ISSUER)) {
                LOGGER.error("Invalid token {}", token);
                throw new ForbiddenException("You are not authorized to access this endpoint");
            }
        }
        catch (GeneralSecurityException e) {
            LOGGER.error("Error occurred while validating token {}", token, e);
            throw new ForbiddenException("You are not authorized to access this endpoint");
        }
        LOGGER.info("bot request authenticated successfully");
    }
}
