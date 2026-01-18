package com.library.system.repository;

import com.library.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides basic CRUD operations and custom user queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);


    Optional<User> findByMobile(String mobile);


    boolean existsByMobile(String mobile);

 
    boolean existsByRole(String role);
}
