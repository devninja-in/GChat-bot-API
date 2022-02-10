package com.ggs.bot.repository.impl;

import com.ggs.bot.domain.User;
import com.ggs.bot.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    @Override
    @ActivateRequestContext
    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

}