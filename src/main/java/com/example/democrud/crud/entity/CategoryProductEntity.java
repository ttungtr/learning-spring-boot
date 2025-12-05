package com.example.democrud.crud.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name= "t_category_product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;
}
