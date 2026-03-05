package com.mohamedbakaryyattoura.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CommandeItemResponse {
    private Long id;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;
    private Long produitId;
    private String produitNom;
}
