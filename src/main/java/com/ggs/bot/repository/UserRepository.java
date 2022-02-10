package com.ggs.bot.repository;

import com.ggs.bot.domain.User;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public interface UserRepository extends PanacheRepositoryBase<User, String> {

    User findByEmail(String email);

}
