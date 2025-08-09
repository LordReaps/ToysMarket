package com.toysmarket.toysmarket.repo;

import com.toysmarket.toysmarket.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface User_repository extends CrudRepository<User, Integer> {
    User findUserById(Integer id);

    List<User> findAll();

    User findUserByUsername(String username);
}
