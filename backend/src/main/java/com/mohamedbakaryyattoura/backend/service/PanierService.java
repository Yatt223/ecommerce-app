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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PanierService {
    private  final PanierRepository panierRepository;
    private final PanierItemRepository panierItemRepository;
    private final ProduitRepository produitRepository;
    private final UserRepository userRepository;

    // Recuperer ou creer le panier d'un utilisateur
    public PanierResponse getPanier(Long userId){
        Panier panier = getOuCreerPanier(userId);
        return toResponse(panier);
    }

    // Ajouter un produit au panier
    public PanierResponse ajoutAuPanier(Long userId, PanierItemRequest request){
        Panier panier = getOuCreerPanier(userId);

        Produit produit = produitRepository.findById(request.getProduitId())
                .orElseThrow(()->
                        new RuntimeException("Produit introuvable !")
                );
        if(produit.getStock() < request.getQuantite()){
            throw new RuntimeException("stock insuffisant !");
        }

        // Vérifie si le produit est déjà dans le panier
        Optional<PanierItem> itemExistant = panierItemRepository
                .findByPanierIdAndProduitId(panier.getId(), produit.getId());
        if(itemExistant.isPresent()){
            // Met à jour la quantité
            PanierItem item = itemExistant.get();
            item.setQuantite(item.getQuantite() + request.getQuantite());
            panierItemRepository.save(item);
        }else{
            // Creer un nouvel item
            PanierItem item = new PanierItem();
            item.setPanier(panier);
            item.setProduit(produit);
            item.setQuantite(request.getQuantite());
            panierItemRepository.save(item);
        }

        panier.setDateMiseAjour(LocalDateTime.now());
        panierRepository.save(panier);

        return getPanier(userId);
    }

    // Met à jour la quantité d'un item
    public  PanierResponse mettreAJourQuantite(Long userId, Long itemId, Integer quantite){
        PanierItem item = panierItemRepository.findById(itemId)
                .orElseThrow(()->
                        new RuntimeException("Item introuvable !")
                );
        if(quantite <= 0){
            panierItemRepository.delete(item);
        }else {
            item.setQuantite(quantite);
            panierItemRepository.save(item);
        }

        return getPanier(userId);
    }

    // Supprime un item du panier
    public  PanierResponse supprimerDuPanier(Long userId, Long itemId){
        PanierItem item = panierItemRepository.findById(itemId)
                .orElseThrow(()->
                        new RuntimeException("Item introuvable !")
                );
        panierItemRepository.delete(item);
        return getPanier(userId);
    }

    // vide tout le panier
    public void videPanier(Long userId){
        Panier panier = getOuCreerPanier(userId);
        panier.getItems().clear();
        panierRepository.save(panier);
    }

    // Recuperer ou creer un panier pour l'utilisateur
    public Panier getOuCreerPanier(Long userId) {
        return panierRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User introuvable !"));
                    Panier nouveauPanier = new Panier();
                    nouveauPanier.setUser(user);
                    return panierRepository.save(nouveauPanier);
                });

    }
    // Convertit entité à DTO Response
    public PanierResponse toResponse(Panier panier){
        PanierResponse response = new PanierResponse();
        response.setId(panier.getId());
        response.setDateMiseAJour(panier.getDateMiseAjour());

        List<PanierItemResponse> items = panier.getItems()
                .stream()
                .map(this::itemToResponse)
                .collect(Collectors.toList());

        response.setItems(items);
        // Calcule le total
        BigDecimal total = items.stream()
                .map(PanierItemResponse::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotal(total);

        // Nombre total d'articles
        int nombreArticles = panier.getItems()
                .stream()
                .mapToInt(PanierItem::getQuantite)
                .sum();
        response.setNombreArticles(nombreArticles);
        return response;
    }

    private PanierItemResponse itemToResponse(PanierItem item){
        PanierItemResponse response = new PanierItemResponse();
        response.setId(item.getId());
        response.setQuantite(item.getQuantite());
        response.setProduitId(item.getProduit().getId());
        response.setProduitNom(item.getProduit().getNom());
        response.setProduitPrix(item.getProduit().getPrix());
        // Image principale du produit
        if(item.getProduit().getImages() != null &&
        !item.getProduit().getImages().isEmpty()){
            response.setProduitImage(
                    item.getProduit().getImages().get(0).getUrl()
            );
        }
        // Sous total = prix * quantité
        BigDecimal sousTotal = item.getProduit().getPrix()
                .multiply(BigDecimal.valueOf(item.getQuantite()));
        response.setSousTotal(sousTotal);
        return response;
    }

}
