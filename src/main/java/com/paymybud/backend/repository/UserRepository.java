package com.paymybud.backend.repository;

import com.paymybud.backend.controller.AuthController;
import com.paymybud.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.id != :id")
    List<User> getAllUserExceptCurrentUser(Integer id);

}
