package com.mohamedbakaryyattoura.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProduitResponse {
    private Long id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private Integer stock;
    private boolean actif;
    private LocalDateTime dateCreation;
    private CategorieResponse categorie;
    private List<String> images; // liste des URLS des images
}
