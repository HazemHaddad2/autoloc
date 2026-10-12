package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Agence;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence create(Agence agence) {
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant"
            );
        }

        verifierAgence(agence);
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", id)
                );
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence agenceExistante = findById(id);

        verifierAgence(agence);

        agenceExistante.setNom(agence.getNom());
        agenceExistante.setVille(agence.getVille());
        agenceExistante.setAdresse(agence.getAdresse());
        agenceExistante.setTelephone(agence.getTelephone());

        return agenceRepository.save(agenceExistante);
    }

    @Override
    public void deleteById(Long id) {
        Agence agence = findById(id);
        agenceRepository.delete(agence);
    }

    private void verifierAgence(Agence agence) {
        if (agence.getNom() == null || agence.getNom().isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom de l'agence est obligatoire"
            );
        }

        if (agence.getVille() == null || agence.getVille().isBlank()) {
            throw new IllegalArgumentException(
                    "La ville de l'agence est obligatoire"
            );
        }

        if (agence.getAdresse() == null || agence.getAdresse().isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse de l'agence est obligatoire"
            );
        }
    }
}
