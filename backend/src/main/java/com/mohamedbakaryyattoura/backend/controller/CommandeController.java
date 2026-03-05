package com.mohamedbakaryyattoura.backend.controller;

import com.mohamedbakaryyattoura.backend.dto.response.CommandeResponse;
import com.mohamedbakaryyattoura.backend.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
