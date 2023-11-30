package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.model.Category;
import com.jchojdak.restaurant.model.Product;
import com.jchojdak.restaurant.model.dto.ProductDto;
import com.jchojdak.restaurant.model.dto.ProductInfoDto;
import com.jchojdak.restaurant.service.ICategoryService;
import com.jchojdak.restaurant.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final ModelMapper modelMapper;
    private final ICategoryService categoryService;

    @GetMapping("/all")
    @Operation(summary = "Get all products")
    public List<ProductInfoDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/favorite")
    @Operation(summary = "Get all favorite products")
    public List<ProductInfoDto> getAllFavoriteProducts() {
        return productService.getAllFavoriteProducts();
    }

    @GetMapping("/details/{id}")
    @Operation(summary = "Get product details")
    public ProductInfoDto getProductDetails(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Create new product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> createProduct(@RequestBody ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);

        Category category = categoryService.findByName(productDto.getCategory());
        product.setCategory(category);

        productService.saveProduct(product);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
