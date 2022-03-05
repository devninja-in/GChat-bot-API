package com.ggs.bot.auth;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AnonymousAuthenticationRequest;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport.Type;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;

@Alternative
@Priority(1)
@ApplicationScoped
public class BotTokenAuthMechanism implements HttpAuthenticationMechanism {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotTokenAuthMechanism.class);

    private static final Logger LOG = LoggerFactory.getLogger(BotTokenAuthMechanism.class);
    @ConfigProperty(name = "bot.gchat.request.authentication.check", defaultValue = "FALSE") private Instance<String> botRequestAuthCheck;

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context,
        IdentityProviderManager identityProviderManager) {

        if (!"TRUE".equalsIgnoreCase(botRequestAuthCheck.get())) {
            LOGGER.info("Skipping authorization as botRequestAuthCheck is {}", botRequestAuthCheck);
            QuarkusSecurityIdentity.Builder builder = new QuarkusSecurityIdentity.Builder();
            builder.setPrincipal(new QuarkusPrincipal(BotIdentityProvider.CHAT_ISSUER));
            QuarkusSecurityIdentity quarkusSecurityIdentity = builder.build();
            Uni<SecurityIdentity> securityIdentity = Uni.createFrom().item(quarkusSecurityIdentity);
            return securityIdentity;
        }

        List<String> authHeaders = context.request().headers().getAll(HttpHeaderNames.AUTHORIZATION);
        if (authHeaders != null && !authHeaders.isEmpty()) {
            for (String current : authHeaders) {
                if (current.toLowerCase(Locale.ENGLISH).startsWith("bearer")) {
                    String[] authHeader = current.split(" ");
                    return identityProviderManager.authenticate(
                        new TokenAuthenticationRequest(new TokenCredential(authHeader[1], "Bearer")));
                }
            }
        }
        return Uni.createFrom().optional(Optional.empty());
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        ChallengeData result = new ChallengeData(HttpResponseStatus.UNAUTHORIZED.code(),
            HttpHeaderNames.WWW_AUTHENTICATE, "Bearer");
        return Uni.createFrom().item(result);
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return new HashSet<>(
            Arrays.asList(TokenAuthenticationRequest.class, AnonymousAuthenticationRequest.class));
    }

    @Override
    public HttpCredentialTransport getCredentialTransport() {
        return new HttpCredentialTransport(Type.AUTHORIZATION, "Bearer");
    }

}