package com.ggs.bot.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Optional;

/**
 * interface to provide basic auth
 */
public interface BasicAuthProvider extends AuthProvider {

    Logger LOGGER = LoggerFactory.getLogger(BasicAuthProvider.class);

    @Override
    default Optional<String> getAuthenticationHeader() {

        Optional<String> username = getUsername();
        Optional<String> password = getPassword();
        if(username.isEmpty() || password.isEmpty()){
            LOGGER.info("Username or password is empty");
            return Optional.empty();
        }

        String notEncoded = username.get() + ":" + password.get();
        return Optional.of("Basic " + Base64.getEncoder().encodeToString(notEncoded.getBytes()));
    }

    /**
     *
     * @return username
     */
    Optional<String> getUsername();

    /**
     *
     * @return password
     */
    Optional<String> getPassword();

}
