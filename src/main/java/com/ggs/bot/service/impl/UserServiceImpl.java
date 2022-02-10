package com.ggs.bot.service.impl;

import com.ggs.bot.domain.User;
import com.ggs.bot.repository.UserRepository;
import com.ggs.bot.service.UserService;

import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject private UserRepository userRepository;

    @Override
    public User getUserWithDefaultValues(String displayName, String email) {
        User user = new User().setDisplayName(displayName).setEmail(email);
        user.setCreatedBy("SYSTEM USER").setCreatedAt(new Date()).setUpdatedBy("SYSTEM USER")
            .setUpdatedAt(new Date()).setId(UUID.randomUUID().toString());
        return user;
    }

    @Override
    @Transactional
    public User createUserWithDefaultValues(String displayName, String email) {
        User user = getUserWithDefaultValues(displayName, email);
        userRepository.persistAndFlush(user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        userRepository.getEntityManager().merge(user);
        return user;
    }
}
