package com.mohamedbakaryyattoura.backend.repository;

import com.mohamedbakaryyattoura.backend.entity.PanierItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PanierItemRepository extends JpaRepository<PanierItem, Long> {
    Optional<PanierItem> findByPanierIdAndProduitId(Long panierId, Long produitId);
}
