package com.ggs.bot.service;

import com.ggs.bot.domain.User;

public interface UserService {

    User getUserWithDefaultValues(String displayName, String email);

    User createUserWithDefaultValues(String displayName, String email);

    User getUserByEmail(String email);

    User updateUser(User user);
}
