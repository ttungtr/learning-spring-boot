package com.example.democrud.crud.repository;

import com.example.democrud.crud.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<ProductEntity, Long> {

}
