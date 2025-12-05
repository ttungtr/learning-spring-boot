package com.example.democrud.crud.service.impl;


import com.example.democrud.crud.dto.request.ProductRequestDto;
import com.example.democrud.crud.dto.response.ProductResponseDto;
import com.example.democrud.crud.entity.CategoryEntity;
import com.example.democrud.crud.entity.CategoryProductEntity;
import com.example.democrud.crud.entity.ProductEntity;
import com.example.democrud.crud.repository.CategoryProductRepository;
import com.example.democrud.crud.repository.CategoryRepository;
import com.example.democrud.crud.repository.ChatMessageRepository;
import com.example.democrud.crud.repository.ProductRepository;
import com.example.democrud.crud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository ;

    @Override
    @Transactional
    public void createProduct(ProductRequestDto request) {



        ProductEntity product = ProductEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .realPrice(request.getRealPrice())
                .build();


        Set<Long> categoryIds = request.getCategoryIds();

        if (!categoryIds.isEmpty()) {
           for (Long categoryId : categoryIds) {
               CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
               if (categoryEntity == null || categoryEntity.getIsDeleted()) {
                   throw new RuntimeException("Category with id " + categoryId + " not found or is deleted");
               }
               categoryProductRepository.save(CategoryProductEntity.builder().category(categoryEntity).product(product).build());

           }
        }

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, String name, String description, Double price, Double realPrice, Set<Long> categoryIds) {

        ProductEntity product = productRepository.findById(productId).orElse(null);


        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setRealPrice(realPrice);

        if (!categoryIds.isEmpty()) {

            Set<Long> currentCategoryIds = categoryProductRepository.findAllByProductId(productId).stream()
                    .map(cp -> cp.getCategory().getId())
                    .collect(Collectors.toSet());

            Set<Long> categoriesToRemove = currentCategoryIds.stream()
                    .filter(id -> !categoryIds.contains(id)).collect(Collectors.toSet());


            Set<Long> categoriesToAdd = categoryIds.stream()
                    .filter(id -> !currentCategoryIds.contains(id)).collect(Collectors.toSet());

            for (Long categoryId : categoriesToRemove) {
                categoryProductRepository.deleteByCategoryIdAndProductId(categoryId, productId);
            }

            for (Long categoryId : categoriesToAdd) {
                CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category with id " + categoryId + " not found"));

                if (categoryEntity.getIsDeleted()) {
                    throw new RuntimeException("Category with id " + categoryId + " is deleted");
                }

                CategoryProductEntity categoryProduct = CategoryProductEntity.builder()
                        .category(categoryEntity)
                        .product(product)
                        .build();

                categoryProductRepository.save(categoryProduct);
            }
        }

        productRepository.save(product);


    }

    @Override
    public void deleteProduct(Long id) {

        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        product.setIsDeleted(true);
        productRepository.save(product);

    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(product -> !product.getIsDeleted())
                .map(product -> ProductResponseDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .realPrice(product.getRealPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        return productRepository.findById(id)
                .filter(product -> !product.getIsDeleted())
                .map(product -> ProductResponseDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .realPrice(product.getRealPrice())
                        .build())
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
