package com.toysmarket.toysmarket.repo;

import com.toysmarket.toysmarket.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface Role_repository extends CrudRepository<Role, Integer> {
    Role findByName(String role_name);
}
