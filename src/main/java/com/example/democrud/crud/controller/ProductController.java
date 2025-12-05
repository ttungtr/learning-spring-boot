package com.example.democrud.crud.controller;


import com.example.democrud.crud.dto.request.ProductRequestDto;
import com.example.democrud.crud.entity.CategoryEntity;
import com.example.democrud.crud.repository.CategoryRepository;
import com.example.democrud.crud.repository.ProductRepository;
import com.example.democrud.crud.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/public/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("")
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequestDto product) {
        log.info("Create product request");
        productService.createProduct(product);
        return ResponseEntity.noContent().build();
    }
}
