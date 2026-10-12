package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Maintenance;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IMaintenanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        verifier(maintenance);

        if (maintenance.getIdMaintenance() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle maintenance ne doit pas avoir d'identifiant"
            );
        }

        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Maintenance", id)
                );
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        Maintenance existante = findById(id);
        verifier(maintenance);

        existante.setDateDebut(maintenance.getDateDebut());
        existante.setDateFin(maintenance.getDateFin());
        existante.setDescription(maintenance.getDescription());
        existante.setVehicule(maintenance.getVehicule());

        return maintenanceRepository.save(existante);
    }

    @Override
    public void deleteById(Long id) {
        maintenanceRepository.delete(findById(id));
    }

    private void verifier(Maintenance maintenance) {
        if (maintenance.getDateDebut() == null
                || maintenance.getDateFin() == null
                || maintenance.getDateFin()
                .isBefore(maintenance.getDateDebut())) {
            throw new IllegalArgumentException(
                    "Les dates de maintenance sont invalides"
            );
        }
    }
}
