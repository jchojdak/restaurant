package com.jchojdak.restaurant.repository;

import com.jchojdak.restaurant.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String category);
    boolean existsByName(String name);

    void deleteByName(String name);
}
