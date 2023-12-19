package fr.univrouen.delivery.service;

import fr.univrouen.delivery.model.Livreur;
import fr.univrouen.delivery.repository.LivreurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivreurService {
    private final LivreurRepository livreurRepository;

    public LivreurService(LivreurRepository livreurRepository) {
        this.livreurRepository = livreurRepository;
    }
    public List<Livreur> findAll() {
        return livreurRepository.findAll();
    }
    public Optional<Livreur> get(Long id) {
        return livreurRepository.findById(id);
    }
    public Livreur save(Livreur livreur) {
        return livreurRepository.save(livreur);
    }
    public void delete(Long id) { livreurRepository.deleteById(id); }
    public Optional<Livreur> updateLivreur(Long id, Livreur livreurDetails) {
        return livreurRepository.findById(id)
                .map(livreur -> {
                    livreur.setName(livreurDetails.getName());
                    livreur.setDisponibility(livreurDetails.getDisponibility());
                    return livreurRepository.save(livreur);
                });
    }

}