package com.example.democrud.crud.repository;

import com.example.democrud.crud.entity.CategoryProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryProductRepository extends JpaRepository<CategoryProductEntity, Long> {

    CategoryProductEntity findByCategoryIdAndProductId(Long categoryId, Long productId);

    void deleteByCategoryIdAndProductId(Long categoryId, Long productId);

    List<CategoryProductEntity> findAllByProductId(Long productId);

    List<CategoryProductEntity> findAllByCategoryId(Long categoryId);
}
