package com.mohamedbakaryyattoura.backend.service;

import com.mohamedbakaryyattoura.backend.dto.request.ProduitRequest;
import com.mohamedbakaryyattoura.backend.dto.response.ProduitResponse;
import com.mohamedbakaryyattoura.backend.entity.Categorie;
import com.mohamedbakaryyattoura.backend.entity.Produit;
import com.mohamedbakaryyattoura.backend.repository.CategorieRepository;
import com.mohamedbakaryyattoura.backend.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final CategorieService categorieService;

    // Recuperer tous les produits actifs avec pagination
    public Page<ProduitResponse> getTousProduits(int page, int taille, String tri){
        Pageable pageable = PageRequest.of(page, taille, Sort.by(tri).descending());
        return produitRepository.findByActifTrue(pageable)
                .map(this::toResponse);
    }
    // Recherche par mot clé
    public Page<ProduitResponse> rechercherProduits(String mot, int page, int taille ){
        Pageable pageable = PageRequest.of(page,taille);
        return produitRepository.rechercherParMotCle(mot, pageable)
                .map(this:: toResponse);
    }

    // Filterer par catégorie
    public Page<ProduitResponse> getProduitsByCategorie(Long categorieId, int page, int taille){
        Pageable pageable = PageRequest.of(page,taille);
        return produitRepository.findByCategorieIdAndActifTrue(categorieId,pageable)
                .map(this::toResponse);
    }

    // Recuperer un produit par id
    public ProduitResponse getProduitParId(Long id){
        Produit produit = produitRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Produit introuvable : "+ id)
                );
        return toResponse(produit);
    }

    // creer un nouveau produit
    public ProduitResponse creerProduit(ProduitRequest request){
        Categorie categorie = categorieRepository
                .findById(request.getCategorieId())
                .orElseThrow(()->
                        new RuntimeException("Categorie introuvable !")
                );
        Produit produit = new Produit();
        produit.setNom(request.getNom());
        produit.setDescription(request.getDescription());
        produit.setPrix(request.getPrix());
        produit.setStock(request.getStock());
        produit.setCategorie(categorie);
        produit.setActif(true);

        return toResponse(produitRepository.save(produit));
    }

    // Modifie un produit
    public ProduitResponse modifierProduit(Long id, ProduitRequest request){
        Produit produit = produitRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Produit introuvable : " + id)
                );
        Categorie categorie = categorieRepository.findById(request.getCategorieId())
                        .orElseThrow(()->
                                new RuntimeException("Catégorie introuvable !"));
        produit.setNom(request.getNom());
        produit.setDescription(request.getDescription());
        produit.setPrix(request.getPrix());
        produit.setStock(request.getStock());
        produit.setCategorie(categorie);
        return toResponse(produitRepository.save(produit));
    }

    // Desactiver un produit (sift delete)
    public void  supprimerProduit(Long id){
        Produit produit = produitRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Produit introuvable")
                );
        produit.setActif(false);
        produitRepository.save(produit);
    }
    // Convertit entite à DTO Response
    public ProduitResponse toResponse(Produit produit){
        ProduitResponse response = new ProduitResponse();
        response.setId(produit.getId());
        response.setNom(produit.getNom());
        response.setDescription(produit.getDescription());
        response.setPrix(produit.getPrix());
        response.setStock(produit.getStock());
        response.setActif(produit.isActif());
        response.setDateCreation(produit.getDateCreation());

        if(produit.getCategorie()!= null){
            response.setCategorie(
                    categorieService.toResponse(produit.getCategorie())
            );
        }

        // Image
        if(produit.getImages() != null){
            List<String> urls = produit.getImages()
                    .stream()
                    .map(img -> img.getUrl())
                    .collect(Collectors.toList());
            response.setImages(urls);
        }
        return  response;
    }
}
