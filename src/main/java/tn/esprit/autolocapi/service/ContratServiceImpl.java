
package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Contrat;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IContratRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat create(Contrat contrat) {
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }

        if (contrat.getMontantTotal() == null
                || contrat.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException(
                    "Le montant total du contrat doit être positif ou nul");
        }

        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        Contrat existant = findById(id);

        if (contrat.getMontantTotal() == null
                || contrat.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException(
                    "Le montant total du contrat doit être positif ou nul");
        }

        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.getValide());

        return contratRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        Contrat contrat = findById(id);
        contratRepository.delete(contrat);
    }
}
