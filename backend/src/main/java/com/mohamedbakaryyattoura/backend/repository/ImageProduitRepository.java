package com.mohamedbakaryyattoura.backend.repository;

import com.mohamedbakaryyattoura.backend.entity.ImageProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProduitRepository extends JpaRepository<ImageProduit, Long> {
    List<ImageProduit> findByProduitId(Long produitId);
}
