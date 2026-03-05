package com.mohamedbakaryyattoura.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorieRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;
}
