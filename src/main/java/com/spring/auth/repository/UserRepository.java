package com.spring.auth.repository;

import com.spring.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNo(String phoneNo);

    boolean existsByEmail(String email);

    User findByEmail(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNoIgnoreCase(String phoneNo);
}
