package com.jchojdak.restaurant.controller;


import com.jchojdak.restaurant.model.Category;
import com.jchojdak.restaurant.model.dto.CategoryDto;
import com.jchojdak.restaurant.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping("/all")
    @Operation(summary = "Get all categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create new category", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);

        categoryService.saveCategory(category);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete category", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
        categoryService.deleteCategory(name);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
