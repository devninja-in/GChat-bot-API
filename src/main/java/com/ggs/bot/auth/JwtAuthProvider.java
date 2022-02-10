package com.ggs.bot.auth;

import java.util.Optional;

/**
 * interface to provide JWT token
 */
public interface JwtAuthProvider extends AuthProvider {

    @Override
    default Optional<String> getAuthenticationHeader() {
        Optional<String>  token = getToken();
        if(token.isPresent()){
            return Optional.of("Bearer "+getToken().get());
        }
        return Optional.empty();
    }

    /**
     *
     * @return JWT token
     */
    public Optional<String> getToken();
}
