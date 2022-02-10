package com.ggs.bot.auth;

import java.util.Optional;

/**
 * interface to provide Authorization header value for Http API call
 */
public interface AuthProvider {

    /**
     * @return value for Authorization header
     */
    public Optional<String> getAuthenticationHeader();

}
