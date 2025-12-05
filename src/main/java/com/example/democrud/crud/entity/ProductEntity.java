package com.example.democrud.crud.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "t_product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Double price;

    @Column
    private String description;

    @Column(name = "real_price")
    private Double realPrice;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;
}

