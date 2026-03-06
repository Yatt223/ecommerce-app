package com.mohamedbakaryyattoura.backend.controller;

import com.mohamedbakaryyattoura.backend.dto.response.CommandeResponse;
import com.mohamedbakaryyattoura.backend.entity.Commande;
import com.mohamedbakaryyattoura.backend.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    // POST /api/commandes/1/creer
    @PostMapping("/{userId}/creer")
    public ResponseEntity<CommandeResponse> creerCommandes(
            @PathVariable Long userId ){
        return ResponseEntity.ok(commandeService.creerCommande(userId));
    }
    // GET /api/commandes/1
    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponse> getCommandeParId(
            @PathVariable Long id ){
        return ResponseEntity.ok(commandeService.getCommandeParId(id));
    }

    // GET /api/commandes/user/1  ✅ MANQUAIT
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommandeResponse>> getCommandesUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(commandeService.getCommandesUser(userId));
    }

    // GET /api/commandes (admin)
    @GetMapping
    public ResponseEntity<List<CommandeResponse>> getToutesCommandes() {
        return ResponseEntity.ok(commandeService.getToutesCommandes());
    }

    // PATCH /api/commandes/1/statut?statut=PAYEE (admin)
    @PatchMapping("/{id}/statut")
    public ResponseEntity<CommandeResponse> changerStatut(
            @PathVariable Long id,
            @RequestParam Commande.StatutCommande statut) {
        return ResponseEntity.ok(commandeService.changerStatut(id, statut));
    }
}
