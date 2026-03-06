package com.mohamedbakaryyattoura.backend.service;

import com.mohamedbakaryyattoura.backend.dto.request.PanierItemRequest;
import com.mohamedbakaryyattoura.backend.dto.response.PanierItemResponse;
import com.mohamedbakaryyattoura.backend.dto.response.PanierResponse;
import com.mohamedbakaryyattoura.backend.entity.Panier;
import com.mohamedbakaryyattoura.backend.entity.PanierItem;
import com.mohamedbakaryyattoura.backend.entity.Produit;
import com.mohamedbakaryyattoura.backend.entity.User;
import com.mohamedbakaryyattoura.backend.repository.PanierItemRepository;
import com.mohamedbakaryyattoura.backend.repository.PanierRepository;
import com.mohamedbakaryyattoura.backend.repository.ProduitRepository;
import com.mohamedbakaryyattoura.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PanierService {

    private final PanierRepository panierRepository;
    private final PanierItemRepository panierItemRepository;
    private final ProduitRepository produitRepository;
    private final UserRepository userRepository;

    // Récupère le panier d'un utilisateur
    @Transactional
    public PanierResponse getPanier(Long userId) {
        Panier panier = getOuCreerPanier(userId);
        // Force le rechargement depuis la base
        panier = panierRepository.findById(panier.getId()).orElseThrow();
        return toResponse(panier);
    }

    // Ajoute un produit au panier
    @Transactional
    public PanierResponse ajouterAuPanier(
            Long userId, PanierItemRequest request) {

        Panier panier = getOuCreerPanier(userId);

        Produit produit = produitRepository
                .findById(request.getProduitId())
                .orElseThrow(() ->
                        new RuntimeException("Produit introuvable !")
                );

        if (produit.getStock() < request.getQuantite()) {
            throw new RuntimeException("Stock insuffisant !");
        }

        // Vérifie si le produit est déjà dans le panier
        Optional<PanierItem> itemExistant = panierItemRepository
                .findByPanierIdAndProduitId(
                        panier.getId(),
                        produit.getId()
                );

        if (itemExistant.isPresent()) {
            // Met à jour la quantité existante
            PanierItem item = itemExistant.get();
            item.setQuantite(item.getQuantite() + request.getQuantite());
            panierItemRepository.save(item);
        } else {
            // Crée un nouvel item
            PanierItem item = new PanierItem();
            item.setPanier(panier);
            item.setProduit(produit);
            item.setQuantite(request.getQuantite());
            panierItemRepository.save(item);
        }

        // Met à jour la date du panier
        panier.setDateMiseAJour(LocalDateTime.now());
        panierRepository.save(panier);

        // Force le rechargement complet depuis la base
        Panier panierMisAJour = panierRepository
                .findById(panier.getId())
                .orElseThrow();

        return toResponse(panierMisAJour);
    }

    // Met à jour la quantité d'un item
    @Transactional
    public PanierResponse mettreAJourQuantite(
            Long userId, Long itemId, Integer quantite) {

        PanierItem item = panierItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException("Item introuvable !")
                );

        if (quantite <= 0) {
            panierItemRepository.delete(item);
        } else {
            item.setQuantite(quantite);
            panierItemRepository.save(item);
        }

        // Recharge le panier depuis la base
        Panier panier = panierRepository
                .findByUserId(userId)
                .orElseThrow();

        return toResponse(panier);
    }

    // Supprime un item du panier
    @Transactional
    public PanierResponse supprimerDuPanier(Long userId, Long itemId) {
        PanierItem item = panierItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException("Item introuvable !")
                );

        panierItemRepository.delete(item);
        panierItemRepository.flush(); // Force la suppression en base

        Panier panier = panierRepository
                .findByUserId(userId)
                .orElseThrow();

        return toResponse(panier);
    }

    // Vide tout le panier
    @Transactional
    public void viderPanier(Long userId) {
        Panier panier = getOuCreerPanier(userId);
        panierItemRepository.deleteAll(panier.getItems());
        panierItemRepository.flush();
        panier.getItems().clear();
        panierRepository.save(panier);
    }

    // Récupère ou crée un panier pour l'utilisateur
    @Transactional
    public Panier getOuCreerPanier(Long userId) {
        return panierRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new RuntimeException("User introuvable !")
                            );
                    Panier nouveauPanier = new Panier();
                    nouveauPanier.setUser(user);
                    return panierRepository.save(nouveauPanier);
                });
    }

    // Convertit entité → DTO Response
    public PanierResponse toResponse(Panier panier) {
        PanierResponse response = new PanierResponse();
        response.setId(panier.getId());
        response.setDateMiseAJour(panier.getDateMiseAJour());

        // Recharge les items depuis la base
        List<PanierItem> items = panierItemRepository
                .findAll()
                .stream()
                .filter(item -> item.getPanier().getId()
                        .equals(panier.getId()))
                .toList();

        List<PanierItemResponse> itemResponses = new ArrayList<>();
        for (PanierItem item : items) {
            itemResponses.add(itemToResponse(item));
        }

        response.setItems(itemResponses);

        // Calcule le total
        BigDecimal total = BigDecimal.ZERO;
        for (PanierItemResponse item : itemResponses) {
            total = total.add(item.getSousTotal());
        }
        response.setTotal(total);

        // Nombre total d'articles
        int nombreArticles = 0;
        for (PanierItem item : items) {
            nombreArticles += item.getQuantite();
        }
        response.setNombreArticles(nombreArticles);

        return response;
    }

    private PanierItemResponse itemToResponse(PanierItem item) {
        PanierItemResponse response = new PanierItemResponse();
        response.setId(item.getId());
        response.setQuantite(item.getQuantite());
        response.setProduitId(item.getProduit().getId());
        response.setProduitNom(item.getProduit().getNom());
        response.setProduitPrix(item.getProduit().getPrix());

        // Image principale
        if (item.getProduit().getImages() != null &&
                !item.getProduit().getImages().isEmpty()) {
            response.setProduitImage(
                    item.getProduit().getImages().get(0).getUrl()
            );
        }

        // Sous-total
        BigDecimal sousTotal = item.getProduit().getPrix()
                .multiply(BigDecimal.valueOf(item.getQuantite()));
        response.setSousTotal(sousTotal);

        return response;
    }
}