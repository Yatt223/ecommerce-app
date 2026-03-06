package com.mohamedbakaryyattoura.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();

    private LocalDateTime datePaiement;
    private LocalDateTime dateExpedition;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "commande",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<CommandeItem> items = new ArrayList<>();

    // Getters/Setters manuels
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }

    public LocalDateTime getDateExpedition() { return dateExpedition; }
    public void setDateExpedition(LocalDateTime dateExpedition) {
        this.dateExpedition = dateExpedition;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<CommandeItem> getItems() { return items; }
    public void setItems(List<CommandeItem> items) { this.items = items; }

    public enum StatutCommande {
        EN_ATTENTE,
        PAYEE,
        EXPEDIEE,
        LIVREE,
        ANNULEE
    }
}