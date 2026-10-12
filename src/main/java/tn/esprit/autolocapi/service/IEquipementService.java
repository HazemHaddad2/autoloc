package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.domain.Equipement;
import java.util.List;

public interface IEquipementService {
    Equipement create(Equipement equipement);
    Equipement findById(Long id);
    List<Equipement> findAll();
    Equipement update(Long id, Equipement equipement);
    void deleteById(Long id);
}
