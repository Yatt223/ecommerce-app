package com.mohamedbakaryyattoura.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "commandes")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero; // ex: CMD-2024-40315-001

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommande staut = StatutCommande.EN_ATTENTE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();

    private LocalDateTime datePaiement;
    private LocalDateTime dateExpedition;

    // Une commande appartient a un user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Une commande a plusieurs lignes
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<CommandeItem> items = new ArrayList<>();

    public enum StatutCommande{
        EN_ATTENTE,
        PAYEE,
        EXPEDIEE,
        LIVREE,
        ANNULEE
    }

    
}
