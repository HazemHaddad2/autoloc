package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Employe;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IEmployeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        verifier(employe);

        if (employe.getId_employe() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employe ne doit pas avoir d'identifiant"
            );
        }

        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employe", id)
                );
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existant = findById(id);
        verifier(employe);

        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        existant.setAgence(employe.getAgence());

        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        employeRepository.delete(findById(id));
    }

    private void verifier(Employe employe) {
        if (employe.getNom() == null || employe.getNom().isBlank()
                || employe.getPrenom() == null
                || employe.getPrenom().isBlank()
                || employe.getRole() == null) {
            throw new IllegalArgumentException(
                    "Le nom, le prenom et le role sont obligatoires"
            );
        }
    }
}
