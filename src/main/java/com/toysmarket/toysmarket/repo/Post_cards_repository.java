package com.toysmarket.toysmarket.repo;

import com.toysmarket.toysmarket.models.Post_cards;
import com.toysmarket.toysmarket.models.User_Post_Card;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Post_cards_repository extends CrudRepository<Post_cards, Integer> {


    //Я не знаю зачем, но приходится переопределять findAll, чтобы модели работали
    @Override
    @EntityGraph(attributePaths = {"images", "author"})
    List<Post_cards> findAll();

    @EntityGraph(attributePaths = {"images", "author"})
    Optional<Post_cards> findById(Integer id);

}
