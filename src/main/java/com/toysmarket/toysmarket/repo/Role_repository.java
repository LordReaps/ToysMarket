package com.toysmarket.toysmarket.repo;

import com.toysmarket.toysmarket.models.Role;
import org.springframework.data.repository.CrudRepository;


public interface Role_repository extends CrudRepository<Role, Integer> {
    Role findByName(String role_name);
}
