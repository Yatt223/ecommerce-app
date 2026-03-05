package com.mohamedbakaryyattoura.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandeRequest {
    // La commande est creer depuis le panier
    // on peut ajouter une adresse de son livraison ici plus tard
    private String adresseLivraison;
}
