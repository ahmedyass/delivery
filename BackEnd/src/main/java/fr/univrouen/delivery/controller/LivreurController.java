package fr.univrouen.delivery.controller;

import fr.univrouen.delivery.model.Livreur;
import fr.univrouen.delivery.service.LivreurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/livreurs")
public class LivreurController {
    private final LivreurService livreurService;

    public LivreurController(LivreurService livreurService) {
        this.livreurService = livreurService;
    }

    @GetMapping
    public ResponseEntity<Page<Livreur>> getAllLivreurs(@RequestParam(required = false) String name, Pageable pageable) {
        if (name != null) {
            return ResponseEntity.ok(livreurService.findByName(name, pageable));
        }
        return ResponseEntity.ok(livreurService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Livreur> createLivreur(@RequestBody Livreur livreur) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livreurService.save(livreur));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLivreur(@PathVariable Long id) {
        return livreurService.get(id)
                .map(livreur -> ResponseEntity.ok().body(livreur))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLivreur(@PathVariable Long id, @RequestBody Livreur livreurDetails) {
        Optional<Livreur> updatedLivreur = livreurService.updateLivreur(id, livreurDetails);
        return updatedLivreur
                .map(livreur -> ResponseEntity.ok().body(livreur))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLivreur(@PathVariable Long id) {
        livreurService.delete(id);
    }
}
