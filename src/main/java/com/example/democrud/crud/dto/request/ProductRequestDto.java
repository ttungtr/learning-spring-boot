package com.example.democrud.crud.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {

    @NotBlank(message = "Name is not blank")
    private String name;

    @NotBlank(message = "Price is not blank")
    private Double price;


    private String description;

    @NotBlank(message = "Real price is not blank")
    private Double realPrice;

    @NotNull(message = "Real price is not null")
    private Set<Long> categoryIds;
}
