package com.mohamedbakaryyattoura.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "paniers")
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateMiseAJour = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "panier",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<PanierItem> items = new ArrayList<>();

    // Getters/Setters manuels pour éviter les problèmes Lombok
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<PanierItem> getItems() { return items; }
    public void setItems(List<PanierItem> items) { this.items = items; }
}