package com.mohamedbakaryyattoura.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PanierItemRequest {
    @NotNull(message = "Le produit est obligatoire")
    private Long produitId;

    @NotNull(message = "La quantitté est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;
}
