package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Product;
import com.jchojdak.restaurant.model.dto.ProductInfoDto;

import java.util.List;
import java.util.Map;

public interface IProductService {
    List<ProductInfoDto> getAllProducts();

    List<ProductInfoDto> getAllFavoriteProducts();

    ProductInfoDto findProductById(Long id);

    Product getProductById(Long id);

    void saveProduct(Product product);

    void deleteProduct(Long id);

    List<ProductInfoDto> getProductsByName(String searchByName);

    Product editProductDetails(Long id, Map<String, Object> updates);
}
