package com.example.democrud.crud.service;

import com.example.democrud.crud.dto.response.CategoryResponseDto;
import java.util.List;

public interface CategoryService {

    void createCategory(String name);

    void deleteCategory(Long categoryId);

    void updateCategory(Long categoryId, String name);

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(Long categoryId);
}
