package com.spring.auth.repository;

import com.spring.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM \"user\" WHERE phone_no = ?1", nativeQuery = true)
    Optional<User> findByPhoneNo(String phoneNo);
}
