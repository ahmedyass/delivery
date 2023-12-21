package fr.univrouen.delivery.repository;

import fr.univrouen.delivery.model.Livreur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Page<Livreur> findAll(Pageable pageable);
    Page<Livreur> findByNameContainingIgnoreCase(String name, Pageable pageable);

    //Page<Livreur> findByNameContainingIgnoreCaseAndCreationDateBetweenAndDisponibility(String name, Date startDate, Date endDate, Boolean disponibility, Pageable pageable);

    //Page<Livreur> findByCreationDateBetweenAndDisponibility(Date startDate, Date endDate, Boolean disponibility, Pageable pageable);
}