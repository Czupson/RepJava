package org.example.notification;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
}