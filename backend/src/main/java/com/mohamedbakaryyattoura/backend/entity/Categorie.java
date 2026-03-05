package com.mohamedbakaryyattoura.backend.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "categories")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String description;

    // une catégorie peut avoir plusieurs produits
    @OneToMany(mappedBy = "categorie")
    private List<Produit> produits;

}
