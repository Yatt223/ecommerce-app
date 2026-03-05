package com.mohamedbakaryyattoura.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorieResponse {
    private Long id;
    private String nom;
    private String description;
    private int nombreProduits;
}
