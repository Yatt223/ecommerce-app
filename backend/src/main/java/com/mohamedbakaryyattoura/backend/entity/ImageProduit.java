package com.mohamedbakaryyattoura.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "images_produit")
public class ImageProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomFichier;

    @Column(nullable = false)
    private String url;

    private boolean principale = false;

    // Plusieurs images pour un produit
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
}
