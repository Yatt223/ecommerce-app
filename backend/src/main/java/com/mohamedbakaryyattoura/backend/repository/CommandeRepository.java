package com.mohamedbakaryyattoura.backend.repository;

import com.mohamedbakaryyattoura.backend.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByUserIdOrderByDateCommandeDesc(Long userId);
    Optional<Commande> findByNumero(String numero);
}
