package fr.univrouen.delivery.repository;

import fr.univrouen.delivery.model.Livreur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Page<Livreur> findAll(Pageable pageable);
    Page<Livreur> findByNameContaining(String name, Pageable pageable);


}