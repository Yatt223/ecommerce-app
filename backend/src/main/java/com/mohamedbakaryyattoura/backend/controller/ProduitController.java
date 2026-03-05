package com.mohamedbakaryyattoura.backend.controller;

import com.mohamedbakaryyattoura.backend.dto.request.ProduitRequest;
import com.mohamedbakaryyattoura.backend.dto.response.ProduitResponse;
import com.mohamedbakaryyattoura.backend.service.ProduitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    // GET /api/produits?page=0&taille=10&tri=dateCreation
    @GetMapping
    public ResponseEntity<Page<ProduitResponse>> getTousProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int taille,
            @RequestParam(defaultValue = "dateCreation") String tri) {
        return ResponseEntity.ok(
                produitService.getTousProduits(page, taille, tri)
        );
    }

    // GET /api/produits/recherche?mot=phone&page=0&taille=10
    @GetMapping("/recherche")
    public ResponseEntity<Page<ProduitResponse>> rechercherProduits(
            @RequestParam String mot,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int taille) {
        return ResponseEntity.ok(
                produitService.rechercherProduits(mot, page, taille)
        );
    }

    // GET /api/produits/categorie/1?page=0&taille=10
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<Page<ProduitResponse>> getProduitsByCategorie(
            @PathVariable Long categorieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int taille) {
        return ResponseEntity.ok(
                produitService.getProduitsByCategorie(categorieId, page, taille)
        );
    }

    // GET /api/produits/1
    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponse> getProduitParId(
            @PathVariable Long id) {
        return ResponseEntity.ok(produitService.getProduitParId(id));
    }

    // POST /api/produits
    @PostMapping
    public ResponseEntity<ProduitResponse> creerProduit(
            @Valid @RequestBody ProduitRequest request) {
        return ResponseEntity.ok(produitService.creerProduit(request));
    }

    // PUT /api/produits/1
    @PutMapping("/{id}")
    public ResponseEntity<ProduitResponse> modifierProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRequest request) {
        return ResponseEntity.ok(
                produitService.modifierProduit(id, request)
        );
    }

    // DELETE /api/produits/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerProduit(@PathVariable Long id) {
        produitService.supprimerProduit(id);
        return ResponseEntity.ok("Produit désactivé !");
    }
}