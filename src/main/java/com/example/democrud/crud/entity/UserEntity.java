package com.example.democrud.crud.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name= "t_user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;

    @Column
    private String password;
}
