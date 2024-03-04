package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.model.Product;
import com.jchojdak.restaurant.model.dto.ProductInfoDto;
import com.jchojdak.restaurant.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new NotFoundException("Product id " + id +" does not exist");
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

    @Override
    public List<ProductInfoDto> getProductsByName(String searchByName) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(searchByName);
        return products.stream()
                .map(product -> mapToProductInfoDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public Product editProductDetails(Long id, Map<String, Object> updates) {
        Product product = getProductById(id);
        if (product != null) {

            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        if (value != null && !value.toString().isEmpty()) {
                            product.setName(value.toString());
                        }
                        break;
                    case "cookTime":
                        if (value != null && !value.toString().isEmpty()) {
                            product.setCookTime(value.toString());
                        }
                        break;
                    case "price":
                        if (value != null && !value.toString().isEmpty()) {
                            product.setPrice(new BigDecimal(value.toString()));
                        }
                        break;
                    case "favorite":
                        if (value != null && !value.toString().isEmpty()) {
                            product.setFavorite(Boolean.parseBoolean(value.toString()));
                        }
                        break;
                    case "imageUrl":
                        if (value != null && !value.toString().isEmpty()) {
                            product.setImageUrl(value.toString());
                        }
                        break;
                    case "ingredients":
                        if (value != null && !value.toString().isEmpty()) {
                            product.setIngredients(value.toString());
                        }
                        break;
                    case "description":
                        if (value != null && !value.toString().isEmpty()) {
                            product.setDescription(value.toString());
                        }
                        break;
                    default:
                        break;
                }
            });

            return productRepository.save(product);
        } else {
            return null;
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
