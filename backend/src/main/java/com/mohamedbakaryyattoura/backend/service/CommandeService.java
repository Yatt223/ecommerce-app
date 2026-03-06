package com.mohamedbakaryyattoura.backend.service;

import com.mohamedbakaryyattoura.backend.dto.response.CommandeItemResponse;
import com.mohamedbakaryyattoura.backend.dto.response.CommandeResponse;
import com.mohamedbakaryyattoura.backend.entity.*;
import com.mohamedbakaryyattoura.backend.repository.CommandeRepository;
import com.mohamedbakaryyattoura.backend.repository.ProduitRepository;
import com.mohamedbakaryyattoura.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final UserRepository userRepository;
    private final ProduitRepository produitRepository;
    private final PanierService panierService;

    // Creer une commande depuis le panier
    @Transactional
    public CommandeResponse creerCommande(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->
                        new RuntimeException("User introuvable !")
                );
        Panier panier = panierService.getOuCreerPanier(userId);
        if(panier.getItems().isEmpty()){
            throw new RuntimeException("Le panier est vide !");

        }
        // Creer la commande
        Commande commande = new Commande();
        commande.setUser(user);
        commande.setNumero(genererNumero());
        commande.setStatut(Commande.StatutCommande.EN_ATTENTE);

        // Creer les lignes de commande
        BigDecimal total = BigDecimal.ZERO;

        for (PanierItem panierItem : panier.getItems()){
            Produit produit = panierItem.getProduit();

            //verifie le stock
            if(produit.getStock() < panierItem.getQuantite()){
                throw new RuntimeException(
                        "Stock insuffisant pour : " + produit.getNom()
                );
            }
            CommandeItem commandeItem = new CommandeItem();
            commandeItem.setCommande(commande);
            commandeItem.setProduit(produit);
            commandeItem.setQuantite(panierItem.getQuantite());
            commandeItem.setPrixUnitaire(produit.getPrix());

            commande.getItems().add(commandeItem);

            // reduit le stock
            produit.setStock(produit.getStock() - panierItem.getQuantite());
            produitRepository.save(produit);

            // Calcule le total
            BigDecimal sousTotal = produit.getPrix()
                    .multiply(BigDecimal.valueOf(panierItem.getQuantite()));
            total = total.add(sousTotal);
        }

        commande.setTotal(total);
        commandeRepository.save(commande);
        // vide le panier après commande
        panierService.viderPanier(userId);
        return toResponse(commande);

    }
    // Historique des commande d'un utilisateur
    public List<CommandeResponse> getCommandesUser(Long userId){
        return commandeRepository.findByUserIdOrderByDateCommandeDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    // Détail d'une commande
    public CommandeResponse getCommandeParId(Long id){
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Commande introuvable : "+ id)
                );
        return toResponse(commande);
    }



    // change le statut d'une commande (admin)
    public  CommandeResponse changerStatut(Long id, Commande.StatutCommande statut){
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Commande introuvable : " + id)
                );
        commande.setStatut(statut);

        if(statut == Commande.StatutCommande.PAYEE){
            commande.setDatePaiement(LocalDateTime.now());
        } else if (statut == Commande.StatutCommande.EXPEDIEE) {
            commande.setDateCommande(LocalDateTime.now());
        }
        return  toResponse(commandeRepository.save(commande));
    }

    // Toutes les commandes (admin)
    public List<CommandeResponse> getToutesCommandes(){
        return commandeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    // Generer un numero de commande unique
    private String genererNumero(){
        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.valueOf(
                (int)(Math.random() * 9000 ) + 100
        );

        return "CMD-" + date + "-" + random;
    }

    // Convertit entite à DTO Response
    public CommandeResponse toResponse(Commande commande){
        CommandeResponse response = new CommandeResponse();
        response.setId(commande.getId());
        response.setNumero(commande.getNumero());
        response.setStatut(commande.getStatut().name());
        response.setTotal(commande.getTotal());
        response.setDateCommande(commande.getDateCommande());
        response.setDatePaiement(commande.getDatePaiement());
        response.setDateExpedition(commande.getDateExpedition());
        response.setUserEmail(commande.getUser().getEmail());

        List<CommandeItemResponse> items = commande.getItems()
                .stream()
                .map(this::itemToResponse)
                .collect(Collectors.toList());
        response.setItems(items);

        return  response;
    }

    private CommandeItemResponse itemToResponse(CommandeItem item){
        CommandeItemResponse response = new CommandeItemResponse();
        response.setId(item.getId());
        response.setQuantite(item.getQuantite());
        response.setPrixUnitaire(item.getPrixUnitaire());
        response.setSousTotal(
                item.getPrixUnitaire()
                        .multiply(BigDecimal.valueOf(item.getQuantite()))
        );
        response.setProduitId(item.getProduit().getId());
        response.setProduitNom(item.getProduit().getNom());
        return response;

    }


}
