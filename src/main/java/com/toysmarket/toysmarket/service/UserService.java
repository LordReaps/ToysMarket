package com.toysmarket.toysmarket.service;

import com.toysmarket.toysmarket.models.Role;

import com.toysmarket.toysmarket.models.User;
import com.toysmarket.toysmarket.repo.Role_repository;
import com.toysmarket.toysmarket.repo.User_repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    User_repository user_repository;
    @Autowired
    Role_repository role_repository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = user_repository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public User findUserById(Integer id) throws NotFoundException{
        Optional<User> userFromDb = Optional.ofNullable(user_repository.findUserById(id));
        return userFromDb.orElse(new User());
    }   public List<User> allUsers() {
        return user_repository.findAll();
    }

    public boolean saveUser(User user) {
        Role userRole = role_repository.findByName("ROLE_USER");
        if (userRole == null) {
            return false; // роль не найдена
        }
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user_repository.save(user);
        return true;
    }

    public boolean deleteUser(Integer id) {
        if (user_repository.findById(id).isPresent()) {
            user_repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<User> userList(Integer idMin){
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}
