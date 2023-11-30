package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Category;
import com.jchojdak.restaurant.model.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {

    Category findByName(String category);

    List<CategoryDto> getAllCategories();

    void saveCategory(Category category);

    void deleteCategory(String name);
}
