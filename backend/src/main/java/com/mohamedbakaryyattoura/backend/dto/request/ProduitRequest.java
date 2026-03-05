package com.mohamedbakaryyattoura.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProduitRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Min(value =0, message = "Le prix doit être positif")
    private BigDecimal prix;

    @NotNull(message = "Le stock est obligatoire")
    @Min(value = 0, message = "Le stock doit être positif ")
    private Integer stock;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categorieId;
}
