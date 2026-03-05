package com.mohamedbakaryyattoura.backend.repository;

import com.mohamedbakaryyattoura.backend.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    @Query("SELECT p FROM Produit p WHERE " +
            "(LOWER(p.nom) LIKE LOWER(CONCAT('%', :mot, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :mot, '%'))) " +
            "AND p.actif = true")
    Page<Produit> rechercherParMotCle(
            @Param("mot") String mot,
            Pageable pageable
    );

    Page<Produit> findByCategorieIdAndActifTrue(
            Long categorieId,
            Pageable pageable
    );

    Page<Produit> findByActifTrue(Pageable pageable);
}