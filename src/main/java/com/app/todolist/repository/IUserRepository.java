package com.app.todolist.repository;

import com.app.todolist.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByUsername(String username);

}
