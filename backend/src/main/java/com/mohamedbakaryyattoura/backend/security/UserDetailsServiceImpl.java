package com.mohamedbakaryyattoura.backend.security;

import com.mohamedbakaryyattoura.backend.entity.Role;
import com.mohamedbakaryyattoura.backend.entity.User;
import com.mohamedbakaryyattoura.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Utilisateur non trouvé : " + email
                        )
                );

        // Convertit les rôles en authorities manuellement
        Set<Role> roles = user.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(
                    new SimpleGrantedAuthority(role.getNom().name())
            );
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getMotDePasse())
                .authorities(authorities)
                .build();
    }
}