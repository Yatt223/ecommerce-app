package com.mohamedbakaryyattoura.backend.service;

import com.mohamedbakaryyattoura.backend.dto.request.CategorieRequest;
import com.mohamedbakaryyattoura.backend.dto.response.CategorieResponse;
import com.mohamedbakaryyattoura.backend.entity.Categorie;
import com.mohamedbakaryyattoura.backend.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorieService {
    private final CategorieRepository categorieRepository;

    // Recupere toutes les categories
    public List<CategorieResponse> getToutesCategories(){
        return  categorieRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Récuperer une categorie par id
    public CategorieResponse getCategorieParId(Long id){
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Catégorie introuvable : " + id)
                );
        return toResponse(categorie);
    }

    // Créé une nouvelle categorie
    public  CategorieResponse creerCategorie(CategorieRequest request){
        if(categorieRepository.existsByNom(request.getNom())){
            throw new RuntimeException("Cette catégorie existe déjà !");
        }

        Categorie categorie = new Categorie();
        categorie.setNom(request.getNom());
        categorie.setDescription(request.getDescription());

        return toResponse(categorieRepository.save(categorie));
    }

    // Modifie une catégorie
    public CategorieResponse modifierCategorie(Long id, CategorieRequest request){
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Categorie introuvable : " + id)
                );
        categorie.setNom(request.getNom());
        categorie.setDescription(request.getDescription());

        return toResponse(categorieRepository.save(categorie));
    }

    // Supprime une categorie
    public void supprimerCategorie(Long id){
        if(!categorieRepository.existsById(id)){
            throw new RuntimeException("Catégorie introuble : " + id);
        }
        categorieRepository.deleteById(id);
    }

    // Convertit entite à DTO Response
    public CategorieResponse toResponse(Categorie categorie){
        CategorieResponse response = new CategorieResponse();
        response.setId(categorie.getId());
        response.setNom(categorie.getNom());
        response.setDescription(categorie.getDescription());
        response.setNombreProduits(
                categorie.getProduits() != null ?
                        categorie.getProduits().size() : 0);
        return response;
    }
}
