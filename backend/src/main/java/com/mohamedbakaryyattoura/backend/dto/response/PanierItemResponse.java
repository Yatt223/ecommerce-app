package com.mohamedbakaryyattoura.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PanierItemResponse {
    private Long id;
    private Integer quantite;
    private Long produitId;
    private String produitNom;
    private BigDecimal produitPrix;
    private String produitImage;
    private BigDecimal sousTotal; // quantite * prix
}
