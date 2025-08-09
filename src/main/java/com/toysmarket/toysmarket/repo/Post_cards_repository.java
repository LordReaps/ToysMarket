package com.toysmarket.toysmarket.repo;

import com.toysmarket.toysmarket.models.Post_cards;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface Post_cards_repository extends CrudRepository<Post_cards, Integer> {


    //Я не знаю зачем, но приходится переопределять findAll, чтобы модели работали
    @Override
    @EntityGraph(attributePaths = {"images"})
    List<Post_cards> findAll();

    @EntityGraph(attributePaths = {"images"})
    Optional<Post_cards> findById(Integer id);
}
