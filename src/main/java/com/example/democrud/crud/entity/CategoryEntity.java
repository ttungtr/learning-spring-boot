package com.example.democrud.crud.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "t_category")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;
}
