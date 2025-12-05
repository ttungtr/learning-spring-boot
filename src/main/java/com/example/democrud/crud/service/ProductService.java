package com.example.democrud.crud.service;

import com.example.democrud.crud.dto.request.ProductRequestDto;
import com.example.democrud.crud.dto.response.ProductResponseDto;

import java.util.List;
import java.util.Set;

public interface ProductService {

    void createProduct(ProductRequestDto product);

    void updateProduct(Long productId ,String name, String description, Double price, Double realPrice, Set<Long> categoryIds);

    void deleteProduct(Long id);

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

}
