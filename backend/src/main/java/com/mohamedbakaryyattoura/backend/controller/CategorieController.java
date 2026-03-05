package com.mohamedbakaryyattoura.backend.controller;

import com.mohamedbakaryyattoura.backend.dto.request.CategorieRequest;
import com.mohamedbakaryyattoura.backend.dto.response.CategorieResponse;
import com.mohamedbakaryyattoura.backend.service.CategorieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {
    private final CategorieService categorieService;

    // GET /api/categories
    @GetMapping
    public ResponseEntity<List<CategorieResponse>> getToutesCategories(){
        return ResponseEntity.ok(categorieService.getToutesCategories());
    }
    // GET /api/categories/1
    @GetMapping("/{id}")
    public ResponseEntity<CategorieResponse> getCategorieParId(@PathVariable Long id){
        return ResponseEntity.ok(categorieService.getCategorieParId(id));
    }
    // POST /api/categories
    @PostMapping
    public ResponseEntity<CategorieResponse> creerCategorie(@Valid @RequestBody CategorieRequest request){
        return ResponseEntity.ok(categorieService.creerCategorie(request));
    }
    // PUt /api/categories/1
    @PutMapping("/{id}")
    public ResponseEntity<CategorieResponse> modifierCategorie(@PathVariable Long id, @Valid @RequestBody CategorieRequest request){
        return ResponseEntity.ok(categorieService.modifierCategorie(id, request));
    }
    // DELETE /api/categories/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerCategorie(@PathVariable Long id){
        categorieService.supprimerCategorie(id);
        return ResponseEntity.ok("Catégorie supprimée !");
    }
}
