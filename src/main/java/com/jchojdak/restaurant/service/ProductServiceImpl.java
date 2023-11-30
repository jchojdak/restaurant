package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.model.Product;
import com.jchojdak.restaurant.model.dto.ProductInfoDto;
import com.jchojdak.restaurant.repository.ProductRepository;
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
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductInfoDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> mapToProductInfoDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductInfoDto> getAllFavoriteProducts() {
        List<Product> products = productRepository.findByFavoriteTrue();

        return products.stream()
                .map(product -> mapToProductInfoDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public ProductInfoDto findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return mapToProductInfoDto(product.get());
        } else {
            throw new NotFoundException("Product does not exist");
        }
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new NotFoundException("Product does not exist");
        }
    }

    private ProductInfoDto mapToProductInfoDto(Product product) {
        ProductInfoDto productInfoDto = modelMapper.map(product, ProductInfoDto.class);

        if (product.getCategory() != null) {
            productInfoDto.setCategory(product.getCategory().getName());
        }

        return productInfoDto;
    }

}
