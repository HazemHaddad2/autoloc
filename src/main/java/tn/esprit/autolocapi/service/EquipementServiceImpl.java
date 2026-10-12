package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Equipement;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IEquipementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement create(Equipement equipement) {
        verifier(equipement);

        if (equipement.getIdEquipement() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel equipement ne doit pas avoir d'identifiant"
            );
        }

        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipement", id)
                );
    }

    @Override
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        Equipement existant = findById(id);
        verifier(equipement);

        existant.setLibelle(equipement.getLibelle());

        return equipementRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        equipementRepository.delete(findById(id));
    }

    private void verifier(Equipement equipement) {
        if (equipement.getLibelle() == null
                || equipement.getLibelle().isBlank()) {
            throw new IllegalArgumentException(
                    "Le libelle de l'equipement est obligatoire"
            );
        }
    }
}
