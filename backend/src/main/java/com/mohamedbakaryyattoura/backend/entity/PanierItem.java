package com.mohamedbakaryyattoura.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "panier_items")
public class PanierItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantite;

    // Plusieurs items dans un panier
    @ManyToOne
    @JoinColumn(name = "panier_id")
    private Panier panier;

    // un item correspond à un produit
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
}
