package com.mohamedbakaryyattoura.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private boolean actif;
    private LocalDateTime dateInscription;
    private Set<String> roles;
}
