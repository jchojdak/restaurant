package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.exception.DuplicateException;
import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.model.Category;
import com.jchojdak.restaurant.model.dto.CategoryDto;
import com.jchojdak.restaurant.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Category findByName(String category) {
        Optional<Category> categoryByName = categoryRepository.findByName(category);

        if (categoryByName.isPresent()) {
            return categoryByName.get();
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void saveCategory(Category category) {
        String categoryName = category.getName();

        if (categoryRepository.existsByName(categoryName)) {
            throw new DuplicateException("Category already exists");
        }

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            categoryRepository.deleteByName(name);
        } else {
            throw new NotFoundException("Category does not exist");
        }
    }
}
