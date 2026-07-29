package com.webapp.onlineelectronicstore.repositories;

import com.webapp.onlineelectronicstore.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    //custom(finder) method:
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email,String password);

    //search User
    List<User> findByNameContaining(String keyword);


}
