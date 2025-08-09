package com.toysmarket.toysmarket.repo;

import com.toysmarket.toysmarket.models.User_Post_Card;
import org.springframework.data.repository.CrudRepository;
import java.util.Set;

public interface User_Post_Card_repository extends CrudRepository<User_Post_Card, Integer>{

    Set<User_Post_Card> findAllByUserId(Integer userId);
    Set<User_Post_Card> findAllByCardId(Integer cardId);


}
