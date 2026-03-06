package com.mohamedbakaryyattoura.backend.controller;

import com.mohamedbakaryyattoura.backend.entity.Role;
import com.mohamedbakaryyattoura.backend.entity.User;
import com.mohamedbakaryyattoura.backend.repository.RoleRepository;
import com.mohamedbakaryyattoura.backend.repository.UserRepository;
import com.mohamedbakaryyattoura.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // POST /api/auth/inscription
    @PostMapping("/inscription")
    public ResponseEntity<?> inscrire(
            @RequestBody Map<String, String> request) {
        try {

            // Log pour voir ce qui arrive
            System.out.println("=== INSCRIPTION ===");
            System.out.println("Email reçu : " + request.get("email"));
            System.out.println("Nom reçu : " + request.get("nom"));

            // Vérifie les champs obligatoires
            if (request.get("email") == null ||
                    request.get("motDePasse") == null ||
                    request.get("nom") == null ||
                    request.get("prenom") == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message",
                                "Tous les champs sont obligatoires !"
                        ));
            }

            // Vérifie si l'email existe déjà
            if (userRepository.existsByEmail(request.get("email"))) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email déjà utilisé !"));
            }

            // Vérifie que les rôles existent
            System.out.println("Recherche du rôle ROLE_USER...");
            List<Role> tousLesRoles = roleRepository.findAll();
            System.out.println("Rôles en base : " + tousLesRoles.size());
            tousLesRoles.forEach(r ->
                    System.out.println(" - " + r.getNom())
            );

            Role roleUser = roleRepository
                    .findByNom(Role.RoleType.ROLE_USER)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Rôle USER introuvable ! " +
                                            "Insère les rôles en base de données."
                            )
                    );

            // Crée l'utilisateur
            User user = new User();
            user.setNom(request.get("nom"));
            user.setPrenom(request.get("prenom"));
            user.setEmail(request.get("email"));
            user.setMotDePasse(
                    passwordEncoder.encode(request.get("motDePasse"))
            );
            user.setRoles(Set.of(roleUser));

            userRepository.save(user);
            System.out.println("Utilisateur créé avec succès !");

            return ResponseEntity.ok(
                    Map.of("message", "Inscription réussie !")
            );

        } catch (Exception e) {
            System.err.println("Erreur inscription : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // POST /api/auth/connexion
    @PostMapping("/connexion")
    public ResponseEntity<?> connecter(
            @RequestBody Map<String, String> request) {
        try {

            System.out.println("=== CONNEXION ===");
            System.out.println("Email : " + request.get("email"));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.get("email"),
                            request.get("motDePasse")
                    )
            );

            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(request.get("email"));
            String token = jwtService.genererToken(userDetails);

            User user = userRepository
                    .findByEmail(request.get("email"))
                    .orElseThrow();

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("nom", user.getNom());
            response.put("prenom", user.getPrenom());
            response.put("roles", user.getRoles()
                    .stream()
                    .map(r -> r.getNom().name())
                    .toList()
            );

            System.out.println("Connexion réussie !");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Erreur connexion : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // GET /api/auth/profil
    @GetMapping("/profil")
    public ResponseEntity<?> getProfil(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtService.extraireEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Utilisateur introuvable !")
                    );

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("nom", user.getNom());
            response.put("prenom", user.getPrenom());
            response.put("roles", user.getRoles()
                    .stream()
                    .map(r -> r.getNom().name())
                    .toList()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}