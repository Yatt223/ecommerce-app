package com.mohamedbakaryyattoura.backend.controller;

import com.mohamedbakaryyattoura.backend.dto.request.PanierItemRequest;
import com.mohamedbakaryyattoura.backend.dto.response.PanierResponse;
import com.mohamedbakaryyattoura.backend.service.PanierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/panier")
@RequiredArgsConstructor
public class PanierController {
    private final PanierService panierService;

    // GET /api/panier/1
    public ResponseEntity<PanierResponse> getPanier(@PathVariable Long userId){
        return ResponseEntity.ok(panierService.getPanier(userId));
    }

    // POST /api/panier/1/ajouter
    @PostMapping("/{userId}/ajouter")
    public ResponseEntity<PanierResponse> ajouterAupanier(@PathVariable Long userId, @Valid @RequestBody PanierItemRequest request){
        return ResponseEntity.ok(panierService.ajoutAuPanier(userId, request));
    }
    // PUT /api/panier/1/items/2?quantite=3
    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<PanierResponse> mettreAJourQuantite(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @RequestParam Integer quantite){
        return ResponseEntity.ok(
                panierService.mettreAJourQuantite(userId, itemId, quantite)
        );
    }

    // DELETE /api/panier/1/items/2
    @DeleteMapping("/{userId}/vider")
    public ResponseEntity<?> viderPanier(@PathVariable Long userId){
        panierService.videPanier(userId);
        return ResponseEntity.ok("Panier vidé");
    }

}
