package com.mohamedbakaryyattoura.backend.service;

import com.mohamedbakaryyattoura.backend.dto.response.UserResponse;
import com.mohamedbakaryyattoura.backend.entity.User;
import com.mohamedbakaryyattoura.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Tous les utilisateurs (admin)
    public List<UserResponse> getTousUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    // Un utilisateur par id
    public UserResponse getUserParId(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("User introuvable : " + id)
                );
        return toResponse(user);

    }
    // Activer / désactiver un utilisateur
    public UserResponse toggleActif(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("User introuvable : " + id)
                );

        user.setActif(!user.isActif());

        return toResponse(userRepository.save(user));
    }

    // Convertit entite à DTO
    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setEmail(user.getEmail());
        response.setActif(user.isActif());
        response.setDateInscription(user.getDateInscription());

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getNom().name())
                .collect(Collectors.toSet());
        response.setRoles(roles);
        return response;
    }
}
