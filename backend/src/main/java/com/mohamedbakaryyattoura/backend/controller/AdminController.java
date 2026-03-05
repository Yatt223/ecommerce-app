package com.mohamedbakaryyattoura.backend.controller;

import com.mohamedbakaryyattoura.backend.dto.response.CommandeResponse;
import com.mohamedbakaryyattoura.backend.dto.response.UserResponse;
import com.mohamedbakaryyattoura.backend.entity.Commande;
import com.mohamedbakaryyattoura.backend.service.CommandeService;
import com.mohamedbakaryyattoura.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CommandeService commandeService;

    // DASHBOARD +++++++++++++++++++++++++
    // GET /api/admin/dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(){
        List<UserResponse> users = userService.getTousUsers();
        List<CommandeResponse> commandes = commandeService.getToutesCommandes();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", users.size());
        stats.put("totalCommandes", commandes.size());
        stats.put("commandesEnAttente", commandes.stream()
                .filter(c-> c.getStatut().equals("EN_ATTENTE")).count());
        stats.put("chiffreAffaires", commandes.stream()
                .filter(c->!c.getStatut().equals("ANNulee"))
                .mapToDouble(c->c.getTotal().doubleValue())
                .sum()
        );
        return ResponseEntity.ok(stats);
    }

    // ++++++++++GESTION UTILISATEURS ++++++++++++++++
    // GET /api/admin/users
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getTousUsers(){
        return ResponseEntity.ok(userService.getTousUsers());
    }

    // PUT api/admin/users/1/toogle
    @PutMapping("/users/{id}/toggle")
    public ResponseEntity<UserResponse> toggleUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.toggleActif(id));
    }

    //.+++++++++++++++++++++GESTION COMMANDES****************
    // PUT /api/admin/commandes/1/statut?satut=PAYEE
    @PutMapping("/commandes/{id}/staut")
    public ResponseEntity<CommandeResponse> changerStatut(@PathVariable Long id,
                                                          @RequestParam Commande.StatutCommande statut){
        return ResponseEntity.ok(commandeService.changerStatut(id, statut));
    }
}
