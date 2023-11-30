package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Product;
import com.jchojdak.restaurant.model.dto.ProductInfoDto;

import java.util.List;

public interface IProductService {
    List<ProductInfoDto> getAllProducts();

    List<ProductInfoDto> getAllFavoriteProducts();

    ProductInfoDto findProductById(Long id);

    void saveProduct(Product product);

    void deleteProduct(Long id);
}
