package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Vehicule;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        verifier(vehicule);

        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau vehicule ne doit pas avoir d'identifiant"
            );
        }

        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", id)
                );
    }

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existant = findById(id);
        verifier(vehicule);

        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        existant.setAgence(vehicule.getAgence());
        existant.setEquipements(vehicule.getEquipements());

        return vehiculeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        vehiculeRepository.delete(findById(id));
    }

    private void verifier(Vehicule vehicule) {
        if (vehicule.getImmatriculation() == null
                || vehicule.getImmatriculation().isBlank()
                || vehicule.getMarque() == null
                || vehicule.getMarque().isBlank()
                || vehicule.getModele() == null
                || vehicule.getModele().isBlank()
                || vehicule.getCategorie() == null
                || vehicule.getTarifJournalier() == null
                || vehicule.getTarifJournalier().signum() < 0
                || vehicule.getStatut() == null) {
            throw new IllegalArgumentException(
                    "Les informations du vehicule sont invalides"
            );
        }
    }
}
