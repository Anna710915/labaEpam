package com.epam.esm.repository;

import com.epam.esm.model.entity.User;

public interface UserRepository {
    User findByUsername(String username);
}
