package com.example.democrud.crud.service.impl;

import com.example.democrud.crud.dto.response.CategoryResponseDto;
import com.example.democrud.crud.entity.CategoryEntity;
import com.example.democrud.crud.repository.CategoryProductRepository;
import com.example.democrud.crud.repository.CategoryRepository;
import com.example.democrud.crud.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl  implements CategoryService {

    private final  CategoryRepository categoryRepository;
    private final CategoryProductRepository categoryProductRepository;

    @Override
    public void createCategory(String name) {
        categoryRepository.save(CategoryEntity.builder().name(name).build());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long categoryId, String name) {

        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(name);
        categoryRepository.save(category);

    }

    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getIsDeleted()) {
            throw new RuntimeException("Category is deleted");
        }

        List<Long> productIds = categoryProductRepository.findAllByCategoryId(categoryId).stream()
                .map(cp -> cp.getProduct().getId())
                .collect(Collectors.toList());



        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .productIds(productIds)
                .build();
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(category -> !category.getIsDeleted())
                .map(category -> CategoryResponseDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .productIds(categoryProductRepository.findAllByCategoryId(category.getId()).stream()
                                .map(cp -> cp.getProduct().getId())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
