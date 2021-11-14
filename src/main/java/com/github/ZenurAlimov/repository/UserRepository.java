package com.github.ZenurAlimov.repository;

import com.github.ZenurAlimov.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {
    @Cacheable("users")
    Optional<User> getByEmail(String email);
}